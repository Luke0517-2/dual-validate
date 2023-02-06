package cht.bss.morder.dual.validate.factory;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cht.bss.morder.dual.validate.common.YesterdayConvert;
import cht.bss.morder.dual.validate.common.exceptions.BusinessException;
import cht.bss.morder.dual.validate.config.CheckQueryRuleProperties;
import cht.bss.morder.dual.validate.config.TransferProperties;
import cht.bss.morder.dual.validate.enums.MoqueryEnumInterface;
import cht.bss.morder.dual.validate.enums.QrySalebehaviorType;
import cht.bss.morder.dual.validate.enums.QueryCustinfoType;
import cht.bss.morder.dual.validate.vo.ComparedData;
import cht.bss.morder.dual.validate.vo.ComparedData.ComparedDataBuilder;
import cht.bss.morder.dual.validate.vo.QueryInput;
import cht.bss.morder.dual.validate.vo.TestCase;

@Service
public class QueryInputFactory {

	@Autowired
	private QueryCustInfoInputFactory custInfoInputFactory;

	@Autowired
	private QrySaleBehaviorInputFactory qrySaleBehaviorInputFactory;

	@Autowired
	private MoqueryInputFactory moqueryInputFactory;
	
	@Autowired
	private TransferProperties properties;
	
	@Autowired
	private YesterdayConvert yesterdayConvert;
	

	public ComparedData getComparedData(Enum type, TestCase testCase) {
		Class clazz = type.getDeclaringClass();
		if (QueryCustinfoType.class == clazz)
			return custInfoInputFactory.getComparedData((QueryCustinfoType) type, testCase);
		else if (QrySalebehaviorType.class == clazz) {
			return qrySaleBehaviorInputFactory.getComparedData(testCase);
		} else if (type instanceof MoqueryEnumInterface) {
			return moqueryInputFactory.getComparedData((MoqueryEnumInterface)type, testCase);
		} else {
			throw new BusinessException("未設定查找類型");
		}
	}

	protected QueryInput buildBasicInput() {
		return QueryInput.builder()
				.empno(properties.getMOrder().getApigwProperties().getEmpNo())
				.fromSite(properties.getMOrder().getApigwProperties().getFromSite())
				.clientip(properties.getMOrder().getApigwProperties().getClientIp())
				.build();
	}

	protected ComparedDataBuilder builder(final String queryService) {
		ComparedDataBuilder builder = ComparedData.builder().queryService(queryService);
		return builder;
	}
	
	protected String getADDate() {
		return yesterdayConvert.getYesterdayADDateString();
	}
	
	protected String getMinguoDate() {
		return yesterdayConvert.getYesterdayMinguoDate();
	}
	
}
