package com.imooc.o2o.service;

import java.util.List;

import com.imooc.o2o.entity.HeadLine;

public interface HeadLineService {

	public  static final String HLLISTKEY="headlinelist";
	
	List<HeadLine> getHeadLineList(HeadLine headLine);
}
