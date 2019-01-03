package de.jepfa.regex.constructs;

import java.util.List;

import de.jepfa.regex.components.Element;
import de.jepfa.regex.elements.StringElement;
import de.jepfa.regex.elements.Strings;
import de.jepfa.regex.helper.Checker;

/**
 * This element helps you to define whole words, that could matches. All words are combined with logical ORs. 
 * So this element matches if ANY of all words matches.
 *
 * @author Jens Pfahl
 */
public class Words extends Word {


	public Words(Element... elem) {
		super(elem);
	}

	public Words(String... string) {
		Checker.checkNotNull(string);
		for (String s : string) {
			addInternal(new StringElement(s));
		}
	}
	
	public <T extends Words> T add(String... strings) {
		return cloneAndCall(e->{
			for (String s : strings) {
				e.elems.add(new StringElement(s));
			}	
		});
	}

	@Override
	protected void fillConstruct(List<Element> list, Element... content) {
		addBoundary(list);
		list.add(new Strings(content));
		addBoundary(list);
	}

	
}