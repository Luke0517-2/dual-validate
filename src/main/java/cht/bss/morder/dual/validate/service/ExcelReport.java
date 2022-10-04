package cht.bss.morder.dual.validate.service;

import java.util.List;

import cht.bss.morder.dual.validate.vo.TestCase;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class ExcelReport {

	private String[] titles;
	private List<TestCase> cases;
	
}
