package cht.bss.morder.dual.validate.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MoquerySpsvcType implements MoqueryEnumInterface {

	Mdsvc("mdsvc", "1", "%s"), Vpnsvc("vpnsvc", "1", "%s");

	private final String tableName;
	private final String type;
	private final String contentTemplate;
}
