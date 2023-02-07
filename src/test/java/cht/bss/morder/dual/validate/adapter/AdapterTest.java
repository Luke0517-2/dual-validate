package cht.bss.morder.dual.validate.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cht.bss.morder.dual.validate.common.MoqueryRuleToEnumMapping;
import cht.bss.morder.dual.validate.config.CheckQueryRuleProperties;

@SpringBootTest
public class AdapterTest {

	@Autowired
	private FlexQueryTableAdapter adapter;
	
	@Autowired
	private CheckQueryRuleProperties properties;
	
	@Autowired
	private MoqueryRuleToEnumMapping mapping;
	
	
//	@Test
//	void initFieldsNumbers() {
//		assertThat(FieldUtils.getAllFields(FlexQueryTableAdapter.class).length)
//	      .isEqualTo(13);  	
//	}
	
	@Test
	void writeValue() throws IllegalAccessException {
		
		assertThat(FieldUtils.readField(adapter,"enumsQueryWithTelnum", true)).isNotNull();
		assertThat(FieldUtils.readField(adapter,"enumsQueryWithTelnumWithTwoDate", true)).isNotNull();
	    assertThat(FieldUtils.readField(adapter,"enumsQueryTwicePhase", true)).isNotNull();
	      
	}
	
	@Test
	void getValueFromProperties() {
		assertEquals(56, mapping.getEnumInterfaces(properties.getQueryRuleList()).size());
	}
}
