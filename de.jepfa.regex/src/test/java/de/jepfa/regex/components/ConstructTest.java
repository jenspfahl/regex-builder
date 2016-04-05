package de.jepfa.regex.components;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.jepfa.regex.elements.Group;
import de.jepfa.regex.elements.SystemElement;
import de.jepfa.regex.elements.TestElement;



public class ConstructTest {
	
	private Construct construct;
	private TestElement origin = new TestElement("origin");
	
	@Before
	public final void setup() {
		construct = new Construct(origin) {
			
			
			@Override
			protected void fillConstruct(List<Element> list, Element... content) {
				Assert.assertEquals(1, content.length);
				Assert.assertSame(origin, content[0]);
				Assert.assertNotNull(list);
				Assert.assertTrue(list.isEmpty());
				
				list.add(SystemElement.STRING_START);
				list.add(new Group(content));
				list.add(SystemElement.STRING_END);
			}
		};
	}

	
	@Test
	public final void testClone() throws Exception {
		
		Element clonedElement = construct.clone();
		
		Assert.assertNotSame(construct, clonedElement);
		Assert.assertEquals(construct.toRegex(), clonedElement.toRegex());
	}
	
	@Test
	public final void testContent() throws Exception {
		
		
		Assert.assertEquals(3, construct.getElemsForRegex().size());
		Assert.assertEquals(SystemElement.STRING_START, construct.getElemsForRegex().get(0));
		Assert.assertTrue(construct.getElemsForRegex().get(1) instanceof Group);
		Group group = (Group)construct.getElemsForRegex().get(1);
		Assert.assertEquals(1, group.getElements().size());
		Assert.assertEquals(origin, group.getElements().get(0));
		Assert.assertEquals(SystemElement.STRING_END, construct.getElemsForRegex().get(2));
	}
	

	
	
}
