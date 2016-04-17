package de.jepfa.regex.components;

import de.jepfa.regex.elements.PlainElement;


/**
 * This element contains all predefined Regular Expession classes as static constants.
 * <p>
 * Because changes of the state of static constants have a global impact, {@link SystemElement}-Elements
 * are immutable. To get your own copy of such element, you can use {@link #changeable()}. The returned
 * instance is changeable described at {@link ChangeableElement}.
 *
 * @author Jens Pfahl
 */
public abstract class SystemElement extends Element {
	


	protected String s;


	protected SystemElement(String s) {
		this.s = s;
	}
	
	/**
	 * Returns a changeable instance of the current static singleton.
	 *
	 * @return not <code>null</code>
	 */
	@SuppressWarnings("unchecked")
	public <T extends ChangeableElement > T changeable() {
		return (T) new PlainElement(s);
	}
	
	@Override
	protected String elementToRegex() {
		return s.toString();
	}
}