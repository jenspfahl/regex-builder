package de.jepfa.regex.constructs;

import java.util.List;

import de.jepfa.regex.components.ChangeableElement;
import de.jepfa.regex.components.Construct;
import de.jepfa.regex.components.Element;
import de.jepfa.regex.components.SystemElement;
import de.jepfa.regex.elements.Any;
import de.jepfa.regex.elements.Boundary;
import de.jepfa.regex.elements.Group;
import de.jepfa.regex.elements.Lookbehind;

/**
 * This is a special {@link Construct} that matches, if the given {@link Element}s occurred at the end of a line. 
 * The line is enclosed by {@link SystemElement#LINE_START} and {@link SystemElement#LINE_END}. 
 *
 * @author Jens Pfahl
 */
public class LineEndsWith extends Construct {
	
	protected boolean not;

	
	/**
	 * Create a new {@link LineEndsWith} with a set of {@link Element Elements}.
	 * You can use {@link #add(Element...)} instead.
	 *
	 * @param elements, not <code>null</code>
	 */
	public LineEndsWith(Element... elements) {
		super(elements);
	}

	
	/**
	 * Defines the whole Construct as <i>Negative</i>.
	 * That means, this contruct matches if the given {@link Element}s didn't occur at the end of a line. 
	 *
	 * @return a changed clone, see {@link ChangeableElement}
	 */
	public <T extends LineEndsWith> T not() {
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
		list.add(Boundary.LINE_START);
		
		if (not) {
			list.add(Any.ANY);
			list.add(new Lookbehind(containment).not());
			
		}
		else {
			list.add(Any.ANY);
			list.add(new Group(containment));
		}
		
		
		list.add(Boundary.LINE_END);
	}


}