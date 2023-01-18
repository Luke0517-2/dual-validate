package cht.bss.morder.dual.validate.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MoqueryTelnumWithTwoDateType implements MoqueryEnumInterface {

    Custdatainfo("custdatainfo", "1", "%s&%s&%s"),
    Empdiscntrec("empdiscntrec", "1", "%s&%s&%s"),
    F4svc("f4svc", "1", "%s&%s&%s"),
    Familysvc("familysvc", "2", "%s&%s&%s"),
    Sharegroupdevice("sharegroupdevice","1","%s&%s&%s"),
    Specsvcmember("sharegroupmem", "1", "%s&%s&%s"),
    Prepaidsvc("prepaidsvc", "1", "%s&%s&%s"), // 根據中華回應 Leadertelnum可視為telnum
    Sernumusage("sernumusage", "1", "%s&%s&%s") //根據中華回應 sernum 為 telnum
    ;

    private final String tableName;
    private final String type;
    private final String contentTemplate;

}
