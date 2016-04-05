package de.jepfa.regex.elements;

import de.jepfa.regex.RegexBuilder;
import de.jepfa.regex.components.ChangeableElement;
import de.jepfa.regex.components.Element;
import de.jepfa.regex.components.Flags;


/**
 * This element contains all predefined Regular Expession classes as static constants.
 * <p>
 * Because changes of the state of static constants have a global impact, {@link SystemElement}-Elements
 * are immutable. To get your own copy of such element, you can use {@link #changeable()}. The returned
 * instance is changeable described at {@link ChangeableElement}.
 *
 * @author Jens Pfahl
 */
public final class SystemElement extends Element {
	
	/**
	 * Matches any character. This may or may not also match line terminators. This depends on the
	 * {@link Flags} of the {@link RegexBuilder} or the ANY-Element.
	 */
	public static SystemElement ANY_CHAR = new SystemElement(".");
	
	/**
	 * Matches any character like {@link #ANY_CHAR}, zero, one or more times.
	 */
	public static SystemElement ANY = ANY_CHAR.arbitrary();

	
	/**
	 * Matches a digit. Digits are the characters 0 till 9.
	 */
	public static SystemElement DIGIT = new SystemElement("\\d");
	
	/**
	 * Matches ALL EXCEPT digits. 
	 * @see #DIGIT
	 */
	public static SystemElement NOT_A_DIGIT = new SystemElement("\\D");
	
	/**
	 * Matches a number. Numbers consists of 0 till 9 characters.
	 */
	public static SystemElement NUMBERS = DIGIT.many();
	

	/**
	 * Matches word characters. Word characters are {@link #ALPHA_OR_NUMBER_CHAR}s. 
	 */
	public static SystemElement WORD_CHAR = new SystemElement("\\w");
	
	/**
	 * Matches ALL EXCEPT word characters. 
	 * @see #WORD_CHAR
	 */
	public static SystemElement NOT_A_WORD_CHAR = new SystemElement("\\W");
	
	/**
	 * Matches words. A word consists of {@link #WORD_CHAR}s. 
	 */
	public static SystemElement WORD = WORD_CHAR.many();

	
	/**
	 * Matches all space character. Space characters are whitespaces and tabulators. 
	 */
	public static SystemElement SPACE_CHAR = new SystemElement("\\s");
	
	/**
	 * Matches ALL EXCEPT space characters. 
	 * @see #SPACE_CHAR
	 */
	public static SystemElement NOT_A_SPACE_CHAR = new SystemElement("\\S");

	/**
	 * Matches all space characters. Space characters are whitespaces and tabulators. 
	 */
	public static SystemElement SPACE = SPACE_CHAR.many();
	
	
	
	/**
	 * Matches the start of a line. This is not a character, but you can use this element 
	 * followed by another element to indicate that the other element begins at the line start.
	 */
	public static SystemElement LINE_START = new SystemElement("^");
	
	/**
	 * Matches the end of a line. This is not a character, but you can use this element 
	 * prepend by another element to indicate that the prepend element ends at the line end.
	 */
	public static SystemElement LINE_END = new SystemElement("$");
	
	public static SystemElement STRING_START = new SystemElement("\\A");
	public static SystemElement STRING_END = new SystemElement("\\Z");

	public static SystemElement TAB = new SystemElement("\\t");
	public static SystemElement NEW_LINE = new SystemElement("\\n");
	public static SystemElement CARRIAGE_RETURN = new SystemElement("\\r");

	public static SystemElement LOWER_CHAR = new SystemElement("\\p{Lower}");
	public static SystemElement UPPER_CHAR = new SystemElement("\\p{Upper}");
	public static SystemElement ASCII_CHAR = new SystemElement("\\p{ASCII}");
	public static SystemElement ALPHA_CHAR = new SystemElement("\\p{Alpha}");
	public static SystemElement ALPHA_OR_NUMBER_CHAR = new SystemElement("\\p{Alnum}");
	public static SystemElement PUNCT = new SystemElement("\\p{Punct}");
	public static SystemElement GRAPH = new SystemElement("\\p{Graph}");
	public static SystemElement PRINT = new SystemElement("\\p{Print}");
	public static SystemElement BLANK = new SystemElement("\\p{Blank}");
	public static SystemElement CONTROL = new SystemElement("\\p{Cntrl}");
	public static SystemElement HEX_DIGIT = new SystemElement("\\p{XDigit}");

	private String s;


	private SystemElement(String s) {
		this.s = s;
	}
	
	/**
	 * Returns a changeable instance of the current static singleton.
	 *
	 * @return not <code>null</code>
	 */
	public ChangeableElement changeable() {
		return new PlainElement(s);
	}
	
	@Override
	protected String elementToRegex() {
		return s.toString();
	}
}