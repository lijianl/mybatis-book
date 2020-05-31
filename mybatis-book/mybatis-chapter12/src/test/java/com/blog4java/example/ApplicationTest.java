package com.blog4java.example;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
// 使用环境
@ActiveProfiles("dev")
public class ApplicationTest {

    // mvc用来 mock HTTP接口的操作
    protected MockMvc mockMvc;

    @Resource
    private WebApplicationContext applicationContext;

    @Before
    public void init() {
        // init
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
    }

}