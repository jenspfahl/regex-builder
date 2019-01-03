package de.jepfa.regex.elements;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.jepfa.regex.components.Element;



public class ChoiceTest {
	
	private Choice choice;
	
	@Before
	public final void setup() {
		choice = new Choice();
	}

	
	@Test
	public final void testClone() throws Exception {
		
		Element clonedChoice = choice.clone();
		
		Assert.assertNotSame(choice, clonedChoice);
		Assert.assertEquals("()", choice.toRegex());
		Assert.assertEquals("()", clonedChoice.toRegex());
	}
	
	@Test
	public final void testAdd() throws Exception {
		TestElement newElem = new TestElement();
		Choice addedChoice = choice.add(newElem);
		
		
		Assert.assertNotSame(choice, addedChoice);
		Assert.assertEquals(0, choice.getElements().size());
		Assert.assertEquals("()", choice.toRegex());
		
		Assert.assertEquals(1, addedChoice.getElements().size());
		Assert.assertSame(newElem, addedChoice.getElements().get(0));
		Assert.assertEquals("(nop)", addedChoice.toRegex());
	}
	
	@Test
	public final void testAdd_WithContent() throws Exception {
		TestElement firstElem = new TestElement();
		TestElement secondElem = new TestElement();
		choice = new Choice(firstElem);
		Choice addedChoice = choice.add(secondElem);
		
		
		Assert.assertSame(firstElem, choice.getElements().get(0));
		Assert.assertEquals(1, choice.getElements().size());
		Assert.assertEquals("(nop)", choice.toRegex());
		
		Assert.assertNotSame(choice, addedChoice);
		Assert.assertEquals(2, addedChoice.getElements().size());
		Assert.assertSame(firstElem, addedChoice.getElements().get(0));
		Assert.assertSame(secondElem, addedChoice.getElements().get(1));
		Assert.assertEquals("(nop|nop)", addedChoice.toRegex());
	}
	
	

}
