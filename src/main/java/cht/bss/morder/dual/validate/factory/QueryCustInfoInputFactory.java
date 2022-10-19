package cht.bss.morder.dual.validate.factory;

import org.springframework.stereotype.Service;

import cht.bss.morder.dual.validate.enums.QueryCustinfoType;
import cht.bss.morder.dual.validate.vo.ComparedData;
import cht.bss.morder.dual.validate.vo.ComparedData.ComparedDataBuilder;
import cht.bss.morder.dual.validate.vo.Params;
import cht.bss.morder.dual.validate.vo.QueryInput;
import cht.bss.morder.dual.validate.vo.TestCase;

@Service
public class QueryCustInfoInputFactory extends QueryInputFactory {

	public ComparedData getComparedData(QueryCustinfoType type, TestCase testCase) {
		QueryInput queryCustInfoInput;
		ComparedDataBuilder builder = builder("querycustinfo");
		if (QueryCustinfoType.telnum.equals(type)) {
			queryCustInfoInput = buildQueryInputWithTelnum(testCase);
			return builder.table("telnum").queryInput(queryCustInfoInput).data(testCase.getTelNum()).build();
		} else {
			queryCustInfoInput = buildQueryInputWithCustid(testCase);
			return builder.table("custbehavior").queryInput(queryCustInfoInput).data(testCase.getCustId()).build();
		}
	}


	private QueryInput buildQueryInputWithCustid(TestCase testCase) {
	QueryInput queryCustInfoInput = buildInput();
	queryCustInfoInput.setParam(Params.builder().custid(testCase.getCustId()).querydata("custbehavior;").build());
	return queryCustInfoInput;
}


	private QueryInput buildQueryInputWithTelnum(TestCase testCase) {
		QueryInput queryCustInfoInput = buildInput();
		queryCustInfoInput.setParam(Params.builder().telnum(testCase.getTelNum()).querydata(
				"telinfo;contractinfo;accountinfo;renter;user;simcard;package_all;spsvc_current;wap_current;promo_current;grouppromo_current;AWInfo;spsvcitem;sernum;vipdata;deposit;mcpsstore;basecontract;taxexempt;empdata;npinfo;CMPInfo;")
				.build());
		return queryCustInfoInput;
	}

	private QueryInput buildInput() {
		QueryInput input = buildBasicInput();
		input.setCmd("QueryCustInfo");
		return input;
	}

}
