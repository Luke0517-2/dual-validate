package cht.bss.morder.dual.validate.service.query;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cht.bss.morder.dual.validate.common.exceptions.BusinessException;
import cht.bss.morder.dual.validate.factory.QrySaleBehaviorInputFactory;
import cht.bss.morder.dual.validate.vo.ComparedData;
import cht.bss.morder.dual.validate.vo.TestCase;

@Service
public class QrysalebehaviorService extends QueryService {

	@Autowired
	private QrySaleBehaviorInputFactory factory;

	@Override
	public List<ComparedData> queryData(TestCase testCase) {
		ComparedData comparedData = buildQueryInputIntoComparedData(testCase);
		CompletableFuture<ComparedData> result = queryResult(comparedData);
		try {
			ComparedData returnedData = result.get();
			return Arrays.asList(new ComparedData[] { returnedData });
		} catch (InterruptedException | ExecutionException e) {
			comparedData.setError("執行發生錯誤");
			return Arrays.asList(new ComparedData[] {comparedData});
		}
	}

	private ComparedData buildQueryInputIntoComparedData(TestCase testCase) {
		ComparedData comparedData = factory.getComparedData(testCase);
		return comparedData;
	}

}
