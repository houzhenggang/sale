package com.hshc.sale.web.component;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 测试定时任务
 */
@Component
public class TestTask {


    /*@Scheduled(cron = "0 0/1 * * * ?")*/
    private void processCfca() throws Exception {
        System.out.println("=====================start=====================");


        System.out.println("=====================end=====================");
    }

}
