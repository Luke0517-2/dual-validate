package cht.bss.morder.dual.validate.adapter;

import java.util.ArrayList;
import java.util.HashSet;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cht.bss.morder.dual.validate.common.MoqueryRuleToEnumMapping;
import cht.bss.morder.dual.validate.config.CheckQueryRuleProperties;
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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class FlexQueryTableAdapter {

	@Autowired
	private MoqueryRuleToEnumMapping moqueryRuleToEnumMapping;

	@Autowired
	private CheckQueryRuleProperties checkQueryRuleProperties;

	private ArrayList<MoqueryEnumInterface> enumsQueryWithContractId;
	private ArrayList<MoqueryEnumInterface> enumsQueryWithContractIdWithTelnum;
	private ArrayList<MoqueryEnumInterface> enumsQueryWithContractIdWithOneDate;
	private ArrayList<MoqueryEnumInterface> enumsQueryWithContractIdWithTwoDate;
	private ArrayList<MoqueryEnumInterface> enumsQueryWithTelnum;
	private ArrayList<MoqueryEnumInterface> enumsQueryWithTelnumWithOneDate;
	private ArrayList<MoqueryEnumInterface> enumsQueryWithTelnumWithTwoDate;
	private ArrayList<MoqueryEnumInterface> enumsQueryTelnumsWithDate;
	private ArrayList<MoqueryEnumForTwiceQuery> enumsQueryTwicePhase;
	private ArrayList<MoqueryEnumInterface> enumsQueryWithTelnumWithMingGuoDate;
	private ArrayList<MoqueryEnumInterface> enumsQueryWithContractWithMingGuoDate;

	@PostConstruct
	void init() throws IllegalAccessException {

		String[] array = { "enumsQueryWithContractId", "enumsQueryWithContractIdWithTelnum",
				"enumsQueryWithContractIdWithOneDate", "enumsQueryWithContractIdWithTwoDate", "enumsQueryWithTelnum",
				"enumsQueryWithTelnumWithOneDate", "enumsQueryWithTelnumWithTwoDate", "enumsQueryTelnumsWithDate",
				"enumsQueryWithTelnumWithMingGuoDate", "enumsQueryWithContractWithMingGuoDate" };
		for (String target : array) {
			FieldUtils.writeDeclaredField(this, target, new ArrayList<MoqueryEnumInterface>(), true);
		}

		FieldUtils.writeDeclaredField(this, "enumsQueryTwicePhase", new ArrayList<MoqueryEnumForTwiceQuery>(), true);

		HashSet<MoqueryEnumInterface> enumInterfaces = moqueryRuleToEnumMapping
				.getEnumInterfaces(checkQueryRuleProperties.getQueryRuleList());

		for (MoqueryEnumInterface targetEnumInstance : enumInterfaces) {
			if (targetEnumInstance instanceof MoqueryContractType)
				enumsQueryWithContractId.add(targetEnumInstance);
			else if (targetEnumInstance instanceof MoqueryContractWithTelnumType)
				enumsQueryWithContractIdWithTelnum.add(targetEnumInstance);
			else if (targetEnumInstance instanceof MoqueryContractWithDateType)
				enumsQueryWithContractIdWithOneDate.add(targetEnumInstance);
			else if (targetEnumInstance instanceof MoqueryContractWithTwoDateType)
				enumsQueryWithContractIdWithTwoDate.add(targetEnumInstance);
			else if (targetEnumInstance instanceof MoqueryTelnumType)
				enumsQueryWithTelnum.add(targetEnumInstance);
			else if (targetEnumInstance instanceof MoqueryTelnumWithDateType)
				enumsQueryWithTelnumWithOneDate.add(targetEnumInstance);
			else if (targetEnumInstance instanceof MoqueryTelnumWithTwoDateType)
				enumsQueryWithTelnumWithTwoDate.add(targetEnumInstance);
			else if (targetEnumInstance instanceof MoqueryTelnumsWithDateType)
				enumsQueryTelnumsWithDate.add(targetEnumInstance);
			else if (targetEnumInstance instanceof MoqueryTelnumWithMingGuoDateType)
				enumsQueryWithTelnumWithMingGuoDate.add(targetEnumInstance);
			else if (targetEnumInstance instanceof MoqueryContractWithMingGuoDateType)
				enumsQueryWithContractWithMingGuoDate.add(targetEnumInstance);
			else if (targetEnumInstance instanceof MoqueryEnumForTwiceQuery)
				enumsQueryTwicePhase.add((MoqueryEnumForTwiceQuery) targetEnumInstance);
			else {
				throw new RuntimeException("conversion type fail , targetEnumInstance = " + targetEnumInstance);
			}
		}
	}
}
