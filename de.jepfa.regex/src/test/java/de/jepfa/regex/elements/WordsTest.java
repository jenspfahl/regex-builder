package de.jepfa.regex.elements;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.jepfa.regex.components.Element;



public class WordsTest {
	
	private Words words;
	
	@Before
	public final void setup() {
		words = new Words("nop");
	}

	
	@Test
	public final void testClone() throws Exception {
		
		Element clonedElement = words.clone();
		
		Assert.assertNotSame(words, clonedElement);
		Assert.assertEquals("(\\b\\Qnop\\E\\b)", words.toRegex());
		Assert.assertEquals("(\\b\\Qnop\\E\\b)", clonedElement.toRegex());
	}
	
	@Test
	public final void testWords() throws Exception {
		Words addedWords = words.add("one", "two");
		
		
		Assert.assertNotSame(words, addedWords);
		Assert.assertEquals(1, words.getElements().size());
		Assert.assertEquals("(\\b\\Qnop\\E\\b)", words.toRegex());
		
		Assert.assertEquals(3, addedWords.getElements().size());
		Assert.assertEquals("(\\b\\Qnop\\E|\\Qone\\E|\\Qtwo\\E\\b)", addedWords.toRegex());
	}
	
	@Test
	public final void testWords_WithNot() throws Exception {
		Words addedWords = words.not().add("one", "two");
		
		
		Assert.assertNotSame(words, addedWords);
		Assert.assertEquals(1, words.getElements().size());
		Assert.assertEquals("(\\b\\Qnop\\E\\b)", words.toRegex());
		
		Assert.assertEquals(3, addedWords.getElements().size());
		Assert.assertEquals("(\\B\\Qnop\\E|\\Qone\\E|\\Qtwo\\E\\B)", addedWords.toRegex());
	}

	

}
