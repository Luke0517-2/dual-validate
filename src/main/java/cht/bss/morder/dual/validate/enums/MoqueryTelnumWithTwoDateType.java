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
    Specsvcmember("specsvcmember", "1", "%s&%s&%s"),
    Sharegroupmem("sharegroupmem", "1", "%s&%s&%s");


    private final String tableName;
    private final String type;
    private final String contentTemplate;

}
