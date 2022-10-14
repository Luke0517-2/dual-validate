package cht.bss.morder.dual.validate.service;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cht.bss.morder.dual.validate.vo.QueryInput;

@Service
@ConditionalOnProperty(prefix = "dual-validate", name = "mode", havingValue = "FILE")
public class MOrderFacadeFileImpl implements MOrderFacade {

	@Autowired
	private ObjectMapper mapper;

	@Override
	public String queryIISI(QueryInput queryInput) {
		try {
			return mapper.writeValueAsString(queryInput);
		} catch (JsonProcessingException e) {
			return "系統發生錯誤";
		}
	}

	@Override
	public String queryCht(QueryInput queryInput) {
		try {
			return mapper.writeValueAsString(queryInput);
		} catch (JsonProcessingException e) {
			return "系統發生錯誤";
		}
	}

}
