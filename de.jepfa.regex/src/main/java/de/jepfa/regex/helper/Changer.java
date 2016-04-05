package de.jepfa.regex.helper;

import java.util.function.Consumer;

import de.jepfa.regex.components.Changeable;

/**
 * Helper to change imutable {@link Changeable}-objects easily.
 * @author Jens Pfahl
 */
public class Changer {

	/**
	 * Helper method to change an {@link Changeable}-object.
	 * Clients should do their method calls on the <code>consumers</code>!
	 * 
	 * @param consumers not <code>null</code>
	 * 
	 * @return the source
	 */
	@SafeVarargs
	final static public <T extends Changeable> T change(T source, Consumer<T>... consumers) {
		Checker.checkNotNull(source);
		source.setChangeable();
		try {
			for (Consumer<T> c : consumers) {
				c.accept(source);
			}
		} finally {
			source.unsetChangeable();
		}
		
		return source;
	}

	/**
	 * Set all given {@link Changeable}s to {@link Changeable#setChangeable()}.
	 *
	 * @param changeables not <code>null</code>
	 */
	public static void setChangeable(Changeable ...changeables) {
		Checker.checkNotNull(changeables);
		for (Changeable changeable : changeables) {
			changeable.setChangeable();
		}
	}
	
	/**
	 * Set all given {@link Changeable}s to {@link Changeable#unsetChangeable()}.
	 *
	 * @param changeables not <code>null</code>
	 */
	public static void unsetChangeable(Changeable ...changeables) {
		Checker.checkNotNull(changeables);
		for (Changeable changeable : changeables) {
			changeable.unsetChangeable();
		}
	}

}
