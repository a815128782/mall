package com.changgou.test;

import com.changgou.common.util.IdWorker;

public class IdTest {

    public static void main(String[] args) {

        IdWorker idWorker = new IdWorker(1,1);

        // 可以使用循环方式获取大量id
        for (int i = 0; i < 100; i++) {
            long id = idWorker.nextId();
            System.out.println(id); //1207620191618469888
            // id 第一个随意然后自增
        }
    }
}
