package cht.bss.morder.dual.validate.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MoqueryContractWithTelnumType implements MoqueryEnumInterface {

	Contractret("contractret", "1", "%s&%s"), Pasuserec("pasuserec", "1", "%s&%s");

	private final String tableName;
	private final String type;
	private final String contentTemplate;
}
