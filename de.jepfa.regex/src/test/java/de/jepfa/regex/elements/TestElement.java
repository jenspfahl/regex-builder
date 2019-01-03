package de.jepfa.regex.elements;

import de.jepfa.regex.components.ChangeableElement;

public class TestElement extends ChangeableElement {


	private String content = "nop";

	public TestElement() {
		
	}

	public TestElement(String content) {
		this.content = content;
	}

	@Override
	protected String elementToRegex() {
		return content;
	}
	
}