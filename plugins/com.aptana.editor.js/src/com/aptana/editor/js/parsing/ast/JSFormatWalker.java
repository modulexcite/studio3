package com.aptana.editor.js.parsing.ast;

import beaver.Symbol;

import com.aptana.parsing.ast.IParseNode;
import com.aptana.parsing.io.SourcePrinter;

public class JSFormatWalker extends JSTreeWalker
{
	private SourcePrinter _printer;

	/**
	 * JSFormatWalker
	 */
	public JSFormatWalker()
	{
		this._printer = new SourcePrinter();
	}

	/**
	 * addSemicolon
	 * 
	 * @param node
	 */
	protected void addSemicolon(IParseNode node)
	{
		if (node instanceof JSNode)
		{
			if (((JSNode) node).getSemicolonIncluded())
			{
				this._printer.print(";"); //$NON-NLS-1$
			}
		}
	}

	/**
	 * formatBinaryOperator
	 * 
	 * @param node
	 * @param lhs
	 * @param operator
	 * @param rhs
	 */
	protected void formatBinaryOperator(IParseNode node, IParseNode lhs, Symbol operator, IParseNode rhs)
	{
		this.formatNode(lhs);
		this._printer.print(" ").print(operator.value).print(" "); //$NON-NLS-1$ //$NON-NLS-2$
		this.formatNode(rhs);
		this.addSemicolon(node);
	}

	/**
	 * formatNaryNode
	 * 
	 * @param node
	 * @param openText
	 * @param delimiter
	 * @param closeText
	 */
	protected void formatNaryNode(IParseNode node, String openText, String delimiter, String closeText)
	{
		this._printer.print(openText);

		boolean first = true;

		for (IParseNode child : node)
		{
			if (first == false)
			{
				this._printer.print(delimiter);
			}
			else
			{
				first = false;
			}

			this.formatNode(child);
		}

		this._printer.print(closeText);
		this.addSemicolon(node);
	}

	/**
	 * formatNode
	 * 
	 * @param node
	 */
	protected void formatNode(IParseNode node)
	{
		if (node instanceof JSNode)
		{
			((JSNode) node).accept(this);
		}
	}

	/**
	 * getText
	 * 
	 * @return
	 */
	public String getText()
	{
		return this._printer.toString();
	}

