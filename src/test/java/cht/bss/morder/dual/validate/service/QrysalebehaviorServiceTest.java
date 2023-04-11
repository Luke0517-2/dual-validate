package cht.bss.morder.dual.validate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.function.TriFunction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import cht.bss.morder.dual.validate.common.DestroyPrototypeBeansPostProcessor;
import cht.bss.morder.dual.validate.enums.MoqueryEnumInterface;
import cht.bss.morder.dual.validate.enums.QrySalebehaviorType;
import cht.bss.morder.dual.validate.enums.QueryCustinfoType;
import cht.bss.morder.dual.validate.service.query.QrysalebehaviorService;
import cht.bss.morder.dual.validate.vo.ComparedData;
import cht.bss.morder.dual.validate.vo.TestCase;

@SpringBootTest
public class QrysalebehaviorServiceTest {

	@Autowired
	private QrysalebehaviorService service;
	
	//Spring up 4.3 provide
	@Autowired
	private ObjectProvider<ComparedData> comparedDataProvider;
	
	@Autowired
	DestroyPrototypeBeansPostProcessor processor;
	
	
	@Test
	void testQueryData() {
		TestCase testCase = getTestCase();
		List<ComparedData> result = service.queryTestCase(testCase);

		assertNotNull(result);
		assertNotEquals(0, result);
		assertEquals(1, result.size());
	}

	private TestCase getTestCase() {
		return TestCase.builder().telNum("11223").custId("33445").build();
	}
	
	@Test
	void testGet() {
		ComparedData object0 = comparedDataProvider.getObject(QrySalebehaviorType.qrySalebehavior,getTestCase(),null);
		ComparedData object1 = comparedDataProvider.getObject(QueryCustinfoType.custbehavior,getTestCase(),null);
		ComparedData object2 = comparedDataProvider.getObject(QueryCustinfoType.telnum,getTestCase(),null);
		String service = object0.getQueryService();
				
		assertEquals("qrysalebehavior", service);
		int hashCode0 = object0.hashCode();		
		int hashCode1 = object1.hashCode();
		int hashCode2 = object2.hashCode();
		
		//confirm spring bean is prototype
		assertNotEquals(hashCode0,hashCode1);
		assertNotEquals(hashCode0,hashCode2);
		assertNotEquals(hashCode1,hashCode2);
		

	}

}
