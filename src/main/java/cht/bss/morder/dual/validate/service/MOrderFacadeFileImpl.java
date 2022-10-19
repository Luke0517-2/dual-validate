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
		return result;
	}

	private String responseFromQueryCustInfo(QueryInput input) {
		String result = StringUtils.EMPTY;
		if(StringUtils.isNoneEmpty(input.getParam().getTelnum())) {
			result = readFile("./jsonsample/queryCustInfo_output.json");
		}else if(StringUtils.isNoneEmpty(input.getParam().getCustid()) && StringUtils.equals("custbehavior;", input.getParam().getQuerydata())) {
			result = readFile("./jsonsample/custbehavior_output.json");
		}else {
			throw new BusinessException("Input參數不正確");
		}
		return result;
	}

	private String responseFromMoquery(QueryInput input) {
		String tablename = input.getParam().getQueryitem().getTablename();
		String result = StringUtils.EMPTY;
		switch(tablename) {
			case "numberusage" : 
				result = readFile("./jsonsample/numberusage_output.json");
				break;
			case "agentmobset" :
				result = readFile("./jsonsample/agentmobset_output.json");
				break;
			case "specialsvc" :
				result = readFile("./jsonsample/specialsvc_output.json");
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
				if(input.getParam().getQueryitem().getQuerytype() == "1")
					result = readFile("./jsonsample/suspresumerec1_output.json");
				else if(input.getParam().getQueryitem().getQuerytype() == "2")
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
		}
		if(StringUtils.isNotBlank(result))
			return result;
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
