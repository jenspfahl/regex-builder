package de.jepfa.regex;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.junit.Assert;

/**
 * @author Jens Pfahl
 */
public class TestHelper {
	
	public static <T> void orAssertEquals(T result, List<T> expected) {
		orAssertEquals(result, expected.toArray());
	}

	@SafeVarargs
	public static void orAssertEquals(Object result, Object ... expected) {
		if (expected != null && expected.length > 0) {
			long fails = Stream.of(expected).filter(e->!Objects.equals(e, result)).count();
			if (fails == expected.length) {
				// force fail
				Assert.assertEquals(expected[0], result);
			}
		}
	}
}
