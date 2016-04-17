package de.jepfa.regex.elements;

import de.jepfa.regex.RegexBuilder;
import de.jepfa.regex.components.ChangeableElement;
import de.jepfa.regex.components.Flags;
import de.jepfa.regex.components.SystemElement;


/**
 * This element contains all predefined Regular Expession classes as static constants.
 * <p>
 * Because changes of the state of static constants have a global impact, {@link Any}-Elements
 * are immutable. To get your own copy of such element, you can use {@link #changeable()}. The returned
 * instance is changeable described at {@link ChangeableElement}.
 *
 * @author Jens Pfahl
 */
public final class Any extends SystemElement {
	

	
	/**
	 * Matches any character. This may or may not also match line terminators. This depends on the
	 * {@link Flags} of the {@link RegexBuilder} or the ANY-Element.
	 */
	public static Any ANY_CHAR = new Any(".");
	
	/**
	 * Matches any characters like {@link #ANY_CHAR}, zero, one or more times.
	 */
	public static Any ANY = ANY_CHAR.arbitrary();

	


	private Any(String s) {
		super(s);
	}
	
}