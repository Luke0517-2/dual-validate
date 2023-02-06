package cht.bss.morder.dual.validate.service.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cht.bss.morder.dual.validate.adapter.FlexQueryTableAdapter;
import cht.bss.morder.dual.validate.enums.MoqueryEnumForTwiceQuery;
import cht.bss.morder.dual.validate.enums.MoqueryEnumInterface;
import cht.bss.morder.dual.validate.enums.MoqueryOrderNoType;
import cht.bss.morder.dual.validate.enums.MoqueryRentCustNoType;
import cht.bss.morder.dual.validate.enums.MoquerySpsvcType;
import cht.bss.morder.dual.validate.enums.MoqueryTelnumType;
import cht.bss.morder.dual.validate.enums.MoqueryTranscashIdType;
import cht.bss.morder.dual.validate.factory.MoqueryInputFactory;
import cht.bss.morder.dual.validate.factory.ResponseVOFactory;
import cht.bss.morder.dual.validate.factory.ResponseVOFactory.ResponseType;
import cht.bss.morder.dual.validate.vo.ComparedData;
import cht.bss.morder.dual.validate.vo.TestCase;
import cht.bss.morder.dual.validate.vo.json.AbstractJSONPathModel;
import cht.bss.morder.dual.validate.vo.json.ContractIDResponseVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MoqueryService extends QueryService {
	
	@Autowired
	private FlexQueryTableAdapter flexQueryTableAdapter;
	
	ArrayList<MoqueryEnumInterface> enumsQueryWithContractId ;
	ArrayList<MoqueryEnumInterface> enumsQueryWithContractIdWithTelnum;
	ArrayList<MoqueryEnumInterface> enumsQueryWithContractIdWithOneDate;
	ArrayList<MoqueryEnumInterface> enumsQueryWithContractIdWithTwoDate;
	ArrayList<MoqueryEnumInterface> enumsQueryWithTelnum;
	ArrayList<MoqueryEnumInterface> enumsQueryWithTelnumWithOneDate;
	ArrayList<MoqueryEnumInterface> enumsQueryWithTelnumWithTwoDate;
	ArrayList<MoqueryEnumInterface> enumsQueryTelnumsWithDate;
	ArrayList<MoqueryEnumForTwiceQuery> enumsQueryTwicePhase;
	ArrayList<MoqueryEnumInterface> enumsQueryWithTelnumWithMingGuoDate;
	ArrayList<MoqueryEnumInterface> enumsQueryWithContractWithMingGuoDate;
	
	@PostConstruct
	public void init() {
	
		 enumsQueryWithContractId = flexQueryTableAdapter.enumsQueryWithContractId;
		 enumsQueryWithContractIdWithTelnum = flexQueryTableAdapter.enumsQueryWithContractIdWithTelnum;

		// 目前將 enumsQueryForSpsvc 和 enumsQueryForOrderNo 移到 enumsQueryTwicePhase 內。
//		private final ArrayList<MoqueryEnumInterface> enumsQueryForSpsvc = flexQueryTableAdapter.getEnumsQueryForSpsvc();
//		private final ArrayList<MoqueryEnumInterface> enumsQueryForOrderNo = flexQueryTableAdapter.getEnumsQueryForOrderNo();
		 enumsQueryWithContractIdWithOneDate = flexQueryTableAdapter.enumsQueryWithContractIdWithOneDate;
		 enumsQueryWithContractIdWithTwoDate = flexQueryTableAdapter.enumsQueryWithContractIdWithTwoDate;
		 enumsQueryWithTelnum = flexQueryTableAdapter.enumsQueryWithTelnum;
		 enumsQueryWithTelnumWithOneDate = flexQueryTableAdapter.enumsQueryWithTelnumWithOneDate;
		 enumsQueryWithTelnumWithTwoDate = flexQueryTableAdapter.enumsQueryWithTelnumWithTwoDate;
		 enumsQueryTelnumsWithDate = flexQueryTableAdapter.enumsQueryTelnumsWithDate;
		 enumsQueryTwicePhase = flexQueryTableAdapter.enumsQueryTwicePhase;
		 enumsQueryWithTelnumWithMingGuoDate = flexQueryTableAdapter.enumsQueryWithTelnumWithMingGuoDate;
		 enumsQueryWithContractWithMingGuoDate = flexQueryTableAdapter.enumsQueryWithContractWithMingGuoDate;
	}
	

	public final String VALUE_FROM_IISI = "VALUE_FROM_IISI";
	public final String VALUE_FROM_CHT = "VALUE_FROM_CHT";

	public final String ERRMSG_NO_NEED_TO_CHECK_WHEN_NO_DATA = "無聯單資料，不須查詢";

	@Autowired
	private MoqueryInputFactory factory;


	@Override
	public List<ComparedData> queryData(TestCase testCase) {
		List<ComparedData> totalQuerys = new ArrayList<>();
		List<CompletableFuture<ComparedData>> asyncQuerys = new ArrayList<>();
		asyncQuerys.addAll(queryAfterGetContractId(testCase));
		asyncQuerys.addAll(queryByTelnum(testCase));

		CompletableFuture.allOf(asyncQuerys.toArray(new CompletableFuture[asyncQuerys.size()])).join();
		for (CompletableFuture<ComparedData> query : asyncQuerys) {
			try {
				totalQuerys.add(query.get());
			} catch (InterruptedException | ExecutionException e) {
				log.debug(e.getMessage());
			}
		}

		return totalQuerys;
	}

	private List<CompletableFuture<ComparedData>> queryAfterGetContractId(TestCase testCase) {
		Map<String, String> contractMap = queryContractId(testCase);
		List<CompletableFuture<ComparedData>> asyncQuerys = new ArrayList<>();

		asyncQuerys.addAll(asyncQueryMocontractType(contractMap, testCase));
//        MoquerySpsvc / MoqueryOrderno / MoqueryRentCustNo / MoqueryTranscashId
		asyncQuerys.addAll(asyncQueryWithTwoPhases(contractMap, testCase));
		return asyncQuerys;
	}

	private List<CompletableFuture<ComparedData>> asyncQueryWithTwoPhases(Map<String, String> contractMap,
			TestCase testCase) {

		ArrayList<CompletableFuture<ComparedData>> list = new ArrayList<>();

		for (MoqueryEnumForTwiceQuery moqueryForTwiceQuery : enumsQueryTwicePhase) {
			List<CompletableFuture<ComparedData>> completableFuture = Stream
					.of(moqueryForTwiceQuery.getMoqueryEnumForFirstPhases())
					.map(moqueryFirstPhase -> getAsyncTasksForWithTwoPhases(moqueryFirstPhase,
							moqueryForTwiceQuery.getMoqueryEnumSecondPhase(), moqueryForTwiceQuery.getResponse(),
							contractMap, testCase))
					.collect(Collectors.toList());
			list.addAll(completableFuture);
		}
		return list;
	}

	private CompletableFuture<ComparedData> getAsyncTasksForWithTwoPhases(MoqueryEnumInterface moqueryFirstType,
			MoqueryEnumInterface moquerySecondType, ResponseVOFactory.ResponseType response,
			Map<String, String> contractMap, TestCase testCase) {

		return CompletableFuture.supplyAsync(() -> {
			CompletableFuture<ComparedData> futureFromCht = queryChtForWithTwoPhases(moqueryFirstType,
					moquerySecondType, response, contractMap, testCase);
			CompletableFuture<ComparedData> futureFromIISI = queryIISIForWithTwoPhases(moqueryFirstType,
					moquerySecondType, response, contractMap, testCase);

			CompletableFuture[] futureArray = new CompletableFuture[] { futureFromCht, futureFromIISI };
			CompletableFuture.allOf(futureArray).join();

			try {
				return mergeQuerys(futureFromCht.get(), futureFromIISI.get());
			} catch (InterruptedException | ExecutionException e) {
				ComparedData comparedData = factory.getComparedData(moquerySecondType, testCase);
				comparedData.setError(e.getMessage());
				return comparedData;
			}
		});
	}

	private CompletableFuture<ComparedData> queryChtForWithTwoPhases(MoqueryEnumInterface firstQueryEnum,
			MoqueryEnumInterface secondQueryEnum, ResponseVOFactory.ResponseType response,
			Map<String, String> contractMap, TestCase testCase) {

		TwoPhasedQueryToCht queryCht = new TwoPhasedQueryToCht(testCase, firstQueryEnum, secondQueryEnum, response);
		return queryCht.runTwoPhaseQuery(contractMap.get(VALUE_FROM_CHT));
	}

	private CompletableFuture<ComparedData> queryIISIForWithTwoPhases(MoqueryEnumInterface firstQueryEnum,
			MoqueryEnumInterface secondQueryEnum, ResponseVOFactory.ResponseType response,
			Map<String, String> contractMap, TestCase testCase) {

		TwoPhasedQueryToIISI queryIISI = new TwoPhasedQueryToIISI(testCase, firstQueryEnum, secondQueryEnum, response);
		return queryIISI.runTwoPhaseQuery(contractMap.get(VALUE_FROM_IISI));
	}

	private List<CompletableFuture<ComparedData>> asyncQueryMocontractType(Map<String, String> contractMap,
			TestCase testCase) {
		List<CompletableFuture<ComparedData>> asyncQuerys = new ArrayList<>();

		HashSet<ArrayList<MoqueryEnumInterface>> totalOfQueryMocontractType = new HashSet<>();
		totalOfQueryMocontractType.add(enumsQueryWithContractWithMingGuoDate);
		totalOfQueryMocontractType.add(enumsQueryWithContractIdWithTwoDate);
		totalOfQueryMocontractType.add(enumsQueryWithContractIdWithOneDate);
		totalOfQueryMocontractType.add(enumsQueryWithContractIdWithTelnum);
		totalOfQueryMocontractType.add(enumsQueryWithContractId);
		
		for(ArrayList<MoqueryEnumInterface> targetContractTypeArray : totalOfQueryMocontractType) {
			if(ObjectUtils.isNotEmpty(targetContractTypeArray)) {
				for(MoqueryEnumInterface moqueryOfContractType : targetContractTypeArray) {
				asyncQuerys.add(getAsyncQueryForContract(testCase, contractMap, moqueryOfContractType));
				}
			}	
		}
//		MoqueryEnumInterface[][] totalOfQueryMocontractType = { enumsQueryWithContractId,
//				enumsQueryWithContractIdWithTelnum, enumsQueryWithContractIdWithOneDate,
//				enumsQueryWithContractIdWithTwoDate, enumsQueryWithContractWithMingGuoDate };
//
//		for (MoqueryEnumInterface[] targetContractTypeArray : totalOfQueryMocontractType) {
//			for (MoqueryEnumInterface contractType : targetContractTypeArray) {
//				asyncQuerys.add(getAsyncQueryForContract(testCase, contractMap, contractType));
//			}
//		}

		return asyncQuerys;
	}

	private CompletableFuture<ComparedData> getAsyncQueryForContract(TestCase testCase, Map<String, String> contractMap,
			MoqueryEnumInterface contractType) {
		return CompletableFuture.supplyAsync(() -> {
			TestCase testCaseForCht = getNewTestCaseWithContractId(testCase, contractMap.get(VALUE_FROM_CHT));
			ComparedData comparedForCht = factory.getComparedData(contractType, testCaseForCht);
			queryCht(comparedForCht);

			TestCase testCaseForIISI = getNewTestCaseWithContractId(testCase, contractMap.get(VALUE_FROM_IISI));
			ComparedData comparedForIISI = factory.getComparedData(contractType, testCaseForIISI);
			queryIISI(comparedForIISI);

			return mergeQuerys(comparedForCht, comparedForIISI);
		});
	}

	protected ComparedData mergeQuerys(ComparedData comparedForCht, ComparedData comparedForIISI) {

		ComparedData obj = comparedForCht.clone();
		obj.setDataFromCht(comparedForCht.getDataFromCht());
		obj.setDataFromIISI(comparedForIISI.getDataFromIISI());

		showErrorMsgWhenCheckMoquery(obj);

		return obj;
	}

	private ComparedData showErrorMsgWhenCheckMoquery(ComparedData mergedComparedData) {
		if (StringUtils.isEmpty(mergedComparedData.getDataFromCht())
				&& StringUtils.isEmpty(mergedComparedData.getDataFromIISI())) {
			mergedComparedData.setError(ERRMSG_NO_NEED_TO_CHECK_WHEN_NO_DATA);
		} else {
			return mergedComparedData;
		}
		return mergedComparedData;
	}

	/*
	 * @parma value = contractId
	 */
	private TestCase getNewTestCaseWithContractId(TestCase testcase, String value) {
		TestCase newObj = testcase.clone();
		newObj.setContract(value);
		return newObj;
	}

	private Map<String, String> queryContractId(TestCase testCase) {
		Map<String, String> map = new HashMap<>();
		ComparedData comparedData = factory.getComparedData((MoqueryEnumInterface) MoqueryTelnumType.Numberusage,
				testCase);
		queryBothServer(comparedData);
		ContractIDResponseVO voFromIISI = ContractIDResponseVO.builder(comparedData.getDataFromIISI());
		ContractIDResponseVO voFromCht = ContractIDResponseVO.builder(comparedData.getDataFromCht());

		map.put(VALUE_FROM_IISI, voFromIISI.getContractID());
		map.put(VALUE_FROM_CHT, voFromCht.getContractID());

		return map;
	}

	private List<CompletableFuture<ComparedData>> queryByTelnum(TestCase testCase) {
		List<ComparedData> asyncQuery = getQueryListByTelnum(testCase);
		List<CompletableFuture<ComparedData>> async = asyncQueryBothServer(asyncQuery);
		return async;
	}

	private List<ComparedData> getQueryListByTelnum(TestCase testCase) {

		ArrayList<ComparedData> queryTelnumList = new ArrayList<>();
		
		HashSet<ArrayList<MoqueryEnumInterface>> totalOfQueryTelnumType = new HashSet<>();
		totalOfQueryTelnumType.add(enumsQueryWithTelnum);
		totalOfQueryTelnumType.add(enumsQueryWithTelnumWithOneDate);
		totalOfQueryTelnumType.add(enumsQueryWithTelnumWithTwoDate);
		totalOfQueryTelnumType.add(enumsQueryWithTelnumWithMingGuoDate);
		totalOfQueryTelnumType.add(enumsQueryTelnumsWithDate);
		
		for(ArrayList<MoqueryEnumInterface> targetTelnumTypeArray : totalOfQueryTelnumType) {
			if(ObjectUtils.isNotEmpty(targetTelnumTypeArray)) {
				for (MoqueryEnumInterface moqueryOftelnumType : targetTelnumTypeArray) {
				queryTelnumList.add(factory.getComparedData(moqueryOftelnumType, testCase));
				}
			}
		}
		
//		MoqueryEnumInterface[][] totalOfQueryTelnumType = { enumsQueryWithTelnum, enumsQueryWithTelnumWithOneDate,
//				enumsQueryWithTelnumWithTwoDate, enumsQueryWithTelnumWithMingGuoDate, enumsQueryTelnumsWithDate };
//
//		for (MoqueryEnumInterface[] targetTelnumTypeArray : totalOfQueryTelnumType) {
//			for (MoqueryEnumInterface telnumType : targetTelnumTypeArray) {
//				queryTelnumList.add(factory.getComparedData(telnumType, testCase));
//			}
//		}

		return queryTelnumList;
	}

	protected List<CompletableFuture<ComparedData>> asyncQueryBothServer(List<ComparedData> list) {
		return list.stream().map(compared -> queryResult(compared)).collect(Collectors.toList());
	}

	@AllArgsConstructor
	private class TwoPhasedQueryToCht {

		private TestCase testCase;
		private MoqueryEnumInterface firstQueryEnum;
		private MoqueryEnumInterface secondQueryEnum;
		private ResponseType responseType;

		public CompletableFuture<ComparedData> runTwoPhaseQuery(String contractId) {

			TestCase testCaseForServer = getNewTestCaseWithContractId(testCase, contractId);
			ComparedData comparedDataForFirstQuery = factory.getComparedData(firstQueryEnum, testCaseForServer);

			return CompletableFuture.supplyAsync(() -> {
				callTo(comparedDataForFirstQuery);
				String value = getValue(comparedDataForFirstQuery.getDataFromCht());
				setValue(testCaseForServer, value, secondQueryEnum);
				ComparedData comparedDataWithSecondQuery = factory.getComparedData(secondQueryEnum, testCaseForServer);
				if (StringUtils.isEmpty(value)) {
					return comparedDataWithSecondQuery.clone();
				} else {
					callTo(comparedDataWithSecondQuery);
					return comparedDataWithSecondQuery;
				}
			});
		}

		private String getValue(String returnString) {
			AbstractJSONPathModel resVO = ResponseVOFactory.getResponseModel(responseType, returnString);
			return resVO.getValue();
		}

		private void callTo(ComparedData comparedData) {
			queryCht(comparedData);
		}
	}

	protected void setValue(TestCase testCase, String value, MoqueryEnumInterface secondQueryEnum) {
		if (secondQueryEnum instanceof MoquerySpsvcType) {
			testCase.setSpsvc(value);
		} else if (secondQueryEnum instanceof MoqueryOrderNoType) {
			testCase.setOrderno(value);
		} else if (secondQueryEnum instanceof MoqueryTranscashIdType) {
			testCase.setTranscashId(value);
		} else if (secondQueryEnum instanceof MoqueryRentCustNoType) {
			testCase.setRentcustno(value);
		} else {
			testCase.setErrorReason(value);
		}
	}

	@AllArgsConstructor
	private class TwoPhasedQueryToIISI {

		private TestCase testCase;
		private MoqueryEnumInterface firstQueryEnum;
		private MoqueryEnumInterface secondQueryEnum;
		private ResponseType responseType;

		public CompletableFuture<ComparedData> runTwoPhaseQuery(String contractId) {

			TestCase testCaseForServer = getNewTestCaseWithContractId(testCase, contractId);
			ComparedData comparedDataForFirstQuery = factory.getComparedData(firstQueryEnum, testCaseForServer);

			return CompletableFuture.supplyAsync(() -> {
				callTo(comparedDataForFirstQuery);
				String value = getValue(comparedDataForFirstQuery.getDataFromIISI());
				setValue(testCaseForServer, value, secondQueryEnum);
				ComparedData comparedDataWithSecondQuery = factory.getComparedData(secondQueryEnum, testCaseForServer);
				if (StringUtils.isEmpty(value)) {
					return comparedDataWithSecondQuery.clone();
				} else {
					callTo(comparedDataWithSecondQuery);
					return comparedDataWithSecondQuery;
				}
			});
		}

		private String getValue(String returnString) {
			try {
				AbstractJSONPathModel resVO = ResponseVOFactory.getResponseModel(responseType, returnString);
				return resVO.getValue();
			} catch (Exception e) {
				return null;
			}
		}

		private void callTo(ComparedData comparedData) {
			queryIISI(comparedData);
		}
	}
}
