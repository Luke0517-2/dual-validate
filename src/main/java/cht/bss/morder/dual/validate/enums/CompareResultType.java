package cht.bss.morder.dual.validate.enums;

public enum CompareResultType {
	EQUAL("一致"), NONEQUAL("不相同"), HAVENOTTOCOMPARE("無須比對");

	CompareResultType(final String value) {
		this.value = value;
	}

	private final String value;

	public String getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return this.value;
	}
}