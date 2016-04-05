package de.jepfa.regex.components;

import static de.jepfa.regex.TestHelper.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.jepfa.regex.RegexBuilder.Flag;



public class FlagsTest {
	
	private Flags flags;
	
	
	@Before
	public final void setup() {
		flags = new Flags();
	}

	@Test
	public final void testClone() throws Exception {
		flags.switchOn(Flag.DOTALL);
		Flags clonedFlags = flags.clone();
		clonedFlags.switchOn(Flag.COMMENTS);
		
		Assert.assertNotSame(flags, clonedFlags);
		Assert.assertFalse(flags.isEmpty());
		Assert.assertTrue(flags.getEnabledFlags().contains(Flag.DOTALL));
		Assert.assertFalse(flags.getEnabledFlags().contains(Flag.COMMENTS));
		
		Assert.assertFalse(clonedFlags.isEmpty());
		Assert.assertTrue(clonedFlags.getEnabledFlags().contains(Flag.DOTALL));
		Assert.assertTrue(clonedFlags.getEnabledFlags().contains(Flag.COMMENTS));
		
	}

	
	@Test
	public final void testSwitchOff() throws Exception {
		flags.switchOff(Flag.IGNORE_CASE_SENSITIVE, Flag.MULTILINE);
		
		Assert.assertTrue(flags.getDisabledFlags().contains(Flag.IGNORE_CASE_SENSITIVE));
		orAssertEquals(flags.toRegex(), "-mi", "-im");
	}
	
	@Test
	public final void testSwitchOn() throws Exception {
		flags.switchOn(Flag.IGNORE_CASE_SENSITIVE, Flag.MULTILINE);
		
		Assert.assertTrue(flags.getEnabledFlags().contains(Flag.IGNORE_CASE_SENSITIVE));
		orAssertEquals(flags.toRegex(), "mi", "im");
	}
	
	@Test
	public final void testSwitchOnOff() throws Exception {
		flags.switchOff(Flag.COMMENTS);
		
		Assert.assertTrue(flags.getDisabledFlags().contains(Flag.COMMENTS));
		Assert.assertEquals("-x", flags.toRegex());
		
		flags.switchOn(Flag.IGNORE_CASE_SENSITIVE);
		
		Assert.assertTrue(flags.getEnabledFlags().contains(Flag.IGNORE_CASE_SENSITIVE));
		Assert.assertEquals("i-x", flags.toRegex());
	}
	
	@Test
	public final void testRestoreDefaults() throws Exception {
		flags.switchOn(Flag.IGNORE_CASE_SENSITIVE);
		flags.switchOff(Flag.DOTALL);
		
		Assert.assertTrue(flags.getEnabledFlags().contains(Flag.IGNORE_CASE_SENSITIVE));
		Assert.assertTrue(flags.getDisabledFlags().contains(Flag.DOTALL));
		Assert.assertEquals("i-s", flags.toRegex());
		
		flags.restoreDefaults(Flag.IGNORE_CASE_SENSITIVE);
		
		Assert.assertFalse(flags.getEnabledFlags().contains(Flag.IGNORE_CASE_SENSITIVE));
		Assert.assertTrue(flags.getDisabledFlags().contains(Flag.DOTALL));
		Assert.assertEquals("-s", flags.toRegex());
	}
	
	@Test
	public final void testRestoreAll() throws Exception {
		flags.switchOn(Flag.IGNORE_CASE_SENSITIVE);
		flags.switchOff(Flag.DOTALL);
		
		Assert.assertTrue(flags.getEnabledFlags().contains(Flag.IGNORE_CASE_SENSITIVE));
		Assert.assertTrue(flags.getDisabledFlags().contains(Flag.DOTALL));
		Assert.assertEquals("i-s", flags.toRegex());
		
		flags.restoreAll();
		
		Assert.assertFalse(flags.getEnabledFlags().contains(Flag.IGNORE_CASE_SENSITIVE));
		Assert.assertFalse(flags.getDisabledFlags().contains(Flag.DOTALL));
		Assert.assertEquals("", flags.toRegex());
	}
	


}
