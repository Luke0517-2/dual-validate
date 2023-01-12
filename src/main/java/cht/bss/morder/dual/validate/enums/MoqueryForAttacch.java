package cht.bss.morder.dual.validate.enums;

import cht.bss.morder.dual.validate.factory.ResponseVOFactory;
import cht.bss.morder.dual.validate.vo.json.AbstractJSONPathModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MoqueryForAttacch {

    MoqueryRentCustNo (new MoqueryEnumInterface[]{MoqueryContractType.Contract},new MoqueryEnumInterface[]{MoqueryRentCustNoType.Pascustomer},ResponseVOFactory.ResponseType.RentcustNo),
    MoqueryTranscashId (new MoqueryEnumInterface[]{MoqueryContractWithDateType.Transcashfee1},new MoqueryEnumInterface[]{MoqueryTranscashIdType.Chargeitem},ResponseVOFactory.ResponseType.TranscashId),
    ;


    private final MoqueryEnumInterface[]  MoqueryEnumFirstInterface;
    private final MoqueryEnumInterface[]  MoqueryEnumSecondInterface;
    private final ResponseVOFactory.ResponseType response;



}
