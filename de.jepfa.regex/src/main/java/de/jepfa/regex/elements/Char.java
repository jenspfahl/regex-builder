package de.jepfa.regex.elements;

import de.jepfa.regex.RegexBuilder.Flag;
import de.jepfa.regex.components.ChangeableElement;
import de.jepfa.regex.components.CharClass;
import de.jepfa.regex.components.SystemElement;


/**
 * This element contains predefined character classes as static constants.
 * <p>
 * To use this classes in {@link Chars}, use {@link #toCharClass()}.
 * <p>
 * Because changes of the state of static constants have a global impact, {@link Char}-Elements
 * are immutable. To get your own copy of such element, you can use {@link #changeable()}. The returned
 * instance is changeable described at {@link ChangeableElement}.
 *
 * @author Jens Pfahl
 */
public final class Char extends SystemElement {
	
	private static class CharClassImpl implements CharClass {

		private String s;

		private CharClassImpl(String s) {
			this.s = s;
		}

		@Override
		public String toRegex() {
			return s;
		}
		
	}

	
	/**
	 * Matches a digit. Digits are the characters 0 till 9.
	 */
	public static Char DIGIT = new Char("\\d");
	
	/**
	 * Matches ALL EXCEPT digits. 
	 * @see #DIGIT
	 */
	public static Char NOT_A_DIGIT = new Char("\\D");
	
	/**
	 * Matches a number. Numbers consists of 0 till 9 characters.
	 */
	public static Char NUMBER = DIGIT.many();
	
	/**
	 * Matches a hexadecimal digit. Hexadecimal digits are the characters 0 till 9 an A till F with ignore case.
	 */
	public static Char HEX_DIGIT = new Char("\\p{XDigit}");
	
	/**
	 * Matches a hexadecimal number. Hexadecimal numbers consists of 0 till 9 an A till F with ignore case.
	 */
	public static Char HEX_NUMBERS = HEX_DIGIT.many();
	

	/**
	 * Matches word characters. Word characters are {@link #ALPHA_OR_NUMBER_CHAR}s. 
	 */
	public static Char WORD_CHAR = new Char("\\w");
	
	/**
	 * Matches ALL EXCEPT word characters. 
	 * @see #WORD_CHAR
	 */
	public static Char NOT_A_WORD_CHAR = new Char("\\W");
	
	/**
	 * Matches words. A word consists of {@link #WORD_CHAR}s. 
	 */
	public static Char WORD = WORD_CHAR.many();
	
	/**
	 * Matches all letters in all languages. For special language characters it is
	 * necessary to enable {@link Flag#UNICODE} or {@link Flag#UNICODE_CHARS}.
	 */
	public static Char LETTER = new Char("\\p{L}");
	
	/**
	 * Matches lowercase letters in all languages. For special language characters it is
	 * necessary to enable {@link Flag#UNICODE} or {@link Flag#UNICODE_CHARS}.
	 */
	public static Char LOWER_LETTER = new Char("\\p{Ll}");
	
	/**
	 * Matches uppercase letters in all languages. For special language characters it is
	 * necessary to enable {@link Flag#UNICODE} or {@link Flag#UNICODE_CHARS}.
	 */
	public static Char UPPER_LETTER = new Char("\\p{Lu}");
	
	/**
	 * Matches all currency symbols. 
	 */
	public static Char CURRENCY = new Char("\\p{Sc}");

	
	/**
	 * Matches all space character. Space characters are whitespaces and tabulators. 
	 */
	public static Char SPACE_CHAR = new Char("\\s");
	
	/**
	 * Matches ALL EXCEPT space characters. 
	 * @see #SPACE_CHAR
	 */
	public static Char NOT_A_SPACE_CHAR = new Char("\\S");

	/**
	 * Matches all space characters. Space characters are whitespaces and tabulators. 
	 */
	public static Char SPACE = SPACE_CHAR.many();
	
	
	
	

	public static Char TAB = new Char("\\t");
	public static Char NEW_LINE = new Char("\\n");
	public static Char CARRIAGE_RETURN = new Char("\\r");

	/**
	 * Matches lowercase characters of all ASCII chars.
	 */
	public static Char LOWER_CHAR = new Char("\\p{Lower}");
	
	/**
	 * Matches uppercase characters of all ASCII chars.
	 */
	public static Char UPPER_CHAR = new Char("\\p{Upper}");
	public static Char ASCII_CHAR = new Char("\\p{ASCII}");
	public static Char ALPHA_CHAR = new Char("\\p{Alpha}");
	public static Char ALPHA_OR_NUMBER_CHAR = new Char("\\p{Alnum}");
	
	public static Char PUNCT = new Char("\\p{Punct}");
	public static Char GRAPH = new Char("\\p{Graph}");
	public static Char PRINT = new Char("\\p{Print}");
	
	/**
	 * Matches all space character. Space characters are whitespaces and tabulators. 
	 */
	public static Char BLANK = new Char("\\p{Blank}");
	
	/**
	 * Matches all not printable control characters. 
	 */
	public static Char CONTROL = new Char("\\p{Cntrl}");
	



	private Char(String s) {
		super(s);
	}
	
	
	/**
	 * Returns a {@link CharClass}-instance for passing into {@link Chars}.
	 *
	 * @return not <code>null</code>
	 */
	public CharClass toCharClass() {
		return new CharClassImpl(s);
	}
	

}