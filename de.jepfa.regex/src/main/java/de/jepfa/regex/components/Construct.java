package de.jepfa.regex.components;

import java.util.LinkedList;
import java.util.List;

import de.jepfa.regex.elements.Group;

/**
 * Constructs are predefined {@link Group}s for certain use cases. 
 * A Construct contains of dynamicly added content ({@link Element}s) like a {@link Group}
 * and predefined {@link Element}s to handle that content.
 * <p>
 * You can create your own Constructs for your own frequently used use cases.
 *
 * @author Jens Pfahl
 */
public abstract class Construct extends Group {

	/**
	 * Create a new {@link Construct} with a set of {@link Element Elements}.
	 * You can use {@link #add(Element...)} instead.
	 *
	 * @param elements, not <code>null</code>
	 */
	public Construct(Element... elements) {
		super(elements);
	}

	
	@Override
	protected List<Element> getElemsForRegex() {
		List<Element> list = new LinkedList<>();
		
		List<Element> elemsForRegex = super.getElemsForRegex();
		Element[] content = elemsForRegex.toArray(new Element[elemsForRegex.size()]);
		
		fillConstruct(list, content);
				
		return list;
	}


	/**
	 * Overwrite this method to fill the given <code>list</code> with all {@link Element}s
	 * you need for your Construct. To make Constructs dynamic, use the given <code>content</code>
	 * for your construction. 
	 * 
	 * @param list the list to fill, initially empty and not <code>null</code>
	 * @param content all {@link Element}s the user of your {@link Construct} adds to, not <code>null</code>
	 */
	abstract protected void fillConstruct(List<Element> list, Element ...content);


}