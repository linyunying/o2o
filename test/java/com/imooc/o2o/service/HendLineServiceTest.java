package com.imooc.o2o.service;



import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.HeadLine;

public class HendLineServiceTest extends BaseTest {

	@Autowired
	HeadLineService headLineService;
	
	@Test
	public void testQueryArea(){
		List<HeadLine> headLineList=headLineService.getHeadLineList(new HeadLine());
		assertEquals(2,headLineList.size());
		
	}
}
