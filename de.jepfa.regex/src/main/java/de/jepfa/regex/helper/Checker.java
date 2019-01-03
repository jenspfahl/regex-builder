package de.jepfa.regex.helper;

import java.util.Objects;
import java.util.function.BooleanSupplier;

import de.jepfa.regex.RegexBuilderException;

/**
 * Helper for checks.
 *
 * @author Jens Pfahl
 */
public class Checker {
	
	public static void checkCondition(String msg, BooleanSupplier p) {
		if (p.getAsBoolean()) {
			throw new RegexBuilderException(msg);
		}
	}
	public static void checkNotNull(Object o) {
		if (Objects.isNull(o)) {
			throw new RegexBuilderException(" should not be null");
		}
	}
	
	public static void checkNoNullElements(Object[] o) {
		checkNotNull(o);
		for (Object object : o) {
			checkNotNull(object);
		}
	}
}
