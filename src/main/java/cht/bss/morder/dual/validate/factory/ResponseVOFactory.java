package cht.bss.morder.dual.validate.factory;

import cht.bss.morder.dual.validate.common.exceptions.BusinessException;
import cht.bss.morder.dual.validate.vo.json.*;

public class ResponseVOFactory {

	public enum ResponseType {
		Contract, OrderNo, SpsvcId, RentcustNo, TranscashId
	}

	public static AbstractJSONPathModel getResponseModel(ResponseType type, String returnStr) {
		if (type == ResponseType.Contract) {
			return ContractIDResponseVO.builder(returnStr);
		} else if (type == ResponseType.OrderNo) {
			return OrderNoResponseVO.builder(returnStr);
		} else if (type == ResponseType.SpsvcId) {
			return SpecsvcIDResponseVO.builder(returnStr);
		} else if (type == ResponseType.RentcustNo) {
			return RentcustNoResponseVO.builder(returnStr);
		} else if (type == ResponseType.TranscashId) {
			return TranscashIdResponseVO.builder(returnStr);
		} else {
			throw new BusinessException("未配置");
		}
	}

}
