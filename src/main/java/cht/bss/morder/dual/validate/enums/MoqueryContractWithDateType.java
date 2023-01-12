package cht.bss.morder.dual.validate.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MoqueryContractWithDateType implements MoqueryEnumInterface{

    Chgcustrec ("chgcustrec","1","%s&%s"),
    DataShareRecLog("data_share_rec_log","1","%s&%s"),
    Empbusiness("empbusiness","1","%s&%s"),
    Promofinereserve("promofinereserve","1","%s&%s"),
    Qosalert("qosalert","1","%s&%s"),
    Refundpaid("refundpaid","1","%s&%s"),
    Telsusptype("telsusptype","1","%s&%s"),
    Transcashfee1("transcashfee","1","%s&%s"),
    Transcashfee2("transcashfee","2","%s&%s"),


    ;

    private final String tableName;
    private final String type;
    private final String contentTemplate;

}
