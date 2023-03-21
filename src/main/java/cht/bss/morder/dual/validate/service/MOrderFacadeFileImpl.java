package cht.bss.morder.dual.validate.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import cht.bss.morder.dual.validate.common.exceptions.BusinessException;
import cht.bss.morder.dual.validate.vo.QueryInput;

@Service
@ConditionalOnProperty(prefix = "dual-validate", name = "mode", havingValue = "FILE")
public class MOrderFacadeFileImpl implements MOrderFacade {

	@Autowired
	private ObjectMapper mapper;

	@Override
	public String queryIISI(QueryInput queryInput) {
//		try {
//			return mapper.writeValueAsString(queryInput);
//		} catch (JsonProcessingException e) {
//			return "系統發生錯誤";
//		}
		return responseFromQuery(queryInput);
	}

	@Override
	public String queryCht(QueryInput queryInput) {
//		try {
//			return mapper.writeValueAsString(queryInput);
//		} catch (JsonProcessingException e) {
//			return "系統發生錯誤";
//		}
		return responseFromQuery(queryInput);
	}

	private String responseFromQuery(QueryInput queryInput){

		String type = queryInput.getCmd();
		if(type.equals("qrysalebehavior")){
			return responseFromQrysalebehavior(queryInput);
		}else if(type.equals("QueryCustInfo")) {
			return responseFromQueryCustInfo(queryInput);
		}else if(type.equals("moquery")) {
			return responseFromMoquery(queryInput);
		}else {
			return "Input-cmd不正確";
		}
	}

