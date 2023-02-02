package cht.bss.morder.dual.validate.enums;

import cht.bss.morder.dual.validate.factory.ResponseVOFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MoqueryEnumForTwiceQuery {

    MoqueryRentCustNo(new MoqueryEnumInterface[]{MoqueryContractType.Contract}, MoqueryRentCustNoType.Pascustomer, ResponseVOFactory.ResponseType.RentcustNo),
    MoqueryTranscashId(new MoqueryEnumInterface[]{MoqueryContractWithDateType.Transcashfee1}, MoqueryTranscashIdType.Chargeitem, ResponseVOFactory.ResponseType.TranscashId),

    //  若要加orderno移進來的話
    MoqueryOrderno1(new MoqueryEnumInterface[]{MoqueryContractType.AgentMobileSet, MoqueryContractType.AgentMobileSetPart}, MoqueryOrderNoType.Modeldeliverdetail, ResponseVOFactory.ResponseType.OrderNo),
    MoqueryOrderno2(new MoqueryEnumInterface[]{MoqueryContractType.AgentMobileSet, MoqueryContractType.AgentMobileSetPart}, MoqueryOrderNoType.Modelinsrec, ResponseVOFactory.ResponseType.OrderNo),
    //  若要加SpsvcId移進來的話
    MoquerySpsvcMN(new MoqueryEnumInterface[]{MoqueryContractType.SpecsvcidMN}, MoquerySpsvcType.Mdsvc, ResponseVOFactory.ResponseType.SpsvcId),
    MoquerySpsvcMV(new MoqueryEnumInterface[]{MoqueryContractType.SpecsvcidMV}, MoquerySpsvcType.Vpnsvc, ResponseVOFactory.ResponseType.SpsvcId),
    MoquerySpsvcF3(new MoqueryEnumInterface[]{MoqueryContractType.SpecsvcidF3}, MoquerySpsvcType.F3svc, ResponseVOFactory.ResponseType.SpsvcId),
    ;


    private final MoqueryEnumInterface[] MoqueryEnumForFirstPhases;
    private final MoqueryEnumInterface MoqueryEnumSecondPhase;
    private final ResponseVOFactory.ResponseType response;


}
