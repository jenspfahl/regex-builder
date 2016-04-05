package de.jepfa.regex.elements;


/**
 * This element helps you to define whole words, that could matches. All words are combined with logical ORs. 
 * So this element matches if ANY of all words matches.
 *
 * @author Jens Pfahl
 */
public class Words extends Strings {

	protected boolean not;


	public Words(String... strings) {
		super(strings);
	}

	public <T extends Words> T not() {
		return cloneAndCall(e -> e.not = true);
	}
	

	public boolean isNot() {
		return not;
	}

	@Override
	protected String getBegin() {
		return super.getBegin() + Word.getBoundary(not);
	}
	
	@Override
	protected String getEnd() {
		return Word.getBoundary(not) + super.getEnd();
	};
	
}