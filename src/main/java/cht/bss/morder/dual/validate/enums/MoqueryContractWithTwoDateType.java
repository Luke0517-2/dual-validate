package cht.bss.morder.dual.validate.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MoqueryContractWithTwoDateType implements MoqueryEnumInterface {

    Applytypechgrec("applytypechgrec", "1", "%s&%s&%s"),
    Delaydisc("delaydisc", "1", "%s&%s&%s"),
    Discnttype("discnttype", "1", "%s&%s&%s"),
    Packageservice("packageservice", "2", "%s&%s&%s"),
    Promoprodrecold("promoprodrecold", "1", "%s&%s&%s"),
    Promoprodreserve("promoprodreserve", "1", "%s&%s&%s"),
    Sharegroupdevice("sharegroupdevice","1","%s&%s&%s"),
    Vspecialsvc("vspecialsvc", "1", "%s&%s&%s"),
    Recashmark("recashmark", "1", "%s&%s&%s"),  // 根據中華回應 此處contract為contractid

    ;

    private final String tableName;
    private final String type;
    private final String contentTemplate;

}
