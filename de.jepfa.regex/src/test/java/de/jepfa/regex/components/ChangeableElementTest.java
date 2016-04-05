package de.jepfa.regex.components;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.jepfa.regex.elements.TestElement;
import de.jepfa.regex.helper.Changer;



public class ChangeableElementTest {
	
	private ChangeableElement changeableElement;
	
	@Before
	public final void setup() {
		changeableElement = new TestElement();
	}

	
	@Test
	public final void testClone() throws Exception {
		
		Element clonedElement = changeableElement.clone();
		
		Assert.assertNotSame(changeableElement, clonedElement);
		Assert.assertEquals("nop", changeableElement.toRegex());
		Assert.assertEquals("nop", clonedElement.toRegex());
	}
	
	
	@Test
	public final void testChangeInstance_WithSetter() throws Exception {
		changeableElement.setChangeable();
		Element newElement = changeableElement.optional();
		changeableElement.unsetChangeable();
		
		Assert.assertSame(changeableElement, newElement);
		Assert.assertSame(changeableElement.getQuantifier(), newElement.getQuantifier());
		Assert.assertSame(changeableElement.getFlags(), newElement.getFlags());
		
		Assert.assertEquals(0, newElement.getQuantifier().getMin());
		Assert.assertEquals(1, newElement.getQuantifier().getMax());
		
		Assert.assertEquals(0, changeableElement.getQuantifier().getMin());
		Assert.assertEquals(1, changeableElement.getQuantifier().getMax());
	}
	
	@Test
	public final void testChangeInstance_WithChanger() throws Exception {
		Assert.assertEquals(1, changeableElement.getQuantifier().getMin());
		Assert.assertEquals(1, changeableElement.getQuantifier().getMax());
		
		Changer.change(changeableElement, e -> e.optional());
		
		Assert.assertEquals(0, changeableElement.getQuantifier().getMin());
		Assert.assertEquals(1, changeableElement.getQuantifier().getMax());
	}
	
	
	
}
