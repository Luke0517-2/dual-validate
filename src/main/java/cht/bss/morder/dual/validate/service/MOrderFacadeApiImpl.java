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
import cht.bss.morder.dual.validate.service.client.ApiClient;
import cht.bss.morder.dual.validate.vo.QueryInput;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "dual-validate", name = "mode", havingValue = "API")
public class MOrderFacadeApiImpl implements MOrderFacade {

	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private ApiClient apiClient;

	@Override
	public String queryIISI(QueryInput queryInput) {
		log.error("API++++++++");
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
			//TODO 需改成call api
			result = readFile("./jsonsample/agentmobset_output.json");
		}else {
			throw new BusinessException("Input參數不正確");
		}
		return result;
	}

	private String responseFromQueryCustInfo(QueryInput input) {
		String result = StringUtils.EMPTY;
		if(StringUtils.isNoneEmpty(input.getParam().getTelnum())) {
			//TODO 需改成call api
			result = readFile("./jsonsample/queryCustInfo_output.json");
			result = apiClient.queryCustInfo(input);
		}else if(StringUtils.isNoneEmpty(input.getParam().getCustid()) && StringUtils.equals("custbehavior;", input.getParam().getQuerydata())) {
			//TODO 需改成call api
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
				//TODO 需改成call api
				result = readFile("./jsonsample/numberusage_output.json");
				break;
			case "agentmobset" :
				//TODO 需改成call api
				result = readFile("./jsonsample/agentmobset_output.json");
				break;
			case "specialsvc" :
				//TODO 需改成call api
				result = readFile("./jsonsample/specialsvc_output.json");
				break;
			case "agent5id" :
				//TODO 需改成call api
				result = readFile("./jsonsample/agent5id_output.json");
				break;
			case "delcustinfoapply" :
				//TODO 需改成call api
				result = readFile("./jsonsample/delcustinfoapply_output.json");
				break;
			case "eformapplyrec" :
				//TODO 需改成call api
				result = readFile("./jsonsample/eformapplyrec_output.json");
				break;
			case "contractret" :
				//TODO 需改成call api
				result = readFile("./jsonsample/contractret_output.json");
				break;
			case "pasuserec" :
				//TODO 需改成call api
				result = readFile("./jsonsample/pasuserec_output.json");
				break;
			case "projmember" :
				//TODO 需改成call api
				result = readFile("./jsonsample/projmember_output.json");
				break;
			case "agentmobsetpart" :
				//TODO 需改成call api
				result = readFile("./jsonsample/emptydatalist_output.json");
				break;
			case "modeldeliverdetail" :
				//TODO 需改成call api
				result = readFile("./jsonsample/modeldeliverdetail_output.json");
				break;
			case "modelinsrec" :
				//TODO 需改成call api
				result = readFile("./jsonsample/modelinsrec_output.json");
				break;
			case "suspresumerec" :
				if(input.getParam().getQueryitem().getQuerytype() == "1")
					//TODO 需改成call api
					result = readFile("./jsonsample/suspresumerec1_output.json");
				else if(input.getParam().getQueryitem().getQuerytype() == "2")
					//TODO 需改成call api
					result = readFile("./jsonsample/suspresumerec2_output.json");
				break;
			case "mdsvc" :
				//TODO 需改成call api
				result = readFile("./jsonsample/mdsvc_output.json");
				break;
			case "vpnsvc" :
				//TODO 需改成call api
				result = readFile("./jsonsample/vpnsvc_output.json");
				break;
			case "sponsorspsvc" :
				//TODO 需改成call api
				result = readFile("./jsonsample/sponsorspsvc_output.json");
				break;
			case "datashareinfo" :
				//TODO 需改成call api
				result = readFile("./jsonsample/datashareinfo_output.json");
				break;
			case "data_share_rec" :
				//TODO 需改成call api
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
