package de.jepfa.regex.elements;

import de.jepfa.regex.components.ChangeableElement;
import de.jepfa.regex.helper.Checker;

/**
 * This element is intended to use when you want to inject Regular Expressions or other Strings 
 * directly to the builder without manipulation. There is no check of correctness.
 *
 * @author Jens Pfahl
 */
public class PlainElement extends ChangeableElement {

	private String s;


	public PlainElement(String s) {
		Checker.checkNotNull(s);
		this.s = s;
	}

	@Override
	protected String elementToRegex() {
		return s.toString();
	}
}