package de.jepfa.regex.elements;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.jepfa.regex.components.Element;



public class StringElementTest {
	
	private StringElement stringElement;
	
	@Before
	public final void setup() {
		stringElement = new StringElement("nop");
	}

	
	@Test
	public final void testClone() throws Exception {
		
		Element clonedElement = stringElement.clone();
		
		Assert.assertNotSame(stringElement, clonedElement);
		Assert.assertEquals("\\Qnop\\E", stringElement.toRegex());
		Assert.assertEquals("\\Qnop\\E", clonedElement.toRegex());
	}
	

}
