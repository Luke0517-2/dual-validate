package cht.bss.morder.dual.validate.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.DependsOn;
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

@DependsOn(value = {"checkQueryRuleProperties"})
@Component
public class FlexQueryTableAdapter {
	
	@Autowired
	private MoqueryRuleToEnumMapping moqueryRuleToEnumMapping;
	
//	@Autowired
//	private CheckQueryRuleProperties checkQueryRuleProperties;

	public ArrayList<MoqueryEnumInterface> enumsQueryWithContractId = new ArrayList<>();
	public ArrayList<MoqueryEnumInterface> enumsQueryWithContractIdWithTelnum = new ArrayList<>();
	public ArrayList<MoqueryEnumInterface> enumsQueryWithContractIdWithOneDate = new ArrayList<>();
	public ArrayList<MoqueryEnumInterface> enumsQueryWithContractIdWithTwoDate = new ArrayList<>();
	public ArrayList<MoqueryEnumInterface> enumsQueryWithTelnum = new ArrayList<>();
	public ArrayList<MoqueryEnumInterface> enumsQueryWithTelnumWithOneDate = new ArrayList<>();
	public ArrayList<MoqueryEnumInterface> enumsQueryWithTelnumWithTwoDate = new ArrayList<>();
	public ArrayList<MoqueryEnumInterface> enumsQueryTelnumsWithDate = new ArrayList<>();
	public ArrayList<MoqueryEnumForTwiceQuery> enumsQueryTwicePhase = new ArrayList<>();
	public ArrayList<MoqueryEnumInterface> enumsQueryWithTelnumWithMingGuoDate = new ArrayList<>();
	public ArrayList<MoqueryEnumInterface> enumsQueryWithContractWithMingGuoDate = new ArrayList<>();
	

	
	
	@PostConstruct
	public void init() {
		
//	List[] totalEnumOfList ={enumsQueryWithContractId,enumsQueryWithContractIdWithTelnum,enumsQueryWithContractIdWithOneDate,enumsQueryWithContractIdWithTwoDate,
//		     enumsQueryWithTelnum,enumsQueryWithTelnumWithOneDate,enumsQueryWithTelnumWithTwoDate,enumsQueryTelnumsWithDate,
//		     enumsQueryTwicePhase,enumsQueryWithTelnumWithMingGuoDate,enumsQueryWithContractWithMingGuoDate};
//	
//	for(List target : totalEnumOfList) {
//		target = new ArrayList<MoqueryEnumInterface>();
//	}
	
	HashSet<MoqueryEnumInterface> enumInterfaces = moqueryRuleToEnumMapping.getEnumInterfaces();

		for(MoqueryEnumInterface targetEnumInstance : enumInterfaces) {
				if(targetEnumInstance instanceof MoqueryContractType)
					enumsQueryWithContractId.add(targetEnumInstance);
				else if(targetEnumInstance instanceof MoqueryContractWithTelnumType)
					enumsQueryWithContractIdWithTelnum.add(targetEnumInstance);
				else if(targetEnumInstance instanceof MoqueryContractWithDateType)
					enumsQueryWithContractIdWithOneDate.add(targetEnumInstance);
				else if(targetEnumInstance instanceof MoqueryContractWithTwoDateType)
					enumsQueryWithContractIdWithTwoDate.add(targetEnumInstance);	
				else if(targetEnumInstance instanceof MoqueryTelnumType)
					enumsQueryWithTelnum.add(targetEnumInstance);		
				else if(targetEnumInstance instanceof MoqueryTelnumWithDateType)
					enumsQueryWithTelnumWithOneDate.add(targetEnumInstance);
				else if(targetEnumInstance instanceof MoqueryTelnumWithTwoDateType)
					enumsQueryWithTelnumWithTwoDate.add(targetEnumInstance);				
				else if(targetEnumInstance instanceof MoqueryTelnumsWithDateType)
					enumsQueryTelnumsWithDate.add(targetEnumInstance);				
				else if(targetEnumInstance instanceof MoqueryTelnumWithMingGuoDateType)
					enumsQueryWithTelnumWithMingGuoDate.add(targetEnumInstance);						
				else if(targetEnumInstance instanceof MoqueryContractWithMingGuoDateType)
					enumsQueryWithContractWithMingGuoDate.add(targetEnumInstance);	
				else if(targetEnumInstance instanceof MoqueryEnumForTwiceQuery)
					enumsQueryTwicePhase.add((MoqueryEnumForTwiceQuery) targetEnumInstance);
				else {
					throw new RuntimeException("conversion type fail , targetEnumInstance = " + targetEnumInstance);		
			}
		}
	}
}
