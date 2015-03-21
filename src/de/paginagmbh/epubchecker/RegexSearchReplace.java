package de.paginagmbh.epubchecker;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * initializes and performs the many regex queries needed to translate the epubcheck results
 * 
 * @author		Tobias Fischer
 * @copyright	pagina GmbH, Tübingen
 * @date 		2013-12-16
 */
public class RegexSearchReplace {

	private ArrayList<String> regex_search = new ArrayList<String>();
	private ArrayList<String> regex_replace = new ArrayList<String>();




	/* ********************************************************************************************************** */

	public RegexSearchReplace() {

		regex_search.clear();
		regex_replace.clear();


		// pagina version 0.x.x

		fillArray("item \\((.*)\\) exists in the zip file, but is not declared in the OPF file",
				__("item ($1) exists in the zip file, but is not declared in the OPF file"));

		fillArray("Length of the first filename in archive must be 8, but was (\\d+)",
				__("Length of the first filename in archive must be 8, but was $1"));

		fillArray("The file (.*) does not appear to be of type (.*)",
				__("The file $1 does not appear to be of type $2"));

		fillArray("image file (.*) is missing",
				__("image file $1 is missing"));

		fillArray("image file (.*) cannot be decrypted",
				__("image file $1 cannot be decrypted"));

		fillArray("image file (.*) is too short",
				__("image file $1 is too short"));

		fillArray("DTBook file (.*) is missing",
				__("DTBook file $1 is missing"));

		fillArray("DTBook file (.*) cannot be decrypted",
				__("DTBook file $1 cannot be decrypted"));

		fillArray("NCX file (.*) is missing",
				__("NCX file $1 is missing"));

		fillArray("NCX file (.*) cannot be decrypted",
				__("NCX file $1 cannot be decrypted"));

		fillArray("OPF file (.*) is missing",
				__("OPF file $1 is missing"));

		fillArray("deprecated media-type (.*)",
				__("deprecated media-type $1"));

		fillArray("use of OPS media-type '(.*)' in OEBPS 1.2 context; use text/x-oeb1-document instead",
				__("use of OPS media-type '$1' in OEBPS 1.2 context; use text/x-oeb1-document instead"));

		fillArray("use of OPS media-type '(.*)' in OEBPS 1.2 context; use text/x-oeb1-css instead",
				__("use of OPS media-type '$1' in OEBPS 1.2 context; use text/x-oeb1-css instead"));

		fillArray("'(.*)' is not a permissible spine media-type",
				__("'$1' is not a permissible spine media-type"));

		fillArray("Spine item with non-standard media-type '(.*)' with no fallback",
				__("Spine item with non-standard media-type '$1' with no fallback"));

		fillArray("Spine item with non-standard media-type '(.*)' with fallback to non-spine-allowed media-type",
				__("Spine item with non-standard media-type '$1' with fallback to non-spine-allowed media-type"));

		fillArray("non-standard media-type '(.*)' with no fallback",
				__("non-standard media-type '$1' with no fallback"));

		fillArray("non-standard media-type '(.*)' with fallback to non-spine-allowed media-type",
				__("non-standard media-type '$1' with fallback to non-spine-allowed media-type"));

		fillArray("item with id '(.*)' not found",
				__("item with id '$1' not found"));

		fillArray("use of deprecated element '(.*)'",
				__("use of deprecated element '$1'"));

		fillArray("role value '(.*)' is not valid",
				__("role value '$1' is not valid"));

		fillArray("date value '(.*)' does not follow recommended syntax as per http://www.w3.org/TR/NOTE-datetime: (.*)",
				__("date value '$1' does not follow recommended syntax as per http://www.w3.org/TR/NOTE-datetime: $2"));

		fillArray("date value '(.*)' is not valid as per http://www.w3.org/TR/NOTE-datetime: \\[For input string: \"(.*)\"\\] is not an integer",
				__("date value '$1' is not valid as per http://www.w3.org/TR/NOTE-datetime: [For input string: '$1'] is not an integer"));

		fillArray("date value '(.*)' is not valid as per http://www.w3.org/TR/NOTE-datetime: zero-length string",
				__("date value '$1' is not valid as per http://www.w3.org/TR/NOTE-datetime: zero-length string"));

		fillArray("date value '(.*)' is not valid.",
				__("date value '$1' is not valid.") + "\n" + "   ");

		fillArray("The date must be in the form YYYY, YYYY-MM or YYYY-MM-DD",
				__("The date must be in the form YYYY, YYYY-MM or YYYY-MM-DD") + "\n" + "   ");

		fillArray("\\(e.g., \"1993\", \"1993-05\", or \"1993-05-01\"\\).",
				__("(e.g., \"1993\", \"1993-05\", or \"1993-05-01\").") + "\n" + "   ");

		fillArray("See http://www.w3.org/TR/NOTE-datetime.",
				__("See http://www.w3.org/TR/NOTE-datetime."));

		fillArray("hyperlink to non-standard resource '(.*)' of type '(.*)'",
				__("hyperlink to non-standard resource '$1' of type '$2'"));

		fillArray("hyperlink to resource outside spine '(.*)'",
				__("hyperlink to resource outside spine '$1'"));

		fillArray("non-standard image resource '(.*)' of type '(.*)'",
				__("non-standard image resource '$1' of type '$2'"));

		fillArray("fragment identifier missing in reference to '(.*)'",
				__("fragment identifier missing in reference to '$1'"));

		fillArray("fragment identifier used for image resource '(.*)'",
				__("fragment identifier used for image resource '$1'"));

		fillArray("fragment identifier used for stylesheet resource '(.*)'",
				__("fragment identifier used for stylesheet resource '$1'"));

		fillArray("fragment identifier is not defined in '(.*)'",
				__("fragment identifier is not defined in '$1'"));

		fillArray("fragment identifier '(.*)' defines incompatible resource type in '(.*)'",
				__("fragment identifier '$1' defines incompatible resource type in '$2'"));

		fillArray("resource (.*) is missing",
				__("resource $1 is missing"));

		fillArray("resource (.*) cannot be decrypted",
				__("resource $1 cannot be decrypted"));

		fillArray("OPS/XHTML file (.*) is missing",
				__("OPS/XHTML file $1 is missing"));

		fillArray("OPS/XHTML file (.*) cannot be decrypted",
				__("OPS/XHTML file $1 cannot be decrypted"));

		fillArray("use of non-registered URI schema type in href: (.*)$",
				__("use of non-registered URI schema type in href: $1"));

		fillArray("Only UTF-8 and UTF-16 encodings are allowed for XML, detected (.*)",
				__("Only UTF-8 and UTF-16 encodings are allowed for XML, detected $1"));

		fillArray("Malformed byte sequence: (.*) Check encoding",
				__("Malformed byte sequence: $1 Check encoding"));

		fillArray("could not parse (.*): duplicate id: (.*)$",
				__("could not parse '$1': duplicate id: '$2'"));

		fillArray("could not parse (.*):",
				__("could not parse $1:"));

		fillArray("element \"(.*)\" from namespace \"(.*)\" not allowed in this context",
				__("element \"$1\" from namespace \"$2\" not allowed in this context"));

		fillArray("unknown element \"(.*)\" from namespace \"(.*)\"",
				__("unknown element \"$1\" from namespace \"$2\""));

		fillArray("attribute \"(.*)\" not allowed at this point; ignored",
				__("attribute \"$1\" not allowed at this point; ignored"));

		fillArray("Epubcheck will not validate ([^\\.]+\\.\\w+)\\b",
				__("Epubcheck will not validate '$1'"));

		fillArray("\\b([^\\.\\s]+\\.\\w+) is an encrypted non-required entry! ",
				__("'$1' is an encrypted non-required entry!"));

		fillArray("\\b([^\\.\\s]+\\.\\w+) is an encrypted required entry! ",
				__("'$1' is an encrypted required entry!"));

		fillArray("element \"([a-z0-9_:]*)\" missing required attribute \"([-a-z0-9_:]*)\"",
				__("element \"$1\" missing required attribute \"$2\""));

		fillArray("attribute \"([-a-z0-9_:]*)\" not allowed here; expected attribute ([a-z0-9-_\",: ]*) or \"([-a-z0-9_:]*)\" \\(with ([^\\)]*)\\)",
				__("attribute \"$1\" not allowed here; expected attribute $2 or \"$3\" (with $4)"));

		fillArray("attribute \"([-a-z0-9_:]*)\" not allowed here; expected attribute ([a-z0-9-_\",: ]*) or \"([-a-z0-9_:]*)\"",
				__("attribute \"$1\" not allowed here; expected attribute $2 or \"$3\""));

		fillArray("attribute \"([-a-z0-9_:]*)\" not allowed here; expected attribute ([a-z0-9-_\",: ]*)",
				__("attribute \"$1\" not allowed here; expected attribute $2"));

		fillArray("element \"([-a-z0-9_:]*)\" not allowed here; expected the element end-tag, text or element ([-a-z0-9_\",: ]*) or \"([-a-z0-9_:]*)\" \\(with ([^\\)]*)\\)",
				__("element \"$1\" not allowed here; expected the element end-tag, text or element $2 or \"$3\" (with $4)"));

		fillArray("element \"([-a-z0-9_:]*)\" not allowed here; expected the element end-tag or element ([-a-z0-9_\",: ]*) or \"([-a-z0-9_:]*)\" \\(with ([^\\)]*)\\)",
				__("element \"$1\" not allowed here; expected the element end-tag or element $2 or \"$3\" (with $4)"));

		fillArray("element \"([-a-z0-9_:]*)\" not allowed here; expected the element end-tag or element ([-a-z0-9_\",: ]*) or \"([-a-z0-9_:]*)\"",
				__("element \"$1\" not allowed here; expected the element end-tag or element $2 or \"$3\""));

		fillArray("element \"([-a-z0-9_:]*)\" not allowed here; expected element ([-a-z0-9_\",: ]*) or \"([-a-z0-9_:]*)\" \\(with ([^\\)]*)\\)",
				__("element \"$1\" not allowed here; expected element $2 or \"$3\" (with $4)"));

		fillArray("element \"([-a-z0-9_:]*)\" not allowed here; expected element ([-a-z0-9_\",: ]*)",
				__("element \"$1\" not allowed here; expected element $2"));

		fillArray("element \"([-a-z0-9_:]*)\" not allowed here; expected the element end-tag",
				__("element \"$1\" not allowed here; expected the element end-tag"));

		fillArray("element \"([-a-z0-9_:]*)\" not allowed anywhere; expected the element end-tag or text",
				__("element \"$1\" not allowed anywhere; expected the element end-tag or text"));

		fillArray("element \"([-a-z0-9_:]*)\" not allowed anywhere; expected element ([-a-z0-9_\",: ]*)",
				__("element \"$1\" not allowed anywhere; expected element $2"));

		fillArray("element \"([-a-z0-9_:]*)\" incomplete; expected element ([-a-z0-9_\",: ]*) or \"([-a-z0-9_:]*)\" \\(with ([^\\)]*)\\)",
				__("element \"$1\" incomplete; expected element $2 or \"$3\" (with $4)"));

		fillArray("zip file contains empty directory (.*)$",
				__("zip file contains empty directory '$1'"));

		fillArray("'(.*)': referenced resource exists, but not declared in the OPF file",
				__("'$1': referenced resource exists, but not declared in the OPF file"));

		fillArray("'(.*)': referenced resource missing in the package",
				__("'$1': referenced resource missing in the package"));

		fillArray("File listed in reference element in guide was not declared in OPF manifest: (.*)$",
				__("File listed in reference element in guide was not declared in OPF manifest: $1"));

		fillArray("External entities are not allowed in XML. External entity declaration found: (.*)$",
				__("External entities are not allowed in XML. External entity declaration found: $1"));

		fillArray("Entity '(.*)' is undeclared",
				__("Entity '$1' is undeclared"));

		fillArray("found attribute \"(.*)\", but no attributes allowed here",
				__("found attribute '$1', but no attributes allowed here"));

		fillArray("This file should declare in opf the property: (.*)$",
				__("This file should declare in opf the property: '$1'"));

		fillArray("value of attribute \"(.*)\" is invalid; must be a string matching the regular expression \"(.*)\", must be a string matching the regular expression \"(.*)\" or must be a string matching the regular expression \"(.*)\"$",
				__("value of attribute '$1' is invalid; must be a string matching the regular expression \"$2\", must be a string matching the regular expression \"$3\" or must be a string matching the regular expression \"$4\""));

		fillArray("value of attribute \"(.*)\" is invalid; must be a string matching the regular expression \"(.*)\"$",
				__("value of attribute '$1' is invalid; must be a string matching the regular expression: \"$2\""));

		fillArray("element \"(.*)\" incomplete; missing required element \"(.*)\"$",
				__("element '$1' incomplete; missing required element '$2'"));



		// pagina version 1.1

		fillArray("'(.*)': referenced resource is not declared in the OPF manifest",
				__("'$1': referenced resource is not declared in the OPF manifest"));

		fillArray("Only UTF-8 and UTF-16 encodings are allowed, detected (.*)$",
				__("Only UTF-8 and UTF-16 encodings are allowed, detected '$1'"));

		fillArray("File (.*) is missing in the package",
				__("File '$1' is missing in the package"));

		fillArray("Token '(.*)' not allowed here, expecting a property name",
				__("Token '$1' not allowed here, expecting a property name"));

		fillArray("Token '(.*)' not allowed here, expecting :",
				__("Token '$1' not allowed here"));

		fillArray("Token '(.*)' not allowed here",
				__("Token '$1' not allowed here"));

		fillArray("Irregular DOCTYPE: found '(.*)', expecting '(.*)'\\.",
				__("Irregular DOCTYPE: found '$1', expecting '$2'."));

		fillArray("text not allowed here; expected element \"(.*)\"",
				__("text not allowed here; expected element '$1'"));

		fillArray("assertion failed: Exactly one '(.*)' nav element must be present",
				__("assertion failed: Exactly one '$1' nav element must be present"));

		fillArray("'(.*)': remote resource reference not allowed; resource must be placed in the OCF",
				__("'$1': remote resource reference not allowed; resource must be placed in the OCF"));

		fillArray("Invalid syntax '(.*)'",
				__("Invalid syntax '$1'"));

		fillArray("The character '(.*)' is not allowed as the first character in '(.*)' expressions",
				__("The character '$1' is not allowed as the first character in '$2' expressions"));

		fillArray("Invalid escape sequence '(.*)'",
				__("Invalid escape sequence '$1'"));

		fillArray("Invalid unicode range expression '(.*)'",
				__("Invalid unicode range expression '$1'"));

		fillArray("Premature end of grammar \\(expecting: (.*)\\)",
				__("Premature end of grammar (expecting: $1)"));

		fillArray("value of attribute \"(.*)\" is invalid; must be an integer",
				__("value of attribute '$1' is invalid; must be an integer"));

		fillArray("assertion failed: Exactly one manifest item must declare the 'nav' property \\(number of 'nav' items: (\\d+)\\)\\.",
				__("assertion failed: Exactly one manifest item must declare the 'nav' property (number of 'nav' items: $1)"));

		fillArray("element \"(.*)\" not allowed yet; missing required element \"(.*)\"",
				__("element '$1' not allowed yet; missing required element '$2'"));

		fillArray("assertion failed: spine element toc attribute must reference the NCX manifest item \\(referenced media type was '(.*)'\\)",
				__("assertion failed: spine element toc attribute must reference the NCX manifest item (referenced media type was '$1')"));



		// pagina version 1.2

		fillArray("This file should declare in the OPF the property: (.*)",
				__("This file should declare in the OPF the property: $1"));

		fillArray("This file should not declare in the OPF the properties: (.*)",
				__("This file should not declare in the OPF the properties: $1"));

		fillArray("meta@dtb:uid content '(.*)' should conform to unique-identifier in content.opf: '(.*)'",
				__("meta@dtb:uid content '$1' should conform to unique-identifier in content.opf: '$2'"));

		fillArray("non-standard stylesheet resource '(.*)' of type '(.*)'. A fallback must be specified.",
				__("non-standard stylesheet resource '$1' of type '$2'. A fallback must be specified."));

		fillArray("File name contains non-ascii characters:(.*)\\. Consider changing filename",
				__("File name contains non-ascii characters: $1 - Consider changing filename"));

		fillArray("Duplicate entry in the ZIP file \\(after Unicode NFC normalization\\): (.*)",
				__("Duplicate entry in the ZIP file (after Unicode NFC normalization): $1"));

		fillArray("Duplicate entry in the ZIP file: (.*)",
				__("Duplicate entry in the ZIP file: $1"));

		fillArray("Guide reference to an item that is not a Content Document: (.*)",
				__("Guide reference to an item that is not a Content Document: $1"));

		fillArray("assertion failed: The \"(.*)\" attribute does not have a unique value",
				__("assertion failed: The '$1' attribute does not have a unique value"));

		fillArray("spine contains multiple references to the manifest item with id (.*)",
				__("spine contains multiple references to the manifest item with id '$1'"));

	}




	/* ********************************************************************************************************** */

	public String doReplace(String s) {

		for(int i=0; i<regex_search.size(); i++) {

			Pattern p = Pattern.compile(regex_search.get(i)); // Create a pattern to match
			Matcher m = p.matcher(s); // Create a matcher with an input string

			while (m.find()) {

//				System.out.println("");
//				System.out.println(p.toString());
//				System.out.println(s);

				s = m.replaceAll(regex_replace.get(i));

//				System.out.println(s);
//				System.out.println("");
			}
		}
		return s;
	}




	/* ********************************************************************************************************** */

	private void fillArray(String search, String replace) {
		regex_search.add(search);
		regex_replace.add(replace);
	}




	/* ********************************************************************************************************** */

	public ArrayList<String> getSearchArray() {
		return regex_search;
	}




	/* ********************************************************************************************************** */

	public ArrayList<String> getReplaceArray() {
		return regex_replace;
	}




	/* ********************************************************************************************************** */

	private static String __(String s) {
		return paginaEPUBChecker.l10n.getString(s);
	}
}