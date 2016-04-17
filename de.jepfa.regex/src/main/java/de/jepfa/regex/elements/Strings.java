package de.jepfa.regex.elements;

import de.jepfa.regex.components.Element;

/**
 * This class defines many {@link StringElement}s. All elements in this class are combined with logical ORs. 
 * So this element matches if ANY of all elements matches.
 *
 * @author Jens Pfahl
 */
public class Strings extends Choice {
	
	public Strings(String... strings) {
		for (String s : strings) {
			elems.add(new StringElement(s));
		}
	}
	
	public Strings(Element... elem) {
		super(elem);
	}
	
	
	public <T extends Strings> T add(String... strings) {
		return cloneAndCall(e->{
			for (String s : strings) {
				e.elems.add(new StringElement(s));
			}	
		});
	}
	
}