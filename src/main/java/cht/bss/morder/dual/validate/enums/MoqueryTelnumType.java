package cht.bss.morder.dual.validate.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MoqueryTelnumType implements MoqueryEnumInterface {
	//use telnum
	Agent5id("agent5id", "1", "%s"), 
	Delcustinfoapply("delcustinfoapply", "1", "%s"),
	Eformapplyrec("eformapplyrec", "1", "%s"),
	Npinrecord("npinrecord", "4" , "%s"),
	;

	private final String tableName;
	private final String type;
	private final String contentTemplate;
}
