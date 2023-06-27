package cht.bss.morder.dual.validate.factory;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cht.bss.morder.dual.validate.common.DaysConvert;
import cht.bss.morder.dual.validate.common.exceptions.BusinessException;
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
	private DaysConvert yesterdayConvert;
	

	public ComparedData getComparedData(Enum type, TestCase testCase , MoqueryEnumInterface moquery) {		
		Class clazz = null;
		if (ObjectUtils.isNotEmpty(type)) {
			clazz = type.getDeclaringClass();			
		}
		if (QueryCustinfoType.class == clazz)
			return custInfoInputFactory.getComparedData((QueryCustinfoType) type, testCase);
		else if (QrySalebehaviorType.class == clazz) {
			return qrySaleBehaviorInputFactory.getComparedData(testCase);
		} else if (moquery instanceof MoqueryEnumInterface) {
			return moqueryInputFactory.getComparedData(moquery, testCase);
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
		return yesterdayConvert.getConvertADDate();
	}
	
	protected String getMinguoDate() {
		return yesterdayConvert.getConvertMinguoDate();
	}
	
}
