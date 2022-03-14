//package com.project.minibacktesting_be.utils;
//
//import com.project.minibacktesting_be.dto.community.CommunityPortResponseDto;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//
//
//public class redisUtil {
//    @Autowired
//    static RedisTemplate<String, Object> redisTemplate;
//
//    public static void setCommunityPort(Long key, CommunityPortResponseDto responseDto){
//        // Redis Template
//        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
//        vop.set("communityPort"+key.toString(), responseDto);
//    }
//
//    public static CommunityPortResponseDto getCommunityPort(Long key){
//        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
//        CommunityPortResponseDto portResponseDto = (CommunityPortResponseDto) vop.get("communityPort"+key.toString());
//        return portResponseDto;
//    }
//}
