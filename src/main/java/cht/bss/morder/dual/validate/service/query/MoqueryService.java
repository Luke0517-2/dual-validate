package cht.bss.morder.dual.validate.service.query;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cht.bss.morder.dual.validate.enums.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.ls.LSOutput;

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

	private static final MoqueryEnumInterface[] enumsQueryWithContractId = { MoqueryContractType.Projmember,
			MoqueryContractType.Suspresumerec1, MoqueryContractType.Suspresumerec2, MoqueryContractType.Datashareinfo,
			MoqueryContractType.Data_share_rec, MoqueryContractType.SponsorSpsvc,

			MoqueryContractType.Deductfee,MoqueryContractType.Einvoicerec,MoqueryContractType.Newdiscntreserve,MoqueryContractType.Subapplytype
	};  // 只有單純的 %s
	private static final MoqueryEnumInterface[] enumsQueryWithContractIdWithTelnum = {
			MoqueryContractWithTelnumType.Contractret, MoqueryContractWithTelnumType.Pasuserec };
	private static final MoqueryEnumInterface[] enumsQueryForSpsvc = { MoqueryContractType.SpecsvcidMN,
			MoqueryContractType.SpecsvcidMV,  MoqueryContractType.SpecsvcidF3};

	private static final MoqueryEnumInterface[] enumsQueryForOrderNo = { MoqueryContractType.AgentMobileSet,
			MoqueryContractType.AgentMobileSetPart };  //有第二步為找orderNo
	private static final MoqueryForAttacch[] enumsQueryAttatch = { MoqueryForAttacch.MoqueryRentCustNo, MoqueryForAttacch.MoqueryTranscashId };


	private static final  MoqueryEnumInterface[] enumsQueryWithContractIdWithOneDate = {MoqueryContractWithDateType.Chgcustrec,
		MoqueryContractWithDateType.DataShareRecLog,MoqueryContractWithDateType.Empbusiness,MoqueryContractWithDateType.Promofinereserve,
		MoqueryContractWithDateType.Qosalert,MoqueryContractWithDateType.Refundpaid,MoqueryContractWithDateType.Telsusptype,
		MoqueryContractWithDateType.Transcashfee1,MoqueryContractWithDateType.Transcashfee2};

	private static final  MoqueryEnumInterface[] enumsQueryWithContractIdWithTwoDate = {MoqueryContractWithTwoDateType.Applytypechgrec,
			MoqueryContractWithTwoDateType.Delaydisc,MoqueryContractWithTwoDateType.Discnttype,MoqueryContractWithTwoDateType.Packageservice,
			MoqueryContractWithTwoDateType.Promoprodrecold,MoqueryContractWithTwoDateType.Promoprodreserve,MoqueryContractWithTwoDateType.Vspecialsvc};

	private static  final MoqueryEnumInterface[] enumsQueryWithTelnum =  { MoqueryTelnumType.Agent5id,
			MoqueryTelnumType.Delcustinfoapply, MoqueryTelnumType.Eformapplyrec };

	private static  final MoqueryEnumInterface[] enumsQueryWithTelnumWithOneDate =  { MoqueryTelnumWithDateType.Adjustbill,
			MoqueryTelnumWithDateType.ModelinsrecShop, MoqueryTelnumWithDateType.Or13d0log,
			MoqueryTelnumWithDateType.Refund, MoqueryTelnumWithDateType.Rfpaidlist, MoqueryTelnumWithDateType.Susptemp };

	private static  final MoqueryEnumInterface[] enumsQueryWithTelnumWithTwoDate =  { MoqueryTelnumWithTwoDateType.Custdatainfo,
			MoqueryTelnumWithTwoDateType.Empdiscntrec,MoqueryTelnumWithTwoDateType.F4svc,MoqueryTelnumWithTwoDateType.Familysvc,
			MoqueryTelnumWithTwoDateType.Specsvcmember, MoqueryTelnumWithTwoDateType.Sharegroupmem};

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
		// MoqueryContractType, MoqueryContractWithTelnumType, enumsQueryWithContractIdWithOneDate, enumsQueryWithContractIdWithTwoDate
		asyncQuerys.addAll(asyncQueryMocontractType(contractMap, testCase));
		// MoquerySpsvc.mdsvc
		asyncQuerys.addAll(asyncQueryMospsvcType(contractMap, testCase));
		//
		asyncQuerys.addAll(asyncQueryOrderType(contractMap, testCase));
		//
		asyncQuerys.addAll(asyncQueryAttatch(contractMap, testCase));

		return asyncQuerys;
	}

	private List<CompletableFuture<ComparedData>> asyncQueryAttatch(Map<String, String> contractMap,
																	  TestCase testCase) {

		List<CompletableFuture<ComparedData>> asyncRequest = new ArrayList<>();
		List<CompletableFuture<ComparedData>> asyncQueryForAttach = Stream.of(enumsQueryAttatch)
				.map(attachType -> {
					for (MoqueryEnumInterface face1 : attachType.getMoqueryEnumFirstInterface())
					{
						for (MoqueryEnumInterface face2 : attachType.getMoqueryEnumSecondInterface())
						{
							return getAsyncTasksForAttach(face1, face2, attachType.getResponse(), contractMap, testCase);
						}
					}
					return null;
				}).collect(Collectors.toList());
		asyncRequest.addAll(asyncQueryForAttach);
		return asyncRequest;
	}

	private CompletableFuture<ComparedData> getAsyncTasksForAttach(MoqueryEnumInterface moqueryFirstType,
																	MoqueryEnumInterface moquerySecondType,
																   ResponseVOFactory.ResponseType response,
																   Map<String, String> contractMap, TestCase testCase) {

		return CompletableFuture.supplyAsync(() -> {
			CompletableFuture<ComparedData> futureFromCht = queryChtForAttach(moqueryFirstType, moquerySecondType, response, contractMap,
					testCase);
			CompletableFuture<ComparedData> futureFromIISI = queryIISIForAttach(moqueryFirstType, moquerySecondType, response,
					contractMap, testCase);

			CompletableFuture<ComparedData>[] futureArray = new CompletableFuture[] { futureFromCht, futureFromIISI };
			CompletableFuture.allOf(futureArray).join();

			try {
				return mergeQuerys(futureFromCht.get(), futureFromIISI.get());
			} catch (InterruptedException | ExecutionException e) {
				ComparedData comparedData = factory.getComparedData((MoqueryEnumInterface) moquerySecondType, testCase);
				comparedData.setError(e.getMessage());
				return comparedData;
			}
		});
	}

	private CompletableFuture<ComparedData> queryChtForAttach(MoqueryEnumInterface firstQueryEnum,
															   MoqueryEnumInterface secondQueryEnum,
															  ResponseVOFactory.ResponseType response,
															  Map<String, String> contractMap, TestCase testCase) {
		TwoPhasedQueryToCht queryCht = new TwoPhasedQueryToCht(testCase, firstQueryEnum, secondQueryEnum,
				response);
		CompletableFuture<ComparedData> asyncQueryFromCht = queryCht.runTwoPhaseQuery(contractMap.get(VALUE_FROM_CHT));
		return asyncQueryFromCht;
	}

	private CompletableFuture<ComparedData> queryIISIForAttach(MoqueryEnumInterface firstQueryEnum,
																MoqueryEnumInterface secondQueryEnum,
															   ResponseVOFactory.ResponseType response,
															   Map<String, String> contractMap,
															   TestCase testCase) {

		TwoPhasedQueryToIISI queryIISI = new TwoPhasedQueryToIISI(testCase, firstQueryEnum, secondQueryEnum,
				response);
		CompletableFuture<ComparedData> asyncQueryFromIISI = queryIISI
				.runTwoPhaseQuery(contractMap.get(VALUE_FROM_IISI));
		return asyncQueryFromIISI;
	}


	private List<CompletableFuture<ComparedData>> asyncQueryOrderType(Map<String, String> contractMap,
			TestCase testCase) {

		List<CompletableFuture<ComparedData>> asyncRequest = new ArrayList<>();
		List<CompletableFuture<ComparedData>> asyncQueryForModelinsrec = Stream.of(enumsQueryForOrderNo)
				.map(enumType -> {
					return getAsyncTasksForOrderNo(enumType, MoqueryOrderNoType.Modelinsrec, contractMap, testCase);
				}).collect(Collectors.toList());

		List<CompletableFuture<ComparedData>> asyncQueryForModeldeliverdetail = Stream.of(enumsQueryForOrderNo)
				.map(enumType -> {
					return getAsyncTasksForOrderNo(enumType, MoqueryOrderNoType.Modeldeliverdetail, contractMap,
							testCase);
				}).collect(Collectors.toList());

		asyncRequest.addAll(asyncQueryForModelinsrec);
		asyncRequest.addAll(asyncQueryForModeldeliverdetail);
		return asyncRequest;
	}


	private CompletableFuture<ComparedData> getAsyncTasksForOrderNo(MoqueryEnumInterface enumType,
			MoqueryEnumInterface orderNoQueryEnum, Map<String, String> contractMap, TestCase testCase) {

		return CompletableFuture.supplyAsync(() -> {
			CompletableFuture<ComparedData> futureFromCht = queryChtForOrderNo(enumType, orderNoQueryEnum, contractMap,
					testCase);
			CompletableFuture<ComparedData> futureFromIISI = queryIISIForOrderNo(enumType, orderNoQueryEnum,
					contractMap, testCase);

			CompletableFuture<ComparedData>[] futureArray = new CompletableFuture[] { futureFromCht, futureFromIISI };
			CompletableFuture.allOf(futureArray).join();

			try {
				return mergeQuerys(futureFromCht.get(), futureFromIISI.get());
			} catch (InterruptedException | ExecutionException e) {
				ComparedData comparedData = factory.getComparedData((MoqueryEnumInterface) orderNoQueryEnum, testCase);
				comparedData.setError(e.getMessage());
				return comparedData;
			}
		});
	}

	private CompletableFuture<ComparedData> queryChtForOrderNo(MoqueryEnumInterface firstQueryEnum,
			MoqueryEnumInterface secondQueryEnum, Map<String, String> contractMap, TestCase testCase) {
		TwoPhasedQueryToCht queryCht = new TwoPhasedQueryToCht(testCase, firstQueryEnum, secondQueryEnum,
				ResponseType.OrderNo);
		CompletableFuture<ComparedData> asyncQueryFromCht = queryCht.runTwoPhaseQuery(contractMap.get(VALUE_FROM_CHT));
		return asyncQueryFromCht;
	}

	private CompletableFuture<ComparedData> queryIISIForOrderNo(MoqueryEnumInterface firstQueryEnum,
			MoqueryEnumInterface secondQueryEnum, Map<String, String> contractMap, TestCase testCase) {
		TwoPhasedQueryToIISI queryIISI = new TwoPhasedQueryToIISI(testCase, firstQueryEnum, secondQueryEnum,
				ResponseType.OrderNo);
		CompletableFuture<ComparedData> asyncQueryFromIISI = queryIISI
				.runTwoPhaseQuery(contractMap.get(VALUE_FROM_IISI));
		return asyncQueryFromIISI;
	}

	private List<CompletableFuture<ComparedData>> asyncQueryMospsvcType(Map<String, String> contractIdMap,
			TestCase testCase) {
		return Stream.of(enumsQueryForSpsvc).map(enumType -> {
			return getAsyncTasksForSpsvc(enumType, contractIdMap, testCase);
		}).collect(Collectors.toList());
	}

	private CompletableFuture<ComparedData> getAsyncTasksForSpsvc(MoqueryEnumInterface enumContractType,
			Map<String, String> contractIdMap, TestCase testCase) {
		return CompletableFuture.supplyAsync(() -> {
			// 查詢 table: specialsvc
			CompletableFuture<ComparedData> futureFromCht = runSpsvcQueryForCht(enumContractType, testCase,
					contractIdMap);
			CompletableFuture<ComparedData> futureFromIISI = runSpsvcQueryForIISI(enumContractType, testCase,
					contractIdMap);
			CompletableFuture<ComparedData>[] futureArray = new CompletableFuture[] { futureFromCht, futureFromIISI };
			CompletableFuture.allOf(futureArray).join();

			try {
				return mergeQuerys(futureFromCht.get(), futureFromIISI.get());
			} catch (InterruptedException | ExecutionException e) {
				ComparedData comparedData = null;
				switch ((MoqueryContractType) enumContractType) {
				case SpecsvcidMN:
					comparedData = factory.getComparedData((MoqueryEnumInterface) MoquerySpsvcType.Mdsvc,
							testCase);
					break;
				case SpecsvcidMV:
					comparedData = factory.getComparedData((MoqueryEnumInterface) MoquerySpsvcType.Vpnsvc,
							testCase);
					break;
				case SpecsvcidF3:
					comparedData = factory.getComparedData((MoqueryEnumInterface) MoquerySpsvcType.F3svc,
							testCase);
					break;
				}
				comparedData.setError(e.getMessage());
				return comparedData;
			}
		});
	}
	private CompletableFuture<ComparedData> runSpsvcQueryForIISI(MoqueryEnumInterface enumContractType,
			TestCase testCase, Map<String, String> contractIdMap) {
		TwoPhasedQueryToIISI queryIISI = null;
		switch ((MoqueryContractType) enumContractType) {
		case SpecsvcidMN:
			queryIISI = new TwoPhasedQueryToIISI(testCase, enumContractType,
					(MoqueryEnumInterface) MoquerySpsvcType.Mdsvc, ResponseType.SpsvcId);
			break;
		case SpecsvcidMV:
			queryIISI = new TwoPhasedQueryToIISI(testCase, enumContractType,
					(MoqueryEnumInterface) MoquerySpsvcType.Vpnsvc, ResponseType.SpsvcId);
			break;
		case SpecsvcidF3:
			queryIISI = new TwoPhasedQueryToIISI(testCase, enumContractType,
					(MoqueryEnumInterface) MoquerySpsvcType.F3svc, ResponseType.SpsvcId);
			break;
		}
//		if (enumContractType == MoqueryContractType.SpecsvcidMN) {
//			queryIISI = new TwoPhasedQueryToIISI(testCase, enumContractType,
//					(MoqueryEnumInterface) MoquerySpsvcType.Mdsvc, ResponseType.SpsvcId);
//		} else if (enumContractType == MoqueryContractType.SpecsvcidMV) {
//			queryIISI = new TwoPhasedQueryToIISI(testCase, enumContractType,
//					(MoqueryEnumInterface) MoquerySpsvcType.Vpnsvc, ResponseType.SpsvcId);
//		}
		return queryIISI.runTwoPhaseQuery(contractIdMap.get(VALUE_FROM_IISI));
	}

	private CompletableFuture<ComparedData> runSpsvcQueryForCht(MoqueryEnumInterface enumContractType,
			TestCase testCase, Map<String, String> contractIdMap) {
		TwoPhasedQueryToCht queryCht = null;
		switch ((MoqueryContractType) enumContractType) {
		case SpecsvcidMN:
			queryCht = new TwoPhasedQueryToCht(testCase, enumContractType,
					(MoqueryEnumInterface) MoquerySpsvcType.Mdsvc, ResponseType.SpsvcId);
			break;
		case SpecsvcidMV:
			queryCht = new TwoPhasedQueryToCht(testCase, enumContractType,
					(MoqueryEnumInterface) MoquerySpsvcType.Vpnsvc, ResponseType.SpsvcId);
			break;
		case SpecsvcidF3:
			queryCht = new TwoPhasedQueryToCht(testCase, enumContractType,
					(MoqueryEnumInterface) MoquerySpsvcType.F3svc, ResponseType.SpsvcId);
			break;
		}
//		if (enumContractType == MoqueryContractType.SpecsvcidMN) {
//			queryCht = new TwoPhasedQueryToCht(testCase, enumContractType,
//					(MoqueryEnumInterface) MoquerySpsvcType.Mdsvc, ResponseType.SpsvcId);
//		} else if (enumContractType == MoqueryContractType.SpecsvcidMV) {
//			queryCht = new TwoPhasedQueryToCht(testCase, enumContractType,
//					(MoqueryEnumInterface) MoquerySpsvcType.Vpnsvc, ResponseType.SpsvcId);
//		}
		return queryCht.runTwoPhaseQuery(contractIdMap.get(VALUE_FROM_CHT));
	}

	private List<CompletableFuture<ComparedData>> asyncQueryMocontractType(Map<String, String> contractMap,
			TestCase testCase) {
		List<CompletableFuture<ComparedData>> asyncQuerys = new ArrayList<>();
		MoqueryEnumInterface[][] totalOfQueryMocontractType = {enumsQueryWithContractId, enumsQueryWithContractIdWithTelnum, 
										  enumsQueryWithContractIdWithOneDate, enumsQueryWithContractIdWithTwoDate};
		
		for(MoqueryEnumInterface[] targetContractTypeArray : totalOfQueryMocontractType) {
			for(MoqueryEnumInterface contractType : targetContractTypeArray) {
				asyncQuerys.add(getAsyncQueryForContract(testCase, contractMap, contractType));
			}
		}
		
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
	
	private ComparedData showErrorMsgWhenCheckMoquery (ComparedData mergedComparedData) {
		if (StringUtils.isEmpty(mergedComparedData.getDataFromCht()) && StringUtils.isEmpty(mergedComparedData.getDataFromIISI())) {
			mergedComparedData.setError(ERRMSG_NO_NEED_TO_CHECK_WHEN_NO_DATA);
		} else {
			return mergedComparedData;
		}
		return mergedComparedData;
	}

	/*
	* @parma value = contractId
	* */
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

//	private List<ComparedData> getQueryListByTelnum(TestCase testCase) {
//		MoqueryEnumInterface[] asyncQueryList = new MoqueryTelnumType[] { MoqueryTelnumType.Agent5id,
//				MoqueryTelnumType.Delcustinfoapply, MoqueryTelnumType.Eformapplyrec };
//		return Stream.of(asyncQueryList).map(enumType -> factory.getComparedData(enumType, testCase))
//				.collect(Collectors.toList());
//	}

	private List<ComparedData> getQueryListByTelnum(TestCase testCase) {

		ArrayList<ComparedData> queryTelnumList = new ArrayList<>();

		for (MoqueryEnumInterface telnumType : enumsQueryWithTelnum) {
			queryTelnumList.add(factory.getComparedData(telnumType, testCase));
		}
		for (MoqueryEnumInterface telnumWithOneDateType : enumsQueryWithTelnumWithOneDate) {
			queryTelnumList.add(factory.getComparedData(telnumWithOneDateType, testCase));
		}
		for (MoqueryEnumInterface telnumWithTwoDateType : enumsQueryWithTelnumWithTwoDate) {
			queryTelnumList.add(factory.getComparedData(telnumWithTwoDateType, testCase));
		}

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
		};

		private void callTo(ComparedData comparedData) {
			queryCht(comparedData);
		}
	}

	protected void setValue(TestCase testCase, String value, MoqueryEnumInterface secondQueryEnum) {
		if (secondQueryEnum instanceof MoquerySpsvcType) {
			testCase.setSpsvc(value);
		} else if (secondQueryEnum instanceof MoqueryOrderNoType) {
			testCase.setOrderno(value);
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
		};

		private void callTo(ComparedData comparedData) {
			queryIISI(comparedData);
		}
	}
}
