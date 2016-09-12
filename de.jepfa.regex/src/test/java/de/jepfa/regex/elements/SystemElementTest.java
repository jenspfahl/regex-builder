package de.jepfa.regex.elements;

import org.junit.Assert;
import org.junit.Test;

import de.jepfa.regex.components.ChangeableElement;
import de.jepfa.regex.components.Element;



public class SystemElementTest {
	
	

	@Test
	public final void testClone() throws Exception {
		
		Element clonedElement = Any.ANY_CHAR.clone();
		
		Assert.assertNotSame(Any.ANY_CHAR, clonedElement);
		Assert.assertEquals(".", Any.ANY_CHAR.toRegex());
		Assert.assertEquals(".", clonedElement.toRegex());
	}
	
	@Test
	public final void testChangeable_WithoutActiveChangeMode() throws Exception {
		
		ChangeableElement changeableElement = Any.ANY_CHAR.changeable();
		//changeableElement.setChangeable();
		Element optionalElement = changeableElement.optional();
		Assert.assertNotSame(changeableElement, Any.ANY_CHAR);
		Assert.assertNotSame(changeableElement, optionalElement);
		Assert.assertEquals(".", Any.ANY_CHAR.toRegex());
		Assert.assertEquals(".", changeableElement.toRegex());
		Assert.assertEquals(".?", optionalElement.toRegex());
	}
	
	@Test
	public final void testChangeable_WithActiveChangeMode() throws Exception {
		
		ChangeableElement changeableElement = Any.ANY_CHAR.changeable();
		changeableElement.setChangeable();
		Element optionalElement = changeableElement.optional();
		Assert.assertNotSame(changeableElement, Any.ANY_CHAR);
		Assert.assertSame(changeableElement, optionalElement);
		Assert.assertEquals(".", Any.ANY_CHAR.toRegex());
		Assert.assertEquals(".?", changeableElement.toRegex());
		Assert.assertEquals(".?", optionalElement.toRegex());
	}
	

}
