package cht.bss.morder.dual.validate.service.query;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;

import cht.bss.morder.dual.validate.service.MOrderFacade;
import cht.bss.morder.dual.validate.vo.ComparedData;
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

	protected void queryIISI(ComparedData comparedData) {
		MOrderFacade facade = getMOrderFacade();
		String dataFromIISI = facade.queryIISI(comparedData.getQueryInput());
		comparedData.setDataFromIISI(dataFromIISI);
	}

	protected void queryCht(ComparedData comparedData) {
		MOrderFacade facade = getMOrderFacade();
		String dataFromCht = facade.queryCht(comparedData.getQueryInput());
		comparedData.setDataFromCht(dataFromCht);
	}

	protected ComparedData queryBothServer(ComparedData comparedData) {
		queryIISI(comparedData);
		queryCht(comparedData);
		return comparedData;
	}

	protected CompletableFuture<ComparedData> queryResult(ComparedData comparedData) {
		ComparedData data = queryBothServer(comparedData);
		return CompletableFuture.supplyAsync(() -> data);
	}
}
