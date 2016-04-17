package de.jepfa.regex.constructs;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.jepfa.regex.components.Element;
import de.jepfa.regex.elements.Chars;



public class WordTest {
	
	private Word word;
	
	@Before
	public final void setup() {
		word = new Word();
	}

	
	@Test
	public final void testClone() throws Exception {
		
		Element clonedElement = word.clone();
		
		Assert.assertNotSame(word, clonedElement);
		Assert.assertEquals("(\\b()\\b)", word.toRegex());
		Assert.assertEquals("(\\b()\\b)", clonedElement.toRegex());
	}
	
	@Test
	public final void testWord() throws Exception {
		Word addedWords = new Word("word").add(new Chars("sz"));
		
		
		Assert.assertNotSame(word, addedWords);
		Assert.assertEquals(0, word.getElements().size());
		Assert.assertEquals("(\\b()\\b)", word.toRegex());
		
		Assert.assertEquals(2, addedWords.getElements().size());
		Assert.assertEquals("(\\b((\\Qword\\E)[sz])\\b)", addedWords.toRegex());
	}
	
	@Test
	public final void testWord_WithNot() throws Exception {
		Word addedWords = new Word("word").not().add(new Chars("sz"));
		
		
		Assert.assertNotSame(word, addedWords);
		Assert.assertEquals(0, word.getElements().size());
		Assert.assertEquals("(\\b()\\b)", word.toRegex());
		
		Assert.assertEquals(2, addedWords.getElements().size());
		Assert.assertEquals("(\\B((\\Qword\\E)[sz])\\B)", addedWords.toRegex());
	}
	
	
	

}
