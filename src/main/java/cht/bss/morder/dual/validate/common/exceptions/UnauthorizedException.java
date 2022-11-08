package cht.bss.morder.dual.validate.common.exceptions;

/**
 * 401 Unauthorized (HTTP/1.0 - RFC 1945).
 */
public class UnauthorizedException extends RuntimeException {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1659802510131955625L;

	/**
	 * UnauthorizedException.
	 */
	public UnauthorizedException() {
		super();
	}

	/**
	 * Instantiates a new unauthorized exception.
	 *
	 * @param message the message
	 */
	public UnauthorizedException(final String message) {
		super(message);
	}

	/**
	 * Instantiates a new unauthorized exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public UnauthorizedException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new unauthorized exception.
	 *
	 * @param cause the cause
	 */
	public UnauthorizedException(final Throwable cause) {
		super(cause);
	}
}
