package cht.bss.morder.dual.validate.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MoqueryTelnumWithDateType implements MoqueryEnumInterface {

    Adjustbill("adjustbill", "1", "%s&%s"),
    ModelinsrecShop("modelinsrec_shop", "1", "%s&%s"),
    Or13d0log("or13d0log", "1", "%s&%s"),
    Refund("refund", "1", "%s&%s"),
    Rfpaidlist("rfpaidlist", "1", "%s&%s"),
    Susptemp("susptemp", "1", "%s&%s"),
    Querylog("querylog", "1", "%s&%s"),  // 根據中華回應，Telrentnum可視為telnum
    ;

    private final String tableName;
    private final String type;
    private final String contentTemplate;

}