	/**
	 * isNotEmpty
	 * 
	 * @param node
	 * @return
	 */
	protected boolean isNotEmpty(IParseNode node)
	{
		boolean result = true;

		if (node instanceof JSNode)
		{
			result = ((JSNode) node).isEmpty() == false;
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSArgumentsNode)
	 */
	@Override
	public void visit(JSArgumentsNode node)
	{
		this.formatNaryNode(node, "(", ", ", ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSArrayNode)
	 */
	@Override
	public void visit(JSArrayNode node)
	{
		this.formatNaryNode(node, "[", ", ", "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSAssignmentNode)
	 */
	@Override
	public void visit(JSAssignmentNode node)
	{
		this.formatBinaryOperator(node, node.getLeftHandSide(), node.getOperator(), node.getRightHandSide());
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSBinaryArithmeticOperatorNode
	 * )
	 */
	@Override
	public void visit(JSBinaryArithmeticOperatorNode node)
	{
		this.formatBinaryOperator(node, node.getLeftHandSide(), node.getOperator(), node.getRightHandSide());
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSBinaryBooleanOperatorNode)
	 */
	@Override
	public void visit(JSBinaryBooleanOperatorNode node)
	{
		this.formatBinaryOperator(node, node.getLeftHandSide(), node.getOperator(), node.getRightHandSide());
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSBreakNode)
	 */
	@Override
	public void visit(JSBreakNode node)
	{
		Symbol label = node.getLabel();

		this._printer.print("break"); //$NON-NLS-1$

		if (label != null)
		{
			String text = (String) label.value;
			
			if (text != null && text.length() > 0)
			{
				this._printer.print(" ").print(label); //$NON-NLS-1$
			}
		}

		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSCaseNode)
	 */
	@Override
	public void visit(JSCaseNode node)
	{
		this._printer.print("case "); //$NON-NLS-1$
		this.formatNode(node.getExpression());
		this._printer.print(": "); //$NON-NLS-1$

		boolean first = true;

		for (IParseNode child : node)
		{
			if (first)
			{
				first = false;
				continue;
			}

			this.formatNode(child);
		}

		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSCatchNode)
	 */
	@Override
	public void visit(JSCatchNode node)
	{
		this._printer.print("catch ("); //$NON-NLS-1$
		this.formatNode(node.getIdentifier());
		this._printer.print(") "); //$NON-NLS-1$
		this.formatNode(node.getBody());
		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSCommaNode)
	 */
	@Override
	public void visit(JSCommaNode node)
	{
		this.formatNaryNode(node, "", ", ", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSConditionalNode)
	 */
	@Override
	public void visit(JSConditionalNode node)
	{
		this.formatNode(node.getTestExpression());
		this._printer.print(" ? "); //$NON-NLS-1$
		this.formatNode(node.getTrueExpression());
		this._printer.print(" : "); //$NON-NLS-1$
		this.formatNode(node.getFalseExpression());
		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSConstructNode)
	 */
	@Override
	public void visit(JSConstructNode node)
	{
		this._printer.print("new "); //$NON-NLS-1$
		this.formatNode(node.getExpression());
		this.formatNode(node.getArguments());
		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSContinueNode)
	 */
	@Override
	public void visit(JSContinueNode node)
	{
		Symbol label = node.getLabel();

		this._printer.print("continue"); //$NON-NLS-1$

		if (label != null)
		{
			String text = (String) label.value;
			
			if (text != null && text.length() > 0)
			{
				this._printer.print(" ").print(label); //$NON-NLS-1$
			}
		}

		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSDeclarationNode)
	 */
	@Override
	public void visit(JSDeclarationNode node)
	{
		this.formatNode(node.getIdentifier());

		IParseNode value = node.getValue();

		if (value instanceof JSNode)
		{
			if (((JSNode) value).isEmpty() == false)
			{
				this._printer.print(" = "); //$NON-NLS-1$
				this.formatNode(value);
			}
		}

		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSDefaultNode)
	 */
	@Override
	public void visit(JSDefaultNode node)
	{
		this.formatNaryNode(node, "default: ", "", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSDoNode)
	 */
	@Override
	public void visit(JSDoNode node)
	{
		this._printer.print("do "); //$NON-NLS-1$

		IParseNode body = node.getBody();
		
		this.formatNode(body);

		if (body.getNodeType() != JSNodeTypes.STATEMENTS)
		{
			this._printer.print(";"); //$NON-NLS-1$
		}

		this._printer.print(" while ("); //$NON-NLS-1$
		this.formatNode(node.getCondition());
		this._printer.print(")"); //$NON-NLS-1$

		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSElementsNode)
	 */
	@Override
	public void visit(JSElementsNode node)
	{
		this.formatNaryNode(node, "", ", ", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSElisionNode)
	 */
	@Override
	public void visit(JSElisionNode node)
	{
		this.formatNaryNode(node, "", ", ", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/* (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSEmptyNode)
	 */
	@Override
	public void visit(JSEmptyNode node)
	{
		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSFalseNode)
	 */
	@Override
	public void visit(JSFalseNode node)
	{
		this._printer.print(node.getText());
		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSFinallyNode)
	 */
	@Override
	public void visit(JSFinallyNode node)
	{
		this._printer.print("finally "); //$NON-NLS-1$
		this.formatNode(node.getBlock());
		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSForInNode)
	 */
	@Override
	public void visit(JSForInNode node)
	{
		this._printer.print("for ("); //$NON-NLS-1$
		this.formatNode(node.getInitializer());
		this._printer.print(" in "); //$NON-NLS-1$
		this.formatNode(node.getExpression());
		this._printer.print(") "); //$NON-NLS-1$
		this.formatNode(node.getBody());
		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSForNode)
	 */
	@Override
	public void visit(JSForNode node)
	{
		IParseNode initializer = node.getInitializer();
		IParseNode condition = node.getCondition();
		IParseNode advance = node.getAdvance();

		this._printer.print("for ("); //$NON-NLS-1$

		if (this.isNotEmpty(initializer))
		{
			this.formatNode(initializer);
		}
		this._printer.print(";"); //$NON-NLS-1$

		if (this.isNotEmpty(condition))
		{
			this._printer.print(" "); //$NON-NLS-1$
			this.formatNode(condition);
		}
		this._printer.print(";"); //$NON-NLS-1$

		if (this.isNotEmpty(advance))
		{
			this._printer.print(" "); //$NON-NLS-1$
			this.formatNode(advance);
		}
		this._printer.print(") "); //$NON-NLS-1$

		this.formatNode(node.getBody());

		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSFunctionNode)
	 */
	@Override
	public void visit(JSFunctionNode node)
	{
		String name = node.getName().getText();

		this._printer.print("function "); //$NON-NLS-1$

		if (name != null && name.length() > 0)
		{
			this._printer.print(name).print(" "); //$NON-NLS-1$
		}

		this.formatNode(node.getParameters());
		this._printer.print(" "); //$NON-NLS-1$
		this.formatNode(node.getBody());
		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSGetElementNode)
	 */
	@Override
	public void visit(JSGetElementNode node)
	{
		this.formatNode(node.getLeftHandSide());
		this._printer.print('['); //$NON-NLS-1$
		this.formatNode(node.getRightHandSide());
		this._printer.print(']'); //$NON-NLS-1$
		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSGetPropertyNode)
	 */
	@Override
	public void visit(JSGetPropertyNode node)
	{
		this.formatNode(node.getLeftHandSide());
		this._printer.print('.'); //$NON-NLS-1$
		this.formatNode(node.getRightHandSide());
		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSGroupNode)
	 */
	@Override
	public void visit(JSGroupNode node)
	{
		this._printer.print("("); //$NON-NLS-1$
		this.formatNode(node.getExpression());
		this._printer.print(")"); //$NON-NLS-1$
		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSIdentifierNode)
	 */
	@Override
	public void visit(JSIdentifierNode node)
	{
		this._printer.print(node.getText());
		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSIfNode)
	 */
	@Override
	public void visit(JSIfNode node)
	{
		IParseNode trueBlock = node.getTrueBlock();
		IParseNode falseBlock = node.getFalseBlock();

		this._printer.print("if ("); //$NON-NLS-1$
		this.formatNode(node.getCondition());
		this._printer.print(") "); //$NON-NLS-1$
		this.formatNode(trueBlock);

		if (this.isNotEmpty(falseBlock))
		{
			if (trueBlock.getNodeType() != JSNodeTypes.STATEMENTS)
			{
				this._printer.print(";"); //$NON-NLS-1$
			}

			this._printer.print(" else "); //$NON-NLS-1$
			this.formatNode(falseBlock);
		}

		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSInvokeNode)
	 */
	@Override
	public void visit(JSInvokeNode node)
	{
		this.formatNode(node.getExpression());
		this.formatNode(node.getArguments());
		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSLabelledNode)
	 */
	@Override
	public void visit(JSLabelledNode node)
	{
		this.formatNode(node.getLabel());
		this._printer.print(": "); //$NON-NLS-1$
		this.formatNode(node.getBlock());
		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSNameValuePairNode)
	 */
	@Override
	public void visit(JSNameValuePairNode node)
	{
		this.formatNode(node.getName());
		this._printer.print(": "); //$NON-NLS-1$
		this.formatNode(node.getValue());
		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSNullNode)
	 */
	@Override
	public void visit(JSNullNode node)
	{
		this._printer.print(node.getText());
		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSNumberNode)
	 */
	@Override
	public void visit(JSNumberNode node)
	{
		this._printer.print(node.getText());
		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSObjectNode)
	 */
	@Override
	public void visit(JSObjectNode node)
	{
		this.formatNaryNode(node, "{", ", ", "}"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSParametersNode)
	 */
	@Override
	public void visit(JSParametersNode node)
	{
		this.formatNaryNode(node, "(", ", ", ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSParseRootNode)
	 */
	@Override
	public void visit(JSParseRootNode node)
	{
		boolean first = true;

		for (IParseNode child : node)
		{
			if (first == false)
			{
				this._printer.print(" "); //$NON-NLS-1$
			}
			else
			{
				first = false;
			}

			this.formatNode(child);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSPostUnaryOperatorNode)
	 */
	@Override
	public void visit(JSPostUnaryOperatorNode node)
	{
		this.formatNode(node.getExpression());

		switch (node.getNodeType())
		{
			case JSNodeTypes.POST_DECREMENT:
				this._printer.print("--"); //$NON-NLS-1$
				break;

			case JSNodeTypes.POST_INCREMENT:
				this._printer.print("++"); //$NON-NLS-1$
				break;
		}

		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSPreUnaryOperatorNode)
	 */
	@Override
	public void visit(JSPreUnaryOperatorNode node)
	{
		IParseNode expression = node.getExpression();

		switch (node.getNodeType())
		{
			case JSNodeTypes.DELETE:
				this._printer.print("delete "); //$NON-NLS-1$
				break;

			case JSNodeTypes.LOGICAL_NOT:
				this._printer.print("!"); //$NON-NLS-1$
				break;

			case JSNodeTypes.NEGATIVE:
				this._printer.print("-"); //$NON-NLS-1$
				break;

			case JSNodeTypes.PRE_DECREMENT:
				this._printer.print("--"); //$NON-NLS-1$
				break;

			case JSNodeTypes.POSITIVE:
				this._printer.print("+"); //$NON-NLS-1$
				break;

			case JSNodeTypes.PRE_INCREMENT:
				this._printer.print("++"); //$NON-NLS-1$
				break;

			case JSNodeTypes.BITWISE_NOT:
				this._printer.print("~"); //$NON-NLS-1$
				break;

			case JSNodeTypes.TYPEOF:
				this._printer.print("typeof"); //$NON-NLS-1$

				if (expression.getNodeType() != JSNodeTypes.GROUP)
				{
					this._printer.print(" "); //$NON-NLS-1$
				}
				break;

			case JSNodeTypes.VOID:
				this._printer.print("void "); //$NON-NLS-1$
				break;
		}

		this.formatNode(expression);
		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSRegexNode)
	 */
	@Override
	public void visit(JSRegexNode node)
	{
		this._printer.print(node.getText());
		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSReturnNode)
	 */
	@Override
	public void visit(JSReturnNode node)
	{
		IParseNode expression = node.getExpression();

		this._printer.print("return"); //$NON-NLS-1$

		if (this.isNotEmpty(expression))
		{
			this._printer.print(" "); //$NON-NLS-1$
			this.formatNode(expression);
		}

		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSStatementsNode)
	 */
	@Override
	public void visit(JSStatementsNode node)
	{
		this.formatNaryNode(node, "{", "", "}"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSStringNode)
	 */
	@Override
	public void visit(JSStringNode node)
	{
		this._printer.print(node.getText());
		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSSwitchNode)
	 */
	@Override
	public void visit(JSSwitchNode node)
	{
		this._printer.print("switch ("); //$NON-NLS-1$
		this.formatNode(node.getExpression());
		this._printer.print(") {"); //$NON-NLS-1$

		boolean first = true;

		for (IParseNode child : node)
		{
			if (first)
			{
				first = false;
				continue;
			}

			this.formatNode(child);
		}

		this._printer.print("}"); //$NON-NLS-1$

		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSThisNode)
	 */
	@Override
	public void visit(JSThisNode node)
	{
		this._printer.print(node.getText());
		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSThrowNode)
	 */
	@Override
	public void visit(JSThrowNode node)
	{
		this._printer.print("throw "); //$NON-NLS-1$
		this.formatNode(node.getExpression());
		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSTrueNode)
	 */
	@Override
	public void visit(JSTrueNode node)
	{
		this._printer.print(node.getText());
		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSTryNode)
	 */
	@Override
	public void visit(JSTryNode node)
	{
		IParseNode catchBlock = node.getCatchBlock();
		IParseNode finallyBlock = node.getFinallyBlock();

		this._printer.print("try "); //$NON-NLS-1$
		this.formatNode(node.getBody());

		if (this.isNotEmpty(catchBlock))
		{
			this._printer.print(" "); //$NON-NLS-1$
			this.formatNode(catchBlock);
		}

		if (this.isNotEmpty(finallyBlock))
		{
			this._printer.print(" "); //$NON-NLS-1$
			this.formatNode(finallyBlock);
		}

		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSVarNode)
	 */
	@Override
	public void visit(JSVarNode node)
	{
		this.formatNaryNode(node, "var ", ", ", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSWhileNode)
	 */
	@Override
	public void visit(JSWhileNode node)
	{
		this._printer.print("while ("); //$NON-NLS-1$
		this.formatNode(node.getCondition());
		this._printer.print(") "); //$NON-NLS-1$
		this.formatNode(node.getBody());
		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSWithNode)
	 */
	@Override
	public void visit(JSWithNode node)
	{
		this._printer.print("with ("); //$NON-NLS-1$
		this.formatNode(node.getExpression());
		this._printer.print(") "); //$NON-NLS-1$
		this.formatNode(node.getBody());
		this.addSemicolon(node);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visitChildren(com.aptana.editor.js.parsing.ast.JSNode)
	 */
	@Override
	protected void visitChildren(JSNode node)
	{
		super.visitChildren(node);
	}
}
