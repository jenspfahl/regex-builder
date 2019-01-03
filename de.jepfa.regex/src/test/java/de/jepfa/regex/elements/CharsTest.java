package de.jepfa.regex.elements;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.jepfa.regex.RegexBuilderException;
import de.jepfa.regex.components.CharClass;
import de.jepfa.regex.components.Element;



public class CharsTest {
	
	private Chars chars;
	
	@Before
	public final void setup() {
		chars = new Chars("");
	}

	
	@Test
	public final void testClone() throws Exception {
		
		Element clonedElement = chars.clone();
		
		Assert.assertNotSame(chars, clonedElement);
		Assert.assertEquals("", chars.toRegex());
		Assert.assertEquals("", clonedElement.toRegex());
	}
	
	@Test(expected=RegexBuilderException.class)
	public final void testNull() throws Exception {
		new Chars((CharClass)null);
	}
	
	@Test
	public final void testAdd_Char() throws Exception {
		Chars addedChars = chars.add('x');
		
		Assert.assertNotSame(chars, addedChars);
		Assert.assertEquals("", chars.toRegex());
		
		Assert.assertEquals("[x]", addedChars.toRegex());
	}
	
	@Test
	public final void testAdd_Char_Quoted() throws Exception {
		Chars addedChars = chars.add('[');
		
		Assert.assertNotSame(chars, addedChars);
		Assert.assertEquals("", chars.toRegex());
		
		Assert.assertEquals("[\\[]", addedChars.toRegex());
	}
	
	@Test
	public final void testAdd_String() throws Exception {
		Chars addedChars = chars.add("abc");
		
		Assert.assertNotSame(chars, addedChars);
		Assert.assertEquals("", chars.toRegex());
		
		Assert.assertEquals("[abc]", addedChars.toRegex());
	}
	
	@Test
	public final void testAdd_String_Quoted() throws Exception {
		Chars addedChars = chars.add("a-c");
		
		Assert.assertNotSame(chars, addedChars);
		Assert.assertEquals("", chars.toRegex());
		
		Assert.assertEquals("[a\\-c]", addedChars.toRegex());
	}
	
	@Test
	public final void testAdd_CharClass() throws Exception {
		Chars addedChars = chars.add(Char.LETTER.toCharClass());
		
		Assert.assertNotSame(chars, addedChars);
		Assert.assertEquals("", chars.toRegex());
		
		Assert.assertEquals("[\\p{L}]", addedChars.toRegex());
	}
	
	
	@Test
	public final void testRange() throws Exception {
		Chars addedChars = chars.range('d', 'y');
		
		Assert.assertNotSame(chars, addedChars);
		Assert.assertEquals("", chars.toRegex());
		
		Assert.assertEquals("[d-y]", addedChars.toRegex());
	}
	
	@Test
	public final void testRange_Quoted() throws Exception {
		Chars addedChars = chars.range('(', ')');
		
		Assert.assertNotSame(chars, addedChars);
		Assert.assertEquals("", chars.toRegex());
		
		Assert.assertEquals("[\\(-\\)]", addedChars.toRegex());
	}
	
	
	@Test
	public final void testNot() throws Exception {
		chars = new Chars('x');
		Chars addedChars = chars.not();
		
		Assert.assertNotSame(chars, addedChars);
		Assert.assertEquals("[x]", chars.toRegex());
		
		Assert.assertEquals("[^x]", addedChars.toRegex());
	}
	
	
	@Test
	public final void testSubtract() throws Exception {
		Chars addedChars = chars.range('a','z').subtract(new Chars('y'));
		
		Assert.assertNotSame(chars, addedChars);
		Assert.assertEquals("", chars.toRegex());
		
		Assert.assertEquals("[a-z&&[^y]]", addedChars.toRegex());
	}
	
	@Test
	public final void testSubtract_WithChar() throws Exception {
		Chars addedChars = chars.range('a','z').subtract('y');
		
		Assert.assertNotSame(chars, addedChars);
		Assert.assertEquals("", chars.toRegex());
		
		Assert.assertEquals("[a-z&&[^y]]", addedChars.toRegex());
	}
	
	@Test
	public final void testSubtract_WithString() throws Exception {
		Chars addedChars = chars.range('a','z').subtract("mno");
		
		Assert.assertNotSame(chars, addedChars);
		Assert.assertEquals("", chars.toRegex());
		
		Assert.assertEquals("[a-z&&[^mno]]", addedChars.toRegex());
	}
	
	@Test
	public final void testSubtract_WithRange() throws Exception {
		Chars addedChars = chars.range('a','z').subtract('m', 'q');
		
		Assert.assertNotSame(chars, addedChars);
		Assert.assertEquals("", chars.toRegex());
		
		Assert.assertEquals("[a-z&&[^m-q]]", addedChars.toRegex());
	}
	
	
	
	@Test
	public final void testIntersect() throws Exception {
		Chars addedChars = chars.range('a','z').intersect(new Chars('y'));
		
		Assert.assertNotSame(chars, addedChars);
		Assert.assertEquals("", chars.toRegex());
		
		Assert.assertEquals("[a-z&&[y]]", addedChars.toRegex());
	}
	
	@Test
	public final void testIntersect_WithChar() throws Exception {
		Chars addedChars = chars.range('a','z').intersect('y');
		
		Assert.assertNotSame(chars, addedChars);
		Assert.assertEquals("", chars.toRegex());
		
		Assert.assertEquals("[a-z&&[y]]", addedChars.toRegex());
	}
	
	@Test
	public final void testIntersect_WithString() throws Exception {
		Chars addedChars = chars.range('a','z').intersect("mno");
		
		Assert.assertNotSame(chars, addedChars);
		Assert.assertEquals("", chars.toRegex());
		
		Assert.assertEquals("[a-z&&[mno]]", addedChars.toRegex());
	}
	
	@Test
	public final void testIntersect_WithRange() throws Exception {
		Chars addedChars = chars.range('a','z').intersect('m', 'q');
		
		Assert.assertNotSame(chars, addedChars);
		Assert.assertEquals("", chars.toRegex());
		
		Assert.assertEquals("[a-z&&[m-q]]", addedChars.toRegex());
	}
	
	
	@Test
	public final void testUnion() throws Exception {
		Chars addedChars = chars.range('a','f').union(new Chars('e', 'z').not());
		
		Assert.assertNotSame(chars, addedChars);
		Assert.assertEquals("", chars.toRegex());
		
		Assert.assertEquals("[a-f[^e-z]]", addedChars.toRegex());
	}
	
	@Test
	public final void testUnion_WithChar() throws Exception {
		Chars addedChars = chars.range('a','f').union('e');
		
		Assert.assertNotSame(chars, addedChars);
		Assert.assertEquals("", chars.toRegex());
		
		Assert.assertEquals("[a-f[e]]", addedChars.toRegex());
	}
	
	@Test
	public final void testUnion_WithString() throws Exception {
		Chars addedChars = chars.range('a','f').union("efg");
		
		Assert.assertNotSame(chars, addedChars);
		Assert.assertEquals("", chars.toRegex());
		
		Assert.assertEquals("[a-f[efg]]", addedChars.toRegex());
	}
	@Test
	public final void testUnion_WithRange() throws Exception {
		Chars addedChars = chars.range('a','f').union('e', 'z');
		
		Assert.assertNotSame(chars, addedChars);
		Assert.assertEquals("", chars.toRegex());
		
		Assert.assertEquals("[a-f[e-z]]", addedChars.toRegex());
	}

}
