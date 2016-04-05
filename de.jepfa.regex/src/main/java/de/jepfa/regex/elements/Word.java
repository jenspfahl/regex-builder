package de.jepfa.regex.elements;

import de.jepfa.regex.components.Element;

/**
 * This element helps you to define a word, that should match. You can also define {@link Element}s, that
 * describe the word. 
 * This element matches if the whole word matches.
 *
 * @author Jens Pfahl
 */
public class Word extends Group {

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
	protected String getBegin() {
		return super.getBegin() + getBoundary(not);
	}
	
	@Override
	protected String getEnd() {
		return getBoundary(not) + super.getEnd();
	};
	
	
	
	static String getBoundary(boolean not) {
		if (not) {
			return "\\B";
		}
		return "\\b";
	}
}