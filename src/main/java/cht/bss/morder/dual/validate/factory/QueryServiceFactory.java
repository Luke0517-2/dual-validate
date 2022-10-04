package cht.bss.morder.dual.validate.factory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import cht.bss.morder.dual.validate.enums.QueryServiceTypes;
import cht.bss.morder.dual.validate.service.query.QueryService;

@Component
public class QueryServiceFactory {

	private Map<QueryServiceTypes, QueryService> queryServices;

	@Autowired
	private ApplicationContext appContext;

	@PostConstruct
	private void postConstruct() {
		queryServices = new HashMap<>();
		Stream.of(QueryServiceTypes.values()).forEach(type -> {
			QueryService service = appContext.getBean(type.getServiceClass());
			queryServices.put(type, service);
		});
	};

	public Collection<QueryService> getQueryServiceList() {
		return queryServices.values();
	}

	public QueryService getQueryService(QueryServiceTypes type) {
		return queryServices.get(type);
	}

}
