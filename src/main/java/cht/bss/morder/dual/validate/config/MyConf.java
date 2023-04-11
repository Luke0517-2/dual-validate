package cht.bss.morder.dual.validate.config;

import java.time.OffsetDateTime;
import java.util.List;

import org.apache.commons.lang3.function.TriFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import cht.bss.morder.dual.validate.enums.MoqueryEnumInterface;
import cht.bss.morder.dual.validate.factory.QueryInputFactory;
import cht.bss.morder.dual.validate.vo.ComparedData;
import cht.bss.morder.dual.validate.vo.Report;
import cht.bss.morder.dual.validate.vo.TestCase;

@Configuration
public class MyConf {
	
	@Qualifier("queryInputFactory")
	@Autowired
	QueryInputFactory factory;

	@Bean
	@Scope(BeanDefinition.SCOPE_PROTOTYPE)
	public ComparedData comparedData(Enum type, TestCase testCase , MoqueryEnumInterface moquery) {
		return factory.getComparedData(type, testCase, moquery);
	}
	
	@Bean
	@Scope(BeanDefinition.SCOPE_PROTOTYPE)
	public Report report(String uuid,OffsetDateTime startDate,List<TestCase> testCases) {
		return new Report(uuid,startDate,testCases);
	}
	
//	use function certain type-safely
	@Bean
	public TriFunction<String, OffsetDateTime, List<TestCase>, Report> reportFactory(){
		return  (uuid, startDate, testCases) -> report(uuid, startDate, testCases);
	}
	
	
}
