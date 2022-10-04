package cht.bss.morder.dual.validate.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MoqueryContractType implements MoqueryEnumInterface {

	Projmember("projmember", "3", "%s"), AgentMobileSet("agentmobset", "1", "%s"),
	AgentMobileSetPart("agentmobsetpart", "1", "%s"), Modeldeliverdetail("modeldeliverdetail", "1", "%s"),
	Suspresumerec1("suspresumerec", "1", "%s"), Suspresumerec2("suspresumerec", "2", "%s"),
	SpecsvcidMN("specialsvc", "1", "%s&MN"), SpecsvcidMV("specialsvc", "1", "%s&MV"),
	SponsorSpsvc("sponsorspsvc", "1", "%s"), Datashareinfo("datashareinfo", "1", "%s"),
	Data_share_rec("data_share_rec", "1", "%s");

	private final String tableName;
	private final String type;
	private final String contentTemplate;
}
