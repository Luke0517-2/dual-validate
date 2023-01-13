package cht.bss.morder.dual.validate.factory;

import cht.bss.morder.dual.validate.enums.*;
import org.springframework.stereotype.Service;

import cht.bss.morder.dual.validate.common.exceptions.BusinessException;
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
		QueryInput queryInput = buildInput(); //將除了param以外放進去

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
			switch ((MoquerySpsvcType) moqueryEnum) {
				case Mdsvc:
				case Vpnsvc:
					queryItemBuilder.content(String.format(template, testCase.getSpsvc()));
					break;
				case F3svc:
//					queryItemBuilder.content(String.format(template, testCase.getSpsvc(), testCase.getFirstDate(), testCase.getSecondDate()));
					break;
			}
		} else if (moqueryEnum instanceof MoqueryRentCustNoType) {
			queryItemBuilder.content(String.format(template, testCase.getRentcustno()));
		} else if (moqueryEnum instanceof MoqueryTranscashIdType) {
			queryItemBuilder.content(String.format(template, testCase.getTranscashId()));

		} else if (moqueryEnum instanceof MoqueryContractWithDateType) {
//			queryItemBuilder.content(String.format(template, testCase.getContract(), testCase.getOneDate()));
		} else if (moqueryEnum instanceof MoqueryContractWithTwoDateType) {
//			queryItemBuilder.content(String.format(template, testCase.getContract(), testCase.getFirstDate(), testCase.getSecondDate()));
		} else if (moqueryEnum instanceof MoqueryTelnumWithDateType) {
//			queryItemBuilder.content(String.format(template, testCase.getContract(), testCase.getOneDate()));
		} else if (moqueryEnum instanceof MoqueryTelnumWithTwoDateType) {
//			queryItemBuilder.content(String.format(template, testCase.getContract(), testCase.getFirstDate(), testCase.getSecondDate()));

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
