package de.jepfa.regex.elements;

import de.jepfa.regex.components.ChangeableElement;
import de.jepfa.regex.components.Element;

/**
 * With this special {@link NonCapturing} you can <i>look behind</i> to the previous
 * token in your analyzing text. If the previous token matches with this element, the result of the next element
 * will be processed.
 * <p>
 * You can use this element in combination with other elements that delivers a result to describe
 * special logic. For example you look for a String after an equals sign but you don't want the equals sign itself,
 * you can use this {@link Lookbehind} described the equals sign followed by a {@link StringElement}.
 *
 * @author Jens Pfahl
 */
public class Lookbehind extends NonCapturing {

	protected boolean not;

	
	/**
	 * Create a new {@link Lookbehind} group with a set of {@link Element Elements}.
	 * You can use {@link #add(Element...)} instead.
	 *
	 * @param elements, not <code>null</code>
	 */
	public Lookbehind(Element... elements) {
		super(elements);
	}

	/**
	 * Defines the whole Lookbehind as <i>Negative</i> Lookbehind.
	 * That means, if the previous token DON'T matches with this element, the result of the next element
	 * will be processed.
	 *
	 * @return a changed clone, see {@link ChangeableElement}
	 */
	public <T extends Lookbehind> T not() {
		return cloneAndCall(e -> e.not = true);
	}
	
	/**
	 * @return <code>true</code> if this Lookbehind is a <i>Negative</i> Lookbehind.
	 * 
	 * @see #not()
	 */
	public boolean isNot() {
		return not;
	}
	
	
	/**
	 * This element is not indexable!
	 */
	@Override
	public boolean isIndexable() {
		return false;
	}
	

	@Override
	protected String getPrefix() {
		if (not) {
			return "?<!";
		}
		return "?<=";
	}
}