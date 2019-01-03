package de.jepfa.regex.components;

import static de.jepfa.regex.helper.Checker.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.jepfa.regex.RegexBuilder;
import de.jepfa.regex.RegexBuilder.Flag;


/**
 * This class represents all match flags of an {@link Element}.
 * Every {@link Flag} of this instance overwrites the flags set in the {@link RegexBuilder} and 
 * can be enabled (switched on) or disabled (switched off) or restored to the default.
 * 
 * @author Jens Pfahl
 */
public class Flags implements Regexable, Cloneable {

	private Map<Flag, Boolean> flags = new HashMap<>();


	/**
	 * @param enabledFlags the glags to switch on, not <code>null</code>.
	 */
	public Flags(Flag... enabledFlags) {
		checkNotNull(enabledFlags);
		switchOn(enabledFlags);
	}

	/**
	 * @return a list of all switched on flags, not <code>null</code>
	 */
	public Set<Flag> getEnabledFlags() {
		return getFlagsFor(Boolean.TRUE);
	}

	/**
	 * @return a list of all switched off flags, not <code>null</code>
	 */
	public Set<Flag> getDisabledFlags() {
		return getFlagsFor(Boolean.FALSE);
	}
	
	/**
	 * @return <code>true</code>, if there are no flags that overwrites the default. 
	 * Neither switched on flags nor switched off flags. 
	 */
	public boolean isEmpty() {
		return flags.isEmpty();
	}

	@Override
	public String toRegex() {

		StringBuffer sb = new StringBuffer();

		if (!flags.isEmpty()) {
			getFlagsAsRegex(sb);
		}

		return sb.toString();
	}
	
	
	
	@Override
	public String toString() {
		return flags.toString();
	}

	@Override
	protected Flags clone() throws CloneNotSupportedException {
		Flags clone = (Flags) super.clone();
		clone.flags = new HashMap<>(flags);
		return clone;
	}
	
	void switchOn(Flag ...flags) {
		checkNotNull(flags);
		Arrays.asList(flags).forEach(e -> this.flags.put(e, true));
	}

	void switchOff(Flag ...flags) {
		checkNotNull(flags);
		Arrays.asList(flags).forEach(e -> this.flags.put(e, false));
	}
	
	void restoreDefaults(Flag ...flags) {
		checkNotNull(flags);
		Arrays.asList(flags).forEach(e -> this.flags.remove(e));
	}
	
	void restoreAll() {
		flags.clear();
	}
	

	private void getFlagsAsRegex(StringBuffer sb) {
		sb.append(getFlagsAsRegex(getFlagsFor(Boolean.TRUE)));
		Set<Flag> disabledFlags = getFlagsFor(Boolean.FALSE);
		if (!disabledFlags.isEmpty()) {
			sb.append("-");
			sb.append(getFlagsAsRegex(disabledFlags));
		}
	}

	private String getFlagsAsRegex(Set<Flag> flags) {
		StringBuilder sb = new StringBuilder();
		for (Flag flag : flags) {
			sb.append(flag.toRegex());
		}
		return sb.toString();
	}
	
	private Set<Flag> getFlagsFor(Boolean b) {
		return Collections.unmodifiableSet(flags.entrySet().stream().filter(e->e.getValue()==b).map(e->e.getKey()).collect(Collectors.toSet()));
	}
	

}