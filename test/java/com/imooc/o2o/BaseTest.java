package com.imooc.o2o;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Junit测试套件
 * 1.测试套件就是将所有的测试类以数组的形式进行集合测试
 * 这样就不用一个个测试类去测试
 * 2.测试套件是一个空类，不能含有任何测试方法
 * 3.测试套件也可以包含其他套件测试
 * 
 * 配置spring和Junit整合，Junit启动时加载springIOC容器
 * @author Administrator
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉JUnit，spring配置文件的位置
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml","classpath:spring/spring-redis.xml"})
public class BaseTest {

}
