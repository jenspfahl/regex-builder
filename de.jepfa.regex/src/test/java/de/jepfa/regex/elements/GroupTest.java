package de.jepfa.regex.elements;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.jepfa.regex.RegexBuilderException;
import de.jepfa.regex.helper.Printer;



public class GroupTest {
	
	private Group group;
	
	@Before
	public final void setup() {
		group = new Group();
	}

	
	@Test
	public final void testClone() throws Exception {
		
		TestElement testElement = new TestElement();
		group = new Group(testElement);
		Group clonedGroup = group.clone();
		
		Assert.assertNotSame(group, clonedGroup);
		Assert.assertFalse(group.getElements().isEmpty());
		Assert.assertEquals(testElement, group.getElements().get(0));
		
		Assert.assertFalse(clonedGroup.getElements().isEmpty());
		Assert.assertEquals(testElement, clonedGroup.getElements().get(0));
	}
	
	@Test
	public final void testAdd() throws Exception {
		TestElement newElem1 = new TestElement("first");
		TestElement newElem2 = new TestElement("second");
		Group addedGroup = group.add(newElem1, newElem2);
		
		
		Assert.assertNotSame(group, addedGroup);
		Assert.assertEquals(0, group.getElements().size());
		Assert.assertEquals("()", group.toRegex());
		Assert.assertEquals(Integer.valueOf(0), group.getIndex());
		
		Assert.assertEquals(2, addedGroup.getElements().size());
		Assert.assertSame(newElem1, addedGroup.getElements().get(0));
		Assert.assertSame(newElem2, addedGroup.getElements().get(1));
		Assert.assertEquals("(firstsecond)", addedGroup.toRegex());
		Assert.assertEquals(Integer.valueOf(0), addedGroup.getIndex());
	}
	
	@Test
	public final void testAdd_WithContent() throws Exception {
		TestElement firstElem = new TestElement();
		TestElement secondElem = new TestElement();
		group = new Group(firstElem);
		Group addedGroup = group.add(secondElem);
		
		
		Assert.assertSame(firstElem, group.getElements().get(0));
		Assert.assertEquals(1, group.getElements().size());
		Assert.assertEquals("(nop)", group.toRegex());
		
		Assert.assertNotSame(group, addedGroup);
		Assert.assertEquals(2, addedGroup.getElements().size());
		Assert.assertSame(firstElem, addedGroup.getElements().get(0));
		Assert.assertSame(secondElem, addedGroup.getElements().get(1));
		Assert.assertEquals("(nopnop)", addedGroup.toRegex());
	}
	
	@Test(expected=RegexBuilderException.class)
	public final void testAdd_Cycle() {
		Group group1 = new Group();
		Group group2 = new Group();
		
		group1.setChangeable();
		group2.setChangeable();
		
		group1.add(group2);
		group2.add(group1);
		
	}
	
	@Test
	public final void testIsIndexable() {
		Assert.assertTrue(group.isIndexable());
	}
	
	@Test
	public final void testGetIndex_WithDifferentGroups() {
		
		
		Group group_1_2 = new Group(new TestElement("1_2"));
		Assert.assertEquals(Integer.valueOf(0), group_1_2.getIndex());
		
		Group group_2_1 = new Group(new TestElement("2_1"));
		Assert.assertEquals(Integer.valueOf(0), group_2_1.getIndex());
		
		Group group_3 = new Group(new TestElement("1_1"));
		Assert.assertEquals(Integer.valueOf(0), group_3.getIndex());
		
		Group group_1 = group_3.add(group_1_2);
		Assert.assertEquals(Integer.valueOf(0), group_3.getIndex());
		Assert.assertEquals(Integer.valueOf(0), group_1.getIndex());
		Assert.assertEquals(Integer.valueOf(1), group_1_2.getIndex());
		
		Group group_2 = new Group(group_2_1).optional();
		Assert.assertEquals(Integer.valueOf(0), group_2.getIndex());
		Assert.assertEquals(Integer.valueOf(1), group_2_1.getIndex());
		
		
		group = new Group(group_1, group_2).add(group_3);
		
		
		System.out.println(Printer.toString(group));
		
		Assert.assertEquals(Integer.valueOf(0), group.getIndex());
		Assert.assertEquals(Integer.valueOf(1), group_1.getIndex());
		Assert.assertEquals(Integer.valueOf(2), group_1_2.getIndex());
		Assert.assertEquals(Integer.valueOf(3), group_2.getIndex());
		Assert.assertEquals(Integer.valueOf(4), group_2_1.getIndex());
		Assert.assertEquals(Integer.valueOf(5), group_3.getIndex()); 
		
	}
	
	
	@Test
	public final void testGetIndex_WithOutChangeable() {
		Group group1 = new Group(new TestElement("1"));
		Group group2 = new Group(new TestElement("2"));
		
		group = new Group(group1, group2, group1.optional()); // here is no repetition! 
		
		
		System.out.println(Printer.toString(group));
		
		Assert.assertEquals(Integer.valueOf(0), group.getIndex());
		Assert.assertEquals(Integer.valueOf(1), group1.getIndex());
		Assert.assertEquals(Integer.valueOf(2), group2.getIndex());
		
	}
	
	@Test
	public final void testGetIndex_WithChangeable() {
		Group group1 = new Group(new TestElement("1"));
		Group group2 = new Group(new TestElement("2"));
		group1.setChangeable();
		group = new Group(group1, group2, group1.optional()); // here is no repetition caused by group1.setChangeable()! 
		
		
		System.out.println(Printer.toString(group));
		
		Assert.assertEquals(Integer.valueOf(0), group.getIndex());
		Assert.assertEquals(Integer.valueOf(3), group1.getIndex()); // repetition leads to last used index 
		Assert.assertEquals(Integer.valueOf(2), group2.getIndex());
		
	}

}
