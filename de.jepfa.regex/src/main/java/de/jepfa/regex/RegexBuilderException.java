package de.jepfa.regex;

/**
 * The common exception do indicate problems during the {@link RegexBuilder}-process.
 *
 * @author Jens Pfahl
 */
public class RegexBuilderException extends RuntimeException {


	private static final long serialVersionUID = 7463483611542673600L;


	public RegexBuilderException(String message, Throwable cause) {
		super(message, cause);
	}


	public RegexBuilderException(String message) {
		super(message);
	}


	public RegexBuilderException(Throwable cause) {
		super(cause);
	}

	

}
