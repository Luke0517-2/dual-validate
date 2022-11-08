package cht.bss.morder.dual.validate.common.exceptions;

/**
 * The Class InvalidInputException.
 *
 * @author 1909002 InvalidInputException
 */
public class InvalidInputException extends RuntimeException {
	
	/** serialVersionUID. */
	private static final long serialVersionUID = -4043316470974958200L;

	/**
	 * InvalidInputException.
	 */
	public InvalidInputException() {
		super();
	}

	/**
	 * InvalidInputException.
	 *
	 * @param message the message
	 */
	public InvalidInputException(final String message) {
		super(message);
	}

	/**
	 * InvalidInputException.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public InvalidInputException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * InvalidInputException.
	 *
	 * @param cause the cause
	 */
	public InvalidInputException(final Throwable cause) {
		super(cause);
	}
}
