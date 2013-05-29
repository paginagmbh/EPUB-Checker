/*
 * Copyright (c) 2007 Adobe Systems Incorporated
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of
 *  this software and associated documentation files (the "Software"), to deal in
 *  the Software without restriction, including without limitation the rights to
 *  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 *  the Software, and to permit persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 *  FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 *  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.adobe.epubcheck.opf;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import com.adobe.epubcheck.api.Report;
import com.adobe.epubcheck.ocf.OCFPackage;
import com.adobe.epubcheck.util.EPUBVersion;
import com.adobe.epubcheck.util.Messages;

public class XRefChecker {

	public static final int RT_GENERIC = 0;

	public static final int RT_HYPERLINK = 1;

	public static final int RT_IMAGE = 2;

	public static final int RT_OBJECT = 3;

	public static final int RT_STYLESHEET = 4;

	public static final int RT_AUDIO = 5;

	public static final int RT_VIDEO = 6;
	
	public static final int RT_SVG_PAINT = 0x10;

	public static final int RT_SVG_CLIP_PATH = 0x11;

	public static final int RT_SVG_SYMBOL = 0x12;

	private class Reference {
		String resource;

		int lineNumber;

		int columnNumber;

		String refResource;

		String fragment;

		int type;

		public Reference(String srcResource, int srcLineNumber,
				int srcColumnNumber, String refResource, String fragment,
				int type) {
			this.fragment = fragment;
			this.lineNumber = srcLineNumber;
			this.columnNumber = srcColumnNumber;
			this.refResource = refResource;
			this.resource = srcResource;
			this.type = type;
		}

	}

	private class Anchor {

		String id;

		int lineNumber;

		int columnNumber;

		int type;

		public Anchor(String id, int lineNumber, int columnNumber, int type) {
			this.id = id;
			this.lineNumber = lineNumber;
			this.columnNumber = columnNumber;
			this.type = type;
		}

	}

	private class Resource {

		String resource;

		String mimeType;

		Hashtable<String, Anchor> anchors;

		boolean inSpine;

		boolean hasValidItemFallback;

		boolean hasValidImageFallback;

		Resource(String resource, String type, boolean inSpine,
				boolean hasValidItemFallback, boolean hasValidImageFallback) {
			this.mimeType = type;
			this.resource = resource;
			this.inSpine = inSpine;
			this.hasValidItemFallback = hasValidItemFallback;
			this.hasValidImageFallback = hasValidImageFallback;
			this.anchors = new Hashtable<String, Anchor>();
		}
	}

	Hashtable<String, Resource> resources = new Hashtable<String, Resource>();

	HashSet<String> undeclared = new HashSet<String>();

	Vector<Reference> references = new Vector<Reference>();

	Hashtable<String, String> bindings = new Hashtable<String, String>();

	Report report;

	OCFPackage ocf;

	EPUBVersion version;

	public XRefChecker(OCFPackage ocf, Report report, EPUBVersion version) {
		this.ocf = ocf;
		this.report = report;
		this.version = version;

	}

	public String getMimeType(String path) {
		return resources.get(path) != null ? resources.get(path).mimeType
				: null;
	}

	public Set<String> getBindingsMimeTypes() {
		return bindings.keySet();
	}

	public String getBindingHandlerSrc(String mimeType) {
		return bindings.get(mimeType);
	}

	public void registerBinding(String mimeType, String handlerSrc) {
		bindings.put(mimeType, handlerSrc);
	}

	public void registerResource(String resource, String mimeType,
			boolean inSpine, boolean hasValidItemFallback,
			boolean hasValidImageFallback) {
		if (resources.get(resource) != null)
			throw new IllegalArgumentException("duplicate resource: "
					+ resource);
		resources.put(resource, new Resource(resource, mimeType, inSpine,
				hasValidItemFallback, hasValidImageFallback));
	}

	public void registerAnchor(String resource, int lineNumber,
			int columnNumber, String id, int type) {
		Resource res = (Resource) resources.get(resource);
		if (res == null)
			throw new IllegalArgumentException("unregistered resource: "
					+ resource);
		if (res.anchors.get(id) != null)
			throw new IllegalArgumentException("duplicate id: " + id);
		res.anchors.put(id, new Anchor(id, lineNumber, columnNumber, type));
	}

	public void registerReference(String srcResource, int srcLineNumber,
			int srcColumnNumber, String refResource, String refFragment,
			int type) {
		if (refResource.startsWith("data:"))
			return;		
		references.add(new Reference(srcResource, srcLineNumber,
				srcColumnNumber, refResource, refFragment, type));
	}

	public void registerReference(String srcResource, int srcLineNumber,
			int srcColumnNumber, String ref, int type) {
		if (ref.startsWith("data:"))
			return;
		// check for query string
		// see http://code.google.com/p/epubcheck/issues/detail?id=190
		// see http://code.google.com/p/epubcheck/issues/detail?id=261
		int query = ref.indexOf('?');
		if (query >= 0 && !ref.matches("^[^:/?#]+://.*")) {
			ref = ref.substring(0, query).trim();
		}
		
		int hash = ref.indexOf("#");
		String refResource;
		String refFragment;
		if (hash >= 0) {
			refResource = ref.substring(0, hash);
			refFragment = ref.substring(hash + 1);
		} else {
			refResource = ref;
			refFragment = null;
		}
		registerReference(srcResource, srcLineNumber, srcColumnNumber,
				refResource, refFragment, type);
	}

	public void checkReferences() {
		Enumeration<Reference> refs = references.elements();
		while (refs.hasMoreElements()) {
			Reference ref = (Reference) refs.nextElement();
			checkReference(ref);
		}

	}

	private void checkReference(Reference ref) {
		Resource res = (Resource) resources.get(ref.refResource);
		if (res == null) {
			if(ref.refResource.matches("^[^:/?#]+://.*") 
					&& !(version==EPUBVersion.VERSION_3 && (ref.type==RT_AUDIO || ref.type==RT_VIDEO))) {
				report.error(
						ref.resource,
						ref.lineNumber,
						ref.columnNumber,
						String.format(Messages.OPF_REMOTE_RESOURCE_NOT_ALLOWED, ref.refResource));
			} else if (!ocf.hasEntry(ref.refResource) && !ref.refResource.matches("^[^:/?#]+://.*")) {				
				report.error(
						ref.resource,
						ref.lineNumber,
						ref.columnNumber,
						String.format(Messages.OPF_REF_RESOURCE_MISSING, ref.refResource));
				
			} else if (!undeclared.contains(ref.refResource)) {
				undeclared.add(ref.refResource);
				report.error(
						ref.resource,
						ref.lineNumber,
						ref.columnNumber,
						String.format(Messages.OPF_REF_RESOURCE_NOT_DECLARED, ref.refResource));
			}
			return;
		}
		if (ref.fragment == null) {
			switch (ref.type) {
			case RT_SVG_PAINT:
			case RT_SVG_CLIP_PATH:
			case RT_SVG_SYMBOL:
				report.error(ref.resource, ref.lineNumber, ref.columnNumber,
						String.format(Messages.OPF_FRAGMENT_ID_MISSING, ref.refResource));
				break;
			case RT_HYPERLINK:
				// if mimeType is null, we should have reported an error already
				if (res.mimeType != null
						&& !OPFChecker.isBlessedItemType(res.mimeType, version)
						&& !OPFChecker
								.isDeprecatedBlessedItemType(res.mimeType)
						&& !res.hasValidItemFallback)
					report.error(ref.resource, ref.lineNumber, ref.columnNumber,
							String.format(Messages.OPF_HYPERLINK_TO_NONSTANDARD_RES, ref.refResource, res.mimeType));
				if (/* !res.mimeType.equals("font/opentype") && */!res.inSpine)
					report.warning(ref.resource, ref.lineNumber, ref.columnNumber,
							String.format(Messages.OPF_HYPERLINK_RES_OUTSIDE_SPINE, ref.refResource));
				break;
			case RT_IMAGE:
				// if mimeType is null, we should have reported an error already
				if (res.mimeType != null
						&& !OPFChecker.isBlessedImageType(res.mimeType)
						&& !res.hasValidImageFallback)
					report.error(ref.resource, ref.lineNumber,
							ref.columnNumber,
							String.format(Messages.OPF_NONSTANDARD_IMAGE, ref.refResource, res.mimeType));
				break;
			case RT_STYLESHEET:
				// if mimeType is null, we should have reported an error already

				// Implementations are allowed to process any stylesheet
				// language they desire; so this is an
				// error only if no fallback is available.
				// See also:
				// https://code.google.com/p/epubcheck/issues/detail?id=244

				if (res.mimeType != null
						&& !OPFChecker.isBlessedStyleType(res.mimeType)
						&& !OPFChecker
								.isDeprecatedBlessedStyleType(res.mimeType)
						&& !res.hasValidItemFallback)
					report.error(ref.resource, ref.lineNumber,
							ref.columnNumber, String.format(
									Messages.OPF_NONSTANDARD_STYLESHEET,
									ref.refResource, res.mimeType));
				break;
			}
		} else { //if (ref.fragment == null) {
			
			if(ref.fragment.startsWith("epubcfi(")) {
				//Issue 150
				return;
			}
			
			switch (ref.type) {
			case RT_HYPERLINK:
				// if mimeType is null, we should have reported an error already
				if (res.mimeType != null
						&& !OPFChecker.isBlessedItemType(res.mimeType, version)
						&& !OPFChecker
								.isDeprecatedBlessedItemType(res.mimeType)
						&& !res.hasValidItemFallback)
					report.error(ref.resource, ref.lineNumber,
							ref.columnNumber,
							String.format(Messages.OPF_HYPERLINK_TO_NONSTANDARD_RES, ref.refResource, res.mimeType));
				if (!res.inSpine)
					report.warning(ref.resource, ref.lineNumber,
							ref.columnNumber,
							String.format(Messages.OPF_HYPERLINK_RES_OUTSIDE_SPINE, ref.refResource));
				break;
			case RT_IMAGE:
				report.error(ref.resource, ref.lineNumber, ref.columnNumber,
						String.format(Messages.OPF_FRAGMENT_ID_FOR_IMG, ref.refResource));
				break;
			case RT_STYLESHEET:
				report.error(ref.resource, ref.lineNumber, ref.columnNumber,
						String.format(Messages.OPF_FRAGMENT_ID_FOR_STYLE, ref.refResource));
				break;
			}
			Anchor anchor = (Anchor) res.anchors.get(ref.fragment);
			if (anchor == null) {
				report.error(ref.resource, ref.lineNumber, ref.columnNumber,
						String.format(Messages.OPF_FRAGMENT_ID_NOT_DEFINED_IN, ref.fragment, ref.refResource));
				return;
			} else {
				switch (ref.type) {
				case RT_SVG_PAINT:
				case RT_SVG_CLIP_PATH:
					if (anchor.type != ref.type)
						report.error(
								ref.resource,
								ref.lineNumber,
								ref.columnNumber,
								String.format(Messages.OPF_FRAGMENT_ID_DEFINES_INCOMPATIBLE_RES, ref.fragment, ref.refResource));
					break;
				case RT_SVG_SYMBOL:
				case RT_HYPERLINK:
					if (anchor.type != ref.type && anchor.type != RT_GENERIC)
						report.error(
								ref.resource,
								ref.lineNumber,
								ref.columnNumber,
								String.format(Messages.OPF_FRAGMENT_ID_DEFINES_INCOMPATIBLE_RES, ref.fragment, ref.refResource));
					break;
				}
			}
		}
	}
}
