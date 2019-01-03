package de.jepfa.regex.elements;

import java.util.regex.Pattern;

import de.jepfa.regex.components.ChangeableElement;
import de.jepfa.regex.helper.Checker;

/**
 * This class defines characters that should match in the given order as the String describes.
 * This element matches, if all characters of the given String matches.
 *
 * @author Jens Pfahl
 */
public class StringElement extends ChangeableElement {

	private String s;


	public StringElement(String s) {
		Checker.checkNotNull(s);
		this.s = s;
	}

	@Override
	protected String elementToRegex() {
		return Pattern.quote(s);
	}
}