package cht.bss.morder.dual.validate.factory;

import org.springframework.stereotype.Service;

import cht.bss.morder.dual.validate.vo.ComparedData;
import cht.bss.morder.dual.validate.vo.Params;
import cht.bss.morder.dual.validate.vo.QueryInput;
import cht.bss.morder.dual.validate.vo.TestCase;

@Service
public class QrySaleBehaviorInputFactory extends QueryInputFactory {

	public ComparedData getComparedData(TestCase testCase) {
		QueryInput queryInput = buildQueryInputWithTelnum(testCase);
		return builder("qrysalebehavior").data(testCase.getTelNum()).table("").queryInput(queryInput).build();
	}

	private QueryInput buildQueryInputWithTelnum(TestCase testCase) {
		QueryInput qrysalebehaviorInput = buildInput();
		qrysalebehaviorInput.setParam(Params.builder().custid(testCase.getCustId()).querydata("custbehavior;").build());

		return qrysalebehaviorInput;
	}

	private QueryInput buildInput() {
		QueryInput input = buildBasicInput();
		input.setCmd("qrysalebehavior");
		return input;
	}

}
