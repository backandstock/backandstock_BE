package com.project.minibacktesting_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

// DB에 데이터가 저장되거나 수정될 때 언제, 누가 했는지를 자동으로 관리
// TimeStamped null값 오류 제거하기위해 포함
@EnableJpaAuditing
@SpringBootApplication
@EnableCaching
public class MiniBacktestingBeApplication {
//    private static final String PROPERTIES =
//            "spring.config.location="

////            +"classpath:/application.properties"
////            +",classpath:/additional.properties";
//                    + "classpath:/additional.properties";

    @PostConstruct
    public void started() {
        // timezone UTC 셋팅
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public static void main(String[] args) {
//        new SpringApplicationBuilder(MiniBacktestingBeApplication.class)
//                .properties(PROPERTIES)
//                .run(args);
        SpringApplication.run(MiniBacktestingBeApplication.class, args);
    }

}

