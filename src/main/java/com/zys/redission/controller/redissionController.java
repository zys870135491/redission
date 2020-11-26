package com.zys.redission.controller;

import io.lettuce.core.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RFuture;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "redission")
@Slf4j
public class redissionController {

    @Autowired
    private RedissonClient redissonClient;

    private Integer count = 50;

    @GetMapping(value = "getLock")
    public String getLock() throws InterruptedException {
        RLock lock = redissonClient.getLock("lock");

        // 公平锁
        try {
            // 尝试加锁，最多等待10秒，上锁以后2秒自动解锁
            boolean res = lock.tryLock(10, 2, TimeUnit.SECONDS);
            if (res) {

                //log.info(Thread.currentThread().getName()+"进入业务代码："+new Date());
                count = count - 1;
                Thread.sleep(1000);
                log.info(Thread.currentThread().getName() + "业务代码结束：" + count);
                return "获得了锁:" + count;
            } else {
                log.info(Thread.currentThread().getName() + "获得锁失败");
                return "获得锁失败";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "获得锁失败，出现异常";
        } finally {
            log.info("释放了锁");
            //lock.isHeldByCurrentThread()，它的意思是查询当前线程是否持有此锁定，如果还持有，则释放，如果未持有，则说明已被释放
            if (lock != null && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }
}
