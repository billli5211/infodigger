package com.bill.crawler.rank;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseCriteria implements ICriteria {

	protected ICriteria decoratedCriteria;

	protected ArrayList<String> criteriaList;

	public BaseCriteria(ICriteria decoratedCriteria) {
		this.decoratedCriteria = decoratedCriteria;
	}

	@Override
	public void setCriteria(List<String> criterias) {
		criteriaList = (ArrayList<String>) criterias;
	}

	@Override
	public int meetCriteria(String content) {
		// TODO Auto-generated method stub
		return 0;
	}

}
