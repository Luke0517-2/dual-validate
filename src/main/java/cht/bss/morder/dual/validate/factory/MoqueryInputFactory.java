package cht.bss.morder.dual.validate.factory;

import org.springframework.stereotype.Service;

import cht.bss.morder.dual.validate.common.exceptions.BusinessException;
import cht.bss.morder.dual.validate.enums.MoqueryContractType;
import cht.bss.morder.dual.validate.enums.MoqueryEnumInterface;
import cht.bss.morder.dual.validate.enums.MoqueryOrderNoType;
import cht.bss.morder.dual.validate.enums.MoquerySpsvcType;
import cht.bss.morder.dual.validate.enums.MoqueryTelnumType;
import cht.bss.morder.dual.validate.enums.MoqueryContractWithTelnumType;
import cht.bss.morder.dual.validate.vo.ComparedData;
import cht.bss.morder.dual.validate.vo.Params;
import cht.bss.morder.dual.validate.vo.QueryInput;
import cht.bss.morder.dual.validate.vo.QueryItem;
import cht.bss.morder.dual.validate.vo.QueryItem.QueryItemBuilder;
import cht.bss.morder.dual.validate.vo.TestCase;

@Service
public class MoqueryInputFactory extends QueryInputFactory {

	public ComparedData getComparedData(MoqueryEnumInterface moqueryEnum, TestCase testCase) {
		QueryInput queryInput = buildQueryInput(moqueryEnum, testCase);
		Params param = queryInput.getParam();
		QueryItem item = param.getQueryitem();
		String table = item.getTablename();
		String content = item.getContent();
		return builder("moquery").data(content).table(table).queryInput(queryInput).build();
	}

	private QueryInput buildQueryInput(MoqueryEnumInterface moqueryEnum, TestCase testCase) {
		QueryInput queryInput = buildInput();

		QueryItemBuilder queryItemBuilder = QueryItem.builder().tablename(moqueryEnum.getTableName())
				.querytype(moqueryEnum.getType());
		String template = moqueryEnum.getContentTemplate();
		
		if (moqueryEnum instanceof MoqueryContractType) {
			queryItemBuilder.content(String.format(template, testCase.getContract()));
		} else if (moqueryEnum instanceof MoqueryTelnumType) {
			queryItemBuilder.content(String.format(template, testCase.getTelNum()));
		} else if (moqueryEnum instanceof MoqueryOrderNoType) {
			queryItemBuilder.content(String.format(template, testCase.getOrderno()));
		} else if (moqueryEnum instanceof MoqueryContractWithTelnumType) {
			queryItemBuilder.content(String.format(template, testCase.getContract(), testCase.getTelNum()));
		} else if (moqueryEnum instanceof MoquerySpsvcType) {
			queryItemBuilder.content(String.format(template, testCase.getSpsvc()));
		} else {
			throw new BusinessException("未設定類型");
		}

		queryInput.setParam(Params.builder().queryitem(queryItemBuilder.build()).build());

		return queryInput;
	}

	private QueryInput buildInput() {
		QueryInput input = buildBasicInput();
		input.setCmd("moquery");
		return input;
	}
}
