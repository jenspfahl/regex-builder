package de.jepfa.regex.elements;

import de.jepfa.regex.components.ChangeableElement;
import de.jepfa.regex.components.Element;

/**
 * With this special {@link NonCapturing} you can <i>look ahead</i> to the next
 * token in your analyzing text. If the next token matches with this element, the result of the prior element
 * will be processed.
 * <p>
 * You can use this element in combination with other elements that delivers a result to describe
 * special logic. For example you look for a String followed by a colon but you don't want the colon itself,
 * you can use a {@link StringElement} followed by this {@link Lookahead} described the colon.
 *
 * @author Jens Pfahl
 */
public class Lookahead extends NonCapturing {

	protected boolean not;

	
	/**
	 * Create a new {@link Lookahead} group with a set of {@link Element Elements}.
	 * You can use {@link #add(Element...)} instead.
	 *
	 * @param elements, not <code>null</code>
	 */
	public Lookahead(Element... elements) {
		super(elements);
	}

	
	/**
	 * Defines the whole Lookahead as <i>Negative</i> Lookahead.
	 * That means, if the next token DON'T matches with this element, the result of the prior element
	 * will be processed.
	 *
	 * @return a changed clone, see {@link ChangeableElement}
	 */
	public <T extends Lookahead> T not() {
		return cloneAndCall(e -> e.not = true);
	}
	
	
	/**
	 * @return <code>true</code> if this Lookahead is a <i>Negative</i> Lookahead.
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
			return "?!";
		}
		return "?=";
	}
}