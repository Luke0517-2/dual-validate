package cht.bss.morder.dual.validate.enums;

import cht.bss.morder.dual.validate.factory.ResponseVOFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
/*
 * 這邊implement是為了在QueryMapping可以放入MoqueryEnumInterface的list內
 * */
public enum MoqueryEnumForTwiceQuery implements MoqueryEnumInterface {

    MoqueryRentCustNo(new MoqueryEnumInterface[]{MoqueryContractType.Contract}, MoqueryRentCustNoType.Pascustomer, ResponseVOFactory.ResponseType.RentcustNo),
    MoqueryTranscashId(new MoqueryEnumInterface[]{MoqueryContractWithDateType.Transcashfee1}, MoqueryTranscashIdType.Chargeitem, ResponseVOFactory.ResponseType.TranscashId),
    MoqueryOrderno1(new MoqueryEnumInterface[]{MoqueryContractType.AgentMobileSet, MoqueryContractType.AgentMobileSetPart}, MoqueryOrderNoType.Modeldeliverdetail, ResponseVOFactory.ResponseType.OrderNo),
    MoqueryOrderno2(new MoqueryEnumInterface[]{MoqueryContractType.AgentMobileSet, MoqueryContractType.AgentMobileSetPart}, MoqueryOrderNoType.Modelinsrec, ResponseVOFactory.ResponseType.OrderNo),
    MoquerySpsvcMN(new MoqueryEnumInterface[]{MoqueryContractType.SpecsvcidMN}, MoquerySpsvcType.Mdsvc, ResponseVOFactory.ResponseType.SpsvcId),
    MoquerySpsvcMV(new MoqueryEnumInterface[]{MoqueryContractType.SpecsvcidMV}, MoquerySpsvcType.Vpnsvc, ResponseVOFactory.ResponseType.SpsvcId),
    MoquerySpsvcF3(new MoqueryEnumInterface[]{MoqueryContractType.SpecsvcidF3}, MoquerySpsvcType.F3svc, ResponseVOFactory.ResponseType.SpsvcId),
    ;


    private final MoqueryEnumInterface[] MoqueryEnumForFirstPhases;
    private final MoqueryEnumInterface MoqueryEnumSecondPhase;
    private final ResponseVOFactory.ResponseType response;


    //以下方法沒用到，因為要implement，只好實作這些方法。
    @Override
    public String getTableName() {
        return null;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public String getContentTemplate() {
        return null;
    }
}
