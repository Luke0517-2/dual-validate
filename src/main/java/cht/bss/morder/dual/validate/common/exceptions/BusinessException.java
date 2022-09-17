package cht.bss.morder.dual.validate.common.exceptions;

/**
 * 用來包裝業務邏輯錯誤.
 */
public class BusinessException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 559969818583671445L;

	/**
	 * Instantiates a new business exception.
	 */
	public BusinessException() {
		super();
	}

	/**
	 * Instantiates a new business exception.
	 *
	 * @param message the message
	 */
	public BusinessException(final String message) {
		super(message);
	}
}
