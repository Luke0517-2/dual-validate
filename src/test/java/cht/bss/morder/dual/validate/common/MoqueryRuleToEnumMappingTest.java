package cht.bss.morder.dual.validate.common;

import cht.bss.morder.dual.validate.config.CheckQueryRuleProperties;
import cht.bss.morder.dual.validate.enums.MoqueryContractType;
import cht.bss.morder.dual.validate.enums.MoqueryEnumForTwiceQuery;
import cht.bss.morder.dual.validate.enums.MoqueryEnumInterface;
import cht.bss.morder.dual.validate.enums.MoqueryTelnumType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MoqueryRuleToEnumMappingTest {

	@Autowired
	private MoqueryRuleToEnumMapping queryMapping;
	@Autowired
	private CheckQueryRuleProperties checkQueryRuleProperties;


	@Test
	public void getEnumFromRules_test() {

		ArrayList<String> checkedCase = new ArrayList<>();
		checkedCase.add("Agent5id");
		checkedCase.add("Deductfee");
		checkedCase.add("vpnsvc");

		CheckQueryRuleProperties checkQueryRuleProperties = new CheckQueryRuleProperties();
		checkQueryRuleProperties.setQueryRuleList(checkedCase);

		HashSet<MoqueryEnumInterface> expectEnum = new HashSet<>();
		expectEnum.add(MoqueryTelnumType.Agent5id);
		expectEnum.add(MoqueryContractType.Deductfee);
		expectEnum.add(MoqueryEnumForTwiceQuery.MoquerySpsvcMV);

		assertEquals(expectEnum, queryMapping.getEnumInterfaces(checkQueryRuleProperties.getQueryRuleList()));
	}

	@Test
	public void getAllEnumFromAllRules_test() {
		String[] allRules = {"Agent5id", "Delcustinfoapply", "Eformapplyrec", "Contractret", "Pasuserec", "Projmember",
				"Modeldeliverdetail", "Modelinsrec", "Suspresumerec", "Mdsvc", "Vpnsvc", "SponsorSpsvc",
				"Datashareinfo", "Data_share_rec", "adjustbill", "applytypechgrec", "chargeitem", "chgcustrec",
				"custdatainfo", "data_share_rec_log", "deductfee", "delaydisc", "discnttype", "einvoicerec",
				"empbusiness", "empdiscntrec", "f3svc", "f4svc", "familysvc", "modelinsrec_shop", "newdiscntreserve",
				"officialfee", "or13d0log", "packageservice", "pascustomer", "prepaidsvc", "promofinereserve",
				"promoprodrecold", "promoprodreserve", "qosalert", "querylog", "recashmark", "recotemp", "refund",
				"refundpaid", "rfpaidlist", "sernumusage", "sharegroupdevice", "sharegroupmem", "specsvcmember",
				"subapplytype", "susptemp", "telsusptype", "transcashfee", "vspecialsvc", "workingrecord"};

		// 總共規則數量：(第一階段文件)14 + (第二階段文件)42 + 1(numberusage)
		assertEquals(allRules.length + 1, queryMapping.getEnumInterfaces(checkQueryRuleProperties.getQueryRuleList()).size());
	}
}