package de.jepfa.regex.elements;

import de.jepfa.regex.components.ChangeableElement;
import de.jepfa.regex.components.SystemElement;


/**
 * This element contains all predefined boundary matchers as static constants.
 * <p>
 * Because changes of the state of static constants have a global impact, {@link Boundary}-Elements
 * are immutable. To get your own copy of such element, you can use {@link #changeable()}. The returned
 * instance is changeable described at {@link ChangeableElement}.
 *
 * @author Jens Pfahl
 */
public final class Boundary extends SystemElement {
	
	/**
	 * Matches the start of a line. This is not a character, but you can use this element 
	 * followed by another element to indicate that the other element begins at the line start.
	 */
	public static Boundary LINE_START = new Boundary("^");
	
	/**
	 * Matches the end of a line. This is not a character, but you can use this element 
	 * prepend by another element to indicate that the prepend element ends at the line end.
	 */
	public static Boundary LINE_END = new Boundary("$");
	
	/**
	 * Matches the boundary of a word.
	 */
	public static Boundary WORD = new Boundary("\\b");
	
	/**
	 * Matches the boundary of a non-word.
	 */
	public static Boundary NOT_A_WORD = new Boundary("\\B");
	
	/**
	 * Matches the start of the input.
	 */
	public static Boundary INPUT_START = new Boundary("\\A");
	
	/**
	 * Matches the end of the input.
	 */
	public static Boundary INPUT_END = new Boundary("\\Z");
	



	private Boundary(String s) {
		super(s);
	}


}