package cht.bss.morder.dual.validate.common;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Component;

import cht.bss.morder.dual.validate.enums.MoqueryContractType;
import cht.bss.morder.dual.validate.enums.MoqueryContractWithDateType;
import cht.bss.morder.dual.validate.enums.MoqueryContractWithMingGuoDateType;
import cht.bss.morder.dual.validate.enums.MoqueryContractWithTelnumType;
import cht.bss.morder.dual.validate.enums.MoqueryContractWithTwoDateType;
import cht.bss.morder.dual.validate.enums.MoqueryEnumForTwiceQuery;
import cht.bss.morder.dual.validate.enums.MoqueryEnumInterface;
import cht.bss.morder.dual.validate.enums.MoqueryTelnumType;
import cht.bss.morder.dual.validate.enums.MoqueryTelnumWithDateType;
import cht.bss.morder.dual.validate.enums.MoqueryTelnumWithMingGuoDateType;
import cht.bss.morder.dual.validate.enums.MoqueryTelnumWithTwoDateType;
import cht.bss.morder.dual.validate.enums.MoqueryTelnumsWithDateType;

@Component
public class MoqueryRuleToEnumMapping {

	public HashSet<MoqueryEnumInterface> getEnumInterfaces(List<String> queryRuleList) {

		HashSet<MoqueryEnumInterface> mapping = new HashSet<>();
		queryRuleList.forEach(tableName -> {
			switch (tableName.toLowerCase()) {
			case "numberusage":
				break;
			case "agent5id":
				mapping.add(MoqueryTelnumType.Agent5id);
				break;
			case "delcustinfoapply":
				mapping.add(MoqueryTelnumType.Delcustinfoapply);
				break;
			case "eformapplyrec":
				mapping.add(MoqueryTelnumType.Eformapplyrec);
				break;
			case "contractret":
				mapping.add(MoqueryContractWithTelnumType.Contractret);
				break;
			case "pasuserec":
				mapping.add(MoqueryContractWithTelnumType.Pasuserec);
				break;
			case "projmember":
				mapping.add(MoqueryContractType.Projmember);
				break;
			case "modeldeliverdetail":
				mapping.add(MoqueryEnumForTwiceQuery.MoqueryOrderno1);
				break;
			case "modelinsrec":
				mapping.add(MoqueryEnumForTwiceQuery.MoqueryOrderno2);
				break;
			case "suspresumerec":
				mapping.add(MoqueryContractType.Suspresumerec1);
				mapping.add(MoqueryContractType.Suspresumerec2);
				break;
			case "mdsvc":
				mapping.add(MoqueryEnumForTwiceQuery.MoquerySpsvcMN);
				break;
			case "vpnsvc":
				mapping.add(MoqueryEnumForTwiceQuery.MoquerySpsvcMV);
				break;
			case "sponsorspsvc":
				mapping.add(MoqueryContractType.SponsorSpsvc);
				break;
			case "datashareinfo":
				mapping.add(MoqueryContractType.Datashareinfo);
				break;
			case "data_share_rec":
				mapping.add(MoqueryContractType.Data_share_rec);
				break;

			case "adjustbill":
				mapping.add(MoqueryTelnumWithDateType.Adjustbill);
				break;
			case "applytypechgrec":
				mapping.add(MoqueryContractWithTwoDateType.Applytypechgrec);
				break;
			case "chargeitem":
				mapping.add(MoqueryEnumForTwiceQuery.MoqueryTranscashId);
				break;
			case "chgcustrec":
				mapping.add(MoqueryContractWithDateType.Chgcustrec);
				break;
			case "custdatainfo":
				mapping.add(MoqueryTelnumWithTwoDateType.Custdatainfo);
				break;
			case "data_share_rec_log":
				mapping.add(MoqueryContractWithDateType.DataShareRecLog);
				break;
			case "deductfee":
				mapping.add(MoqueryContractType.Deductfee);
				break;
			case "delaydisc":
				mapping.add(MoqueryContractWithTwoDateType.Delaydisc);
				break;
			case "discnttype":
				mapping.add(MoqueryContractWithTwoDateType.Discnttype);
				break;
			case "einvoicerec":
				mapping.add(MoqueryContractType.Einvoicerec);
				break;
			case "empbusiness":
				mapping.add(MoqueryContractWithDateType.Empbusiness);
				break;
			case "empdiscntrec":
				mapping.add(MoqueryTelnumWithTwoDateType.Empdiscntrec);
				break;
			case "f3svc":
				mapping.add(MoqueryEnumForTwiceQuery.MoquerySpsvcF3);
				break;
			case "f4svc":
				mapping.add(MoqueryTelnumWithTwoDateType.F4svc);
				break;
			case "familysvc":
				mapping.add(MoqueryTelnumWithTwoDateType.Familysvc);
				break;
			case "modelinsrec_shop":
				mapping.add(MoqueryTelnumWithDateType.ModelinsrecShop);
				break;
			case "newdiscntreserve":
				mapping.add(MoqueryContractType.Newdiscntreserve);
				break;
			case "officialfee":
				mapping.add(MoqueryContractWithMingGuoDateType.Officialfee);
				break;
			case "or13d0log":
				mapping.add(MoqueryTelnumWithDateType.Or13d0log);
				break;
			case "packageservice":
				mapping.add(MoqueryContractWithTwoDateType.Packageservice);
				break;
			case "pascustomer":
				mapping.add(MoqueryEnumForTwiceQuery.MoqueryRentCustNo);
				break;
			case "prepaidsvc":
				mapping.add(MoqueryTelnumWithTwoDateType.Prepaidsvc);
				break;
			case "promofinereserve":
				mapping.add(MoqueryContractWithDateType.Promofinereserve);
				break;
			case "promoprodrecold":
				mapping.add(MoqueryContractWithTwoDateType.Promoprodrecold);
				break;
			case "promoprodreserve":
				mapping.add(MoqueryContractWithTwoDateType.Packageservice);
				break;
			case "qosalert":
				mapping.add(MoqueryContractWithTwoDateType.Qosalert);
				break;
			case "querylog":
				mapping.add(MoqueryTelnumWithDateType.Querylog);
				break;
			case "recashmark":
				mapping.add(MoqueryContractWithTwoDateType.Recashmark);
				break;
			case "recotemp":
				mapping.add(MoqueryTelnumWithMingGuoDateType.Recotemp);
				break;
			case "refund":
				mapping.add(MoqueryTelnumWithDateType.Refund);
				break;
			case "refundpaid":
				mapping.add(MoqueryContractWithDateType.Refundpaid);
				break;
			case "rfpaidlist":
				mapping.add(MoqueryTelnumWithDateType.Rfpaidlist);
				break;
			case "sernumusage":
				mapping.add(MoqueryTelnumWithTwoDateType.Sernumusage);
				break;
			case "sharegroupdevice":
				mapping.add(MoqueryContractWithTwoDateType.Sharegroupdevice);
				break;
			case "sharegroupmem":
				mapping.add(MoqueryContractWithTwoDateType.Sharegroupmem);
				break;
			case "specsvcmember":
				mapping.add(MoqueryTelnumWithTwoDateType.Specsvcmember);
				break;
			case "subapplytype":
				mapping.add(MoqueryContractType.Subapplytype);
				break;
			case "susptemp":
				mapping.add(MoqueryTelnumWithDateType.Susptemp);
				break;
			case "telsusptype":
				mapping.add(MoqueryContractWithDateType.Telsusptype);
				break;
			case "transcashfee":
				mapping.add(MoqueryContractWithDateType.Transcashfee2);
				break;
			case "vspecialsvc":
				mapping.add(MoqueryContractWithTwoDateType.Vspecialsvc);
				break;
			case "workingrecord":
				mapping.add(MoqueryTelnumsWithDateType.Workingrecord);
				break;
			default:
				throw new RuntimeException("TableName = " + tableName + "  is not found");
			}
		});
		return mapping;
	}

}
