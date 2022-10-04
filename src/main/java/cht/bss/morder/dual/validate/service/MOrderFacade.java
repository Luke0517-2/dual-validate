package cht.bss.morder.dual.validate.service;

import cht.bss.morder.dual.validate.vo.QueryInput;
import cht.bss.morder.dual.validate.vo.QueryItem;

public interface MOrderFacade {
	String queryIISI(QueryInput queryInput);
	String queryCht(QueryInput queryInput);
}
