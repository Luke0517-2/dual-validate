package cht.bss.morder.dual.validate.service.query;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;

import cht.bss.morder.dual.validate.common.exceptions.BusinessException;
import cht.bss.morder.dual.validate.service.MOrderFacade;
import cht.bss.morder.dual.validate.vo.ComparedData;
import cht.bss.morder.dual.validate.vo.QueryInput;
import cht.bss.morder.dual.validate.vo.TestCase;

public abstract class QueryService {

	@Autowired
	private MOrderFacade morderFacade;

	protected abstract List<ComparedData> queryData(TestCase testCase);

	public List<ComparedData> queryTestCase(TestCase testCase) {
		List<ComparedData> result = queryData(testCase);
		return result;
	}

	private MOrderFacade getMOrderFacade() {
		return morderFacade;
	}

	protected CompletableFuture<String> queryIISI(QueryInput queryInput) {
		MOrderFacade facade = getMOrderFacade();
		return CompletableFuture.supplyAsync(() -> facade.queryIISI(queryInput));
	}

	protected CompletableFuture<String> queryCht(QueryInput queryInput) {
		MOrderFacade facade = getMOrderFacade();
		return CompletableFuture.supplyAsync(() -> facade.queryCht(queryInput));
	}
	
	protected ComparedData queryForData(ComparedData comparedData) {
		QueryInput queryInput = comparedData.getQueryInput();
		CompletableFuture<String> dataFromIISI = queryIISI(queryInput);
		CompletableFuture<String> dataFromCht = queryCht(queryInput);

		List<String> errors = new ArrayList<>();

		try {
			comparedData.setDataFromCht(dataFromCht.get());
		} catch (InterruptedException | ExecutionException e) {
			errors.add(e.getMessage());
		}

		try {
			comparedData.setDataFromIISI(dataFromIISI.get());
		} catch (InterruptedException | ExecutionException e) {
			errors.add(e.getMessage());
		}

		if (errors.isEmpty()) {
			return comparedData;
		} else {
			String template = "查詢發生錯誤: %s";
			throw new BusinessException(String.format(template, String.join(",", errors)));
		}
	}

	protected CompletableFuture<List<ComparedData>> queryResult(ComparedData comparedData) {
		ComparedData data = queryForData(comparedData);
		List<ComparedData> result = new ArrayList<>();
		result.add(data);
		return CompletableFuture.supplyAsync(()-> result);
	}
}
