package cht.bss.morder.dual.validate.common;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CheckQueryTable {

	@Value("#{${dual-validate-query-table-list}}")
	private List<String> listOfQueryTable;

	public boolean filterQueryTable(String targetTableName) {

		List<String> checkList = listOfQueryTable.stream()
				.filter(checkTableName -> checkTableName.equals(targetTableName)).collect(Collectors.toList());

		if (checkList.isEmpty())
			return false;
		else
			return true;
	}
}
