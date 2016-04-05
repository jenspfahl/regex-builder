package de.jepfa.regex.constructs;

import java.util.List;

import de.jepfa.regex.components.ChangeableElement;
import de.jepfa.regex.components.Construct;
import de.jepfa.regex.components.Element;
import de.jepfa.regex.elements.Group;
import de.jepfa.regex.elements.Lookahead;
import de.jepfa.regex.elements.SystemElement;

/**
 * This is a special {@link Construct} that matches, if the given {@link Element}s occurred at the beginning of a line. 
 * The line is enclosed by {@link SystemElement#LINE_START} and {@link SystemElement#LINE_END}. 
 *
 * @author Jens Pfahl
 */
public class LineStartsWith extends Construct {
	
	protected boolean not;

	
	/**
	 * Create a new {@link LineStartsWith} with a set of {@link Element Elements}.
	 * You can use {@link #add(Element...)} instead.
	 *
	 * @param elements, not <code>null</code>
	 */
	public LineStartsWith(Element... elements) {
		super(elements);
	}


	/**
	 * Defines the whole Construct as <i>Negative</i>.
	 * That means, this contruct matches if the given {@link Element}s didn't occur at the beginning of a line. 
	 *
	 * @return a changed clone, see {@link ChangeableElement}
	 */
	public <T extends LineStartsWith> T not() {
		return cloneAndCall(e -> e.not = true);
	}
	
	
	/**
	 * @return <code>true</code> if this Construct is <i>Negative</i>.
	 * 
	 * @see #not()
	 */
	public boolean isNot() {
		return not;
	}
	
	
	@Override
	protected void fillConstruct(List<Element> list, Element... containment) {
		list.add(SystemElement.LINE_START);
		if (not) {
			list.add(new Lookahead(containment).not());
		}
		else {
			list.add(new Group(containment));
		}
		list.add(SystemElement.ANY);
		list.add(SystemElement.LINE_END);
	}


}