	private String responseFromQrysalebehavior(QueryInput input){
		String result = StringUtils.EMPTY;
		if(StringUtils.isNoneBlank(input.getParam().getTelnum())) {			
			result = readFile("./jsonsample/agentmobset_output.json");
		}else {
			throw new BusinessException("Input參數不正確");
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(result);
		stringBuilder.append(System.currentTimeMillis());
		return stringBuilder.toString();
	}

	private String responseFromQueryCustInfo(QueryInput input) {
		String result = StringUtils.EMPTY;
		if(StringUtils.isNoneEmpty(input.getParam().getTelnum())) {
			result = readFile("./jsonsample/querycustInfo_output.json");
		}else if(StringUtils.isNoneEmpty(input.getParam().getCustid()) && StringUtils.equals("custbehavior;", input.getParam().getQuerydata())) {
			result = readFile("./jsonsample/custbehavior_output.json");
		}else {
			throw new BusinessException("Input參數不正確");
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(result);
		stringBuilder.append(System.currentTimeMillis());
		return stringBuilder.toString();
	}

	private String responseFromMoquery(QueryInput input) {
		String tablename = input.getParam().getQueryitem().getTablename();
		String result = StringUtils.EMPTY;
		switch(tablename) {
			case "numberusage" : 
				result = readFile("./jsonsample/numberusage_output.json");
				break;
			case "agentmobset" :
				result = readFile("./jsonsample/agentmobileset_output.json");
				break;
			case "specialsvc" :
				System.out.println();
				if("MN".equals(input.getParam().getQueryitem().getContent().split("&")[1]))
					result = readFile("./jsonsample/specsvcidmn_output.json");
				else if("MV".equals(input.getParam().getQueryitem().getContent().split("&")[1]))
					result = readFile("./jsonsample/specsvcidmv_output.json");
				else if("F3".equals(input.getParam().getQueryitem().getContent().split("&")[1]))
					result = readFile("./jsonsample/specsvcidf3_output.json");
				break;
			case "agent5id" :
				result = readFile("./jsonsample/agent5id_output.json");
				break;
			case "delcustinfoapply" :
				result = readFile("./jsonsample/delcustinfoapply_output.json");
				break;
			case "eformapplyrec" :
				result = readFile("./jsonsample/eformapplyrec_output.json");
				break;
			case "contractret" :
				result = readFile("./jsonsample/contractret_output.json");
				break;
			case "pasuserec" :
				result = readFile("./jsonsample/pasuserec_output.json");
				break;
			case "projmember" :
				result = readFile("./jsonsample/projmember_output.json");
				break;
			case "agentmobsetpart" :
				result = readFile("./jsonsample/emptydatalist_output.json");
				break;
			case "modeldeliverdetail" :
				result = readFile("./jsonsample/modeldeliverdetail_output.json");
				break;
			case "modelinsrec" :
				result = readFile("./jsonsample/modelinsrec_output.json");
				break;
			case "suspresumerec" :
				if("1".equals(input.getParam().getQueryitem().getQuerytype()))
					result = readFile("./jsonsample/suspresumerec1_output.json");
				else if("2".equals(input.getParam().getQueryitem().getQuerytype()))
					result = readFile("./jsonsample/suspresumerec2_output.json");
				break;
			case "mdsvc" :
				result = readFile("./jsonsample/mdsvc_output.json");
				break;
			case "vpnsvc" :
				result = readFile("./jsonsample/vpnsvc_output.json");
				break;
			case "sponsorspsvc" :
				result = readFile("./jsonsample/sponsorspsvc_output.json");
				break;
			case "datashareinfo" :
				result = readFile("./jsonsample/datashareinfo_output.json");
				break;
			case "data_share_rec" :
				result = readFile("./jsonsample/data_share_rec_output.json");
				break;
			case "adjustbill":
				result = readFile("./jsonsample/adjustbill_output.json");
				break;
			case "applytypechgrec":
				result = readFile("./jsonsample/applytypechgrec_output.json");
				break;
			case "chargeitem":
				result = readFile("./jsonsample/chargeitem_output.json");
				break;
			case "chgcustrec":
				result = readFile("./jsonsample/chgcustrec_output.json");
				break;
			case "custdatainfo":
				result = readFile("./jsonsample/custdatainfo_output.json");
				break;
			case "data_share_rec_log":
				result = readFile("./jsonsample/datasharereclog_output.json");
				break;
			case "deductfee":
				result = readFile("./jsonsample/deductfee_output.json");
				break;
			case "delaydisc":
				result = readFile("./jsonsample/delaydisc_output.json");
				break;
			case "discnttype":
				result = readFile("./jsonsample/discnttype_output.json");
				break;
			case "einvoicerec":
				result = readFile("./jsonsample/einvoicerec_output.json");
				break;
			case "empbusiness":
				result = readFile("./jsonsample/empbusiness_output.json");
				break;
			case "empdiscntrec":
				result = readFile("./jsonsample/empdiscntrec_output.json");
				break;
			case "f3svc":
				result = readFile("./jsonsample/f3svc_output.json");
				break;
			case "f4svc":
				result = readFile("./jsonsample/f4svc_output.json");
				break;
			case "familysvc":
				result = readFile("./jsonsample/familysvc_output.json");
				break;
			case "modelinsrec_shop":
				result = readFile("./jsonsample/modelinsrecshop_output.json");
				break;
			case "newdiscntreserve":
				result = readFile("./jsonsample/newdiscntreserve_output.json");
				break;
			case "officialfee":
				result = readFile("./jsonsample/officialfee_output.json");
				break;
			case "or13d0log":
				result = readFile("./jsonsample/or13d0log_output.json");
				break;
			case "packageservice":
				result = readFile("./jsonsample/packageservice_output.json");
				break;
			case "pascustomer":
				result = readFile("./jsonsample/pascustomer_output.json");
				break;
			case "prepaidsvc":
				result = readFile("./jsonsample/prepaidsvc_output.json");
				break;
			case "promofinereserve":
				result = readFile("./jsonsample/promofinereserve_output.json");
				break;
			case "promoprodrecold":
				result = readFile("./jsonsample/promoprodrecold_output.json");
				break;
			case "promoprodreserve":
				result = readFile("./jsonsample/promoprodreserve_output.json");
				break;
			case "qosalert":
				result = readFile("./jsonsample/qosalert_output.json");
				break;
			case "querylog":
				result = readFile("./jsonsample/querylog_output.json");
				break;
			case "recashmark":
				result = readFile("./jsonsample/recashmark_output.json");
				break;
			case "recotemp":
				result = readFile("./jsonsample/recotemp_output.json");
				break;
			case "refund":
				result = readFile("./jsonsample/refund_output.json");
				break;
			case "refundpaid":
				result = readFile("./jsonsample/refundpaid_output.json");
				break;
			case "rfpaidlist":
				result = readFile("./jsonsample/rfpaidlist_output.json");
				break;
			case "sernumusage":
				result = readFile("./jsonsample/sernumusage_output.json");
				break;
			case "sharegroupdevice":
				result = readFile("./jsonsample/sharegroupdevice_output.json");
				break;
			case "sharegroupmem":
				result = readFile("./jsonsample/sharegroupmem_output.json");
				break;
			case "specsvcmember":
				result = readFile("./jsonsample/specsvcmember_output.json");
				break;
			case "subapplytype":
				result = readFile("./jsonsample/subapplytype_output.json");
				break;
			case "susptemp":
				result = readFile("./jsonsample/susptemp_output.json");
				break;
			case "telsusptype":
				result = readFile("./jsonsample/telsusptype_output.json");
				break;
			case "transcashfee":
				if("1".equals(input.getParam().getQueryitem().getQuerytype()))
					result = readFile("./jsonsample/transcashfee1_output.json");
				else if("2".equals(input.getParam().getQueryitem().getQuerytype()))
					result = readFile("./jsonsample/transcashfee2_output.json");
				break;
			case "vspecialsvc":
				result = readFile("./jsonsample/vspecialsvc_output.json");
				break;
			case "workingrecord":
				result = readFile("./jsonsample/workingrecord_output.json");
				break;
			case "contract":
				result = readFile("./jsonsample/contract_output.json");
				break;
		}
		if(StringUtils.isNotBlank(result)) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(result);
			stringBuilder.append(System.currentTimeMillis());
			return stringBuilder.toString();
		}
		else
			throw new BusinessException("Input有誤");
	}	
	
	String readFile(String path) {
		String result = StringUtils.EMPTY;
		try {
			result = FileUtils.readFileToString(new File(path),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
