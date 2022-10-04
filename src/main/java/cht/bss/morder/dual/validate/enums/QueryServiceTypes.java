package cht.bss.morder.dual.validate.enums;

import cht.bss.morder.dual.validate.service.query.MoqueryService;
import cht.bss.morder.dual.validate.service.query.QrysalebehaviorService;
import cht.bss.morder.dual.validate.service.query.QueryCustInfoService;
import cht.bss.morder.dual.validate.service.query.QueryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum QueryServiceTypes {

	Moquery(MoqueryService.class), 
	Qrysalebehavior(QrysalebehaviorService.class),
	QueryCustInfo(QueryCustInfoService.class);

	private final Class<? extends QueryService> serviceClass;
}
