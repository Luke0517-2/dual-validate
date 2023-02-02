package cht.bss.morder.dual.validate.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MoqueryRentCustNoType implements MoqueryEnumInterface {

	Pascustomer("pascustomer", "1", "%s"),
	;


	private final String tableName;
	private final String type;
	private final String contentTemplate;
}
