package org.idpf.epubcheck.util.saxon;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.Int64Value;
import net.sf.saxon.value.SequenceType;

public class ColumnNumberFunction extends ExtensionFunctionDefinition {

	private static final long serialVersionUID = -4202710868367933385L;

	public static StructuredQName QNAME = new StructuredQName("saxon", "http://saxon.sf.net/", "column-number");
	
	@Override
	public StructuredQName getFunctionQName() {
		return QNAME;
	}

	@Override
	public int getMaximumNumberOfArguments() {
		return 1;
	}

	@Override
	public int getMinimumNumberOfArguments() {
		return 0;
	}

	@Override
	public SequenceType[] getArgumentTypes() {
		return new SequenceType[] { SequenceType.SINGLE_NODE };
	}

	@Override
	public SequenceType getResultType(SequenceType[] suppliedArgumentTypes) {
		return SequenceType.SINGLE_INTEGER;
	}

	@Override
	public boolean dependsOnFocus() {
		return true;
	}

	@Override
	public boolean trustResultType() {
		return true;
	}

	@Override
	public ExtensionFunctionCall makeCallExpression() {
		return new ExtensionFunctionCall() {

			private static final long serialVersionUID = -4202710868367933385L;

			public SequenceIterator<? extends Item<?>> call(
					@SuppressWarnings("rawtypes") SequenceIterator[] arguments,
					XPathContext context) throws XPathException {
				if (context.getContextItem() instanceof NodeInfo) {
					return new Int64Value(
							((NodeInfo) context.getContextItem())
									.getColumnNumber()).iterate();
				}
				throw new XPathException(
						"Unexpected XPath context for saxon:column-number");
			}
		};
	}
}
