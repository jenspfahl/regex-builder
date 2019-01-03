package de.jepfa.regex.elements;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.jepfa.regex.components.Element;



public class StringsTest {
	
	private Strings strings;
	
	@Before
	public final void setup() {
		strings = new Strings("nop");
	}

	
	@Test
	public final void testClone() throws Exception {
		
		Element clonedElement = strings.clone();
		
		Assert.assertNotSame(strings, clonedElement);
		Assert.assertEquals("(\\Qnop\\E)", strings.toRegex());
		Assert.assertEquals("(\\Qnop\\E)", clonedElement.toRegex());
	}
	
	@Test
	public final void testStrings() throws Exception {
		TestElement newElem = new TestElement("\\t");
		StringElement newElemAsStringElement = new StringElement(newElem.toRegex());
		Strings addedStrings = strings.add("\\t");
		
		
		Assert.assertNotSame(strings, addedStrings);
		Assert.assertEquals(1, strings.getElements().size());
		Assert.assertEquals("(\\Qnop\\E)", strings.toRegex());
		
		Assert.assertEquals(2, addedStrings.getElements().size());
		Assert.assertEquals("\\Qnop\\E", addedStrings.getElements().get(0).toRegex());
		Assert.assertEquals(newElemAsStringElement.toRegex(), addedStrings.getElements().get(1).toRegex());
		Assert.assertNotSame(newElemAsStringElement, addedStrings.getElements().get(1));
		Assert.assertEquals("(\\Qnop\\E|\\Q\\t\\E)", addedStrings.toRegex());
	}
	
	@Test
	public final void testStrings_MoreThanOne() throws Exception {
		String firstElem = "First";
		String secondElem = "Second";
		strings = new Strings(firstElem);
		Strings addedStrings = strings.add(secondElem);
		
		
		Assert.assertEquals(1, strings.getElements().size());
		Assert.assertEquals("\\QFirst\\E", strings.getElements().get(0).toRegex());
		Assert.assertEquals("(\\QFirst\\E)", strings.toRegex());
		
		Assert.assertEquals(2, addedStrings.getElements().size());
		Assert.assertEquals("\\QFirst\\E", addedStrings.getElements().get(0).toRegex());
		Assert.assertEquals("\\QSecond\\E", addedStrings.getElements().get(1).toRegex());
		Assert.assertEquals("(\\QFirst\\E|\\QSecond\\E)", addedStrings.toRegex());
	}
	
	

}
