package de.jepfa.regex.constructs;

import java.util.List;

import de.jepfa.regex.components.Construct;
import de.jepfa.regex.components.Element;
import de.jepfa.regex.elements.Boundary;
import de.jepfa.regex.elements.Group;
import de.jepfa.regex.elements.Strings;

/**
 * This element helps you to define a word, that should match. You can also define {@link Element}s, that
 * describe the word. 
 * This element matches if the whole word matches.
 *
 * @author Jens Pfahl
 */
public class Word extends Construct {

	protected boolean not;


	public Word(Element... elem) {
		super(elem);
	}
	
	public Word(String string) {
		super(new Strings(string));
	}

	public <T extends Word> T not() {
		return cloneAndCall(e -> e.not = true);
	}
	

	public boolean isNot() {
		return not;
	}
	
	@Override
	protected void fillConstruct(List<Element> list, Element... content) {
		addBoundary(list);
		list.add(new Group(content));
		addBoundary(list);
	}

	protected void addBoundary(List<Element> list) {
		if (not) {
			list.add(Boundary.NOT_A_WORD);
		} else {
			list.add(Boundary.WORD);
		}
	}
	


}