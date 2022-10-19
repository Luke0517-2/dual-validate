package cht.bss.morder.dual.validate.vo;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ParamsTest {

	private static Params params;

	private static ObjectMapper mapper = new ObjectMapper();
	
	@Test
	void testParameterIsNullSerialize2Json() throws JsonMappingException, JsonProcessingException {
		final String telnum = "0912345678";
		final String custid = "Helloworld";
		final String querydata = "123456";
		final QueryItem queryitem = QueryItem.builder().tablename("telnum").querytype("1,2").content("test").build();

		// build a Params Object which querydata is null.
		params = Params.builder().telnum(telnum).custid(custid).querydata(null).queryitem(queryitem).build();
		// Params Object 2 json string
		String querydataIsNull2jsonString = mapper.writeValueAsString(params);
		assertEquals(false, querydataIsNull2jsonString.contains("querydata"));
		
		params = Params.builder().telnum(telnum).custid(custid).querydata(querydata).queryitem(null).build();
		String queryitemIsNull2jsonString = mapper.writeValueAsString(params);
		assertEquals(false, queryitemIsNull2jsonString.contains("queryitem"));
	}

	@Test
	void testPAllParametersNotNull2Json() throws JsonMappingException, JsonProcessingException {
		final String telnum = "0912345678";
		final String custid = "Helloworld";
		final String querydata = "123456";
		final QueryItem queryitem = QueryItem.builder().tablename("telnum").querytype("1,2").content("test").build();

		// build a Params Object which querydata have value
		params = Params.builder().telnum(telnum).custid(custid).querydata(querydata).queryitem(queryitem).build();
		String paramsObject2jsonString = mapper.writeValueAsString(params);
		
		assertEquals(true, paramsObject2jsonString.contains("querydata"));
		assertEquals(true, paramsObject2jsonString.contains("queryitem"));
	}
	
	@Test
	void testJsonDeserialize2Params() throws JsonMappingException, JsonProcessingException {
		String querydataIsNull =  "{\"telnum\":\"0912345678\",\"custid\":\"Helloworld\",\"queryitem\":{\"tablename\":\"telnum\",\"querytype\":\"1,2\",\"content\":\"test\"}}";
		String queryitemIsNull = "{\"telnum\":\"0912345678\",\"custid\":\"Helloworld\",\"querydata\":\"123456\"}";
		String containAllKey = "{\"telnum\":\"0912345678\",\"custid\":\"Helloworld\",\"querydata\":\"123456\",\"queryitem\":{\"tablename\":\"telnum\",\"querytype\":\"1,2\",\"content\":\"test\"}}";
		
		params = mapper.readValue(querydataIsNull, Params.class);
		assertNull(params.getQuerydata());
		
		params = mapper.readValue(queryitemIsNull, Params.class);
		assertNull(params.getQueryitem());
		
		params = mapper.readValue(containAllKey, Params.class);
		assertEquals("123456", params.getQuerydata());
	}
	
}
