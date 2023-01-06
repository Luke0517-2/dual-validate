package cht.bss.morder.dual.validate.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import cht.bss.morder.dual.validate.common.exceptions.BusinessException;
import cht.bss.morder.dual.validate.config.DualValidateProperties;
import cht.bss.morder.dual.validate.service.client.ApiClient;
import cht.bss.morder.dual.validate.vo.QueryInput;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "dual-validate", name = "mode", havingValue = "API")
public class MOrderFacadeApiImpl implements MOrderFacade {

	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private ApiClient apiClient;
	
	@Autowired
	private DualValidateProperties urlProperties;

	@Override
	public String queryIISI(QueryInput queryInput) {
//		try {
//			return mapper.writeValueAsString(queryInput);
//		} catch (JsonProcessingException e) {
//			return "系統發生錯誤";
//		}
		queryInput.setBaseUrl(urlProperties.getIisi());
		return responseFromQuery(queryInput);
	}

	@Override
	public String queryCht(QueryInput queryInput) {
//		try {
//			return mapper.writeValueAsString(queryInput);
//		} catch (JsonProcessingException e) {
//			return "系統發生錯誤";
//		}
		queryInput.setBaseUrl(urlProperties.getCht());
		return responseFromQuery(queryInput);
	}

	private String responseFromQuery(QueryInput queryInput){

		String type = queryInput.getCmd();
		if(type.equals("qrysalebehavior")){
			return responseFromQrysalebehavior(queryInput);
		}else if(type.equals("QueryCustInfo")) {
			return responseFromQueryCustInfo(queryInput);
		}else if(type.equals("moquery")) {
			return responseFromMoquery(queryInput);
		}else {
			return "Input-cmd不正確";
		}
	}

	private String responseFromQrysalebehavior(QueryInput input){
		String result = StringUtils.EMPTY;
		log.trace("start query {}, telnum:{}",input.getCmd(),input.getParam().getTelnum());
		if(StringUtils.isNoneBlank(input.getParam().getTelnum())) {
			
			result = apiClient.queryApi(input);
		}else {
			log.error("input:{}", input);
			throw new BusinessException("Qrysalebehavior查詢失敗");
		}
		return result;
	}

	private String responseFromQueryCustInfo(QueryInput input) {
		String result = StringUtils.EMPTY;
		log.trace("start query {}, telnum:{}, custid:{}",input.getCmd(),input.getParam().getTelnum(),input.getParam().getCustid());
		log.trace("querydata:{}",input.getParam().getQuerydata());
		
		if(StringUtils.isNoneEmpty(input.getParam().getTelnum())) {
			result = apiClient.queryApi(input);
		}else if(StringUtils.isNoneEmpty(input.getParam().getCustid()) && StringUtils.equals("custbehavior;", input.getParam().getQuerydata())) {
			result = apiClient.queryApi(input);
		}else {
			log.error("input:{}",input);
			throw new BusinessException("QueryCustInfo查詢失敗");
		}
		return result;
	}

	private String responseFromMoquery(QueryInput input) {
		String tablename = input.getParam().getQueryitem().getTablename();
		String result = StringUtils.EMPTY;
		log.trace("start query {}, tablename:{}, param:{}",input.getCmd(),tablename,input.getParam());
		result = apiClient.queryApi(input);
		
		if(StringUtils.isNotBlank(result))
			return result;
		else
			log.error("input:{}", input);
			throw new BusinessException("Moquery "+tablename+"查詢失敗");
	}	
	
	String readFile(String path) {
		String result = StringUtils.EMPTY;
		try {
			result = FileUtils.readFileToString(new File(path),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
