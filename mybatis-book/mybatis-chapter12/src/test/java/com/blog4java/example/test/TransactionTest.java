package com.blog4java.example.test;

import com.blog4java.example.ApplicationTest;
import com.blog4java.example.entity.User;
import com.blog4java.example.service.UserService;
import org.junit.Test;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

public class TransactionTest extends ApplicationTest {


    // 测试事务
    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private UserService userService;

    @Test
    public void testTrans() {
        // 编程式事务操作
        transactionTemplate.execute(new TransactionCallback<Integer>() {
            @Override
            public Integer doInTransaction(TransactionStatus status) {
                User user = buildUser();
                userService.userRegister(user);
                System.out.println("===>" + status.toString());
                return 0;
            }
        });
    }

    private User buildUser() {
        User user = new User();
        user.setName("aaa");
        user.setNickName("aaa");
        user.setPassword("***");
        user.setPhone("111");
        return user;
    }
}
