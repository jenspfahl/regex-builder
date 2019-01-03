package de.jepfa.regex.elements;

import de.jepfa.regex.components.Element;

/**
 * All elements in a {@link Choice} are combined with logical ORs. So this Choice-{@link Group} matches
 * if ANY of all elements matches.
 *
 * @author Jens Pfahl
 */
public class Choice extends Group {

	/**
	 * Create a new {@link Choice} with a set of {@link Element Elements}.
	 * You can use {@link #add(Element...)} instead.
	 *
	 * @param elements, not <code>null</code>
	 */
	public Choice(Element... elements) {
		super(elements);
	}
	

	@Override
	protected String getOperator() {
		return "|";
	}

}