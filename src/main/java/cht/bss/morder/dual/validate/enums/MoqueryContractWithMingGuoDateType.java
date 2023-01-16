package cht.bss.morder.dual.validate.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MoqueryContractWithMingGuoDateType implements MoqueryEnumInterface {

    Officialfee("officialfee", "1", "%s&%s");

    private final String tableName;
    private final String type;
    private final String contentTemplate;
}
