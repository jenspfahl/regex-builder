package de.jepfa.regex.elements;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.jepfa.regex.components.Element;



public class PlainElementTest {
	
	private PlainElement plainRegex;
	
	@Before
	public final void setup() {
		plainRegex = new PlainElement("");
	}

	
	@Test
	public final void testClone() throws Exception {
		
		Element clonedElement = plainRegex.clone();
		
		Assert.assertNotSame(plainRegex, clonedElement);
		Assert.assertEquals("", plainRegex.toRegex());
		Assert.assertEquals("", clonedElement.toRegex());
	}
	
	@Test
	public final void test() throws Exception {
		plainRegex = new PlainElement("[1-9]");
				
		Assert.assertEquals("[1-9]", plainRegex.toRegex());

	}
	
	

}
