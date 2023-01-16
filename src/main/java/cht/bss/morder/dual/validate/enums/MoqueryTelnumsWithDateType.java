package cht.bss.morder.dual.validate.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MoqueryTelnumsWithDateType implements MoqueryEnumInterface {

    Workingrecord("workingrecord", "1", "%s&%s&%s"), // 根據中華回應，門號都是給受理的行動門號
    ;

    private final String tableName;
    private final String type;
    private final String contentTemplate;

}
