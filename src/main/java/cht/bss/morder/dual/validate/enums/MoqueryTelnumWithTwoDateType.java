package cht.bss.morder.dual.validate.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MoqueryTelnumWithTwoDateType implements MoqueryEnumInterface{

    Adjustbill ("adjustbill","1","%s&%s"),
    ModelinsrecShop("modelinsrec_shop","1","%s&%s"),
    Or13d0log("or13d0log","1","%s&%s"),
    Refund("refund","1","%s&%s"),
    Rfpaidlist("rfpaidlist","1","%s&%s"),
    Susptemp("susptemp","1","%s&%s"),

    Custdatainfo ("custdatainfo","1","%s&%s&%s"),
    Empdiscntrec ("empdiscntrec","1","%s&%s&%s"),
    F4svc ("f4svc","1","%s&%s&%s"),
    Familysvc ("familysvc","2","%s&%s&%s"),
    Specsvcmember ("specsvcmember","1","%s&%s&%s"),
    Sharegroupmem ("sharegroupmem","1","%s&%s&%s")

    ;


    private final String tableName;
    private final String type;
    private final String contentTemplate;

}
