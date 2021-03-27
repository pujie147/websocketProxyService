package com.vdegree.february.im.common.utils;

import com.google.common.collect.Lists;
import com.vdegree.february.im.common.constant.RoomType;

import java.util.stream.Collectors;


/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/27 16:36
 */
public class RoomIdGenerateUtil {
    public static String generate(Long userId1, Long userId2, RoomType roomType){
        return roomType.getRoomPrefix()+ generate(userId1,userId2);
    }

    private static String generate(Long userId1,Long userId2){
        return Lists.newArrayList(userId1.toString(),userId2.toString()).stream().sorted().collect(Collectors.joining("-"));
    }

    public static void main(String[] args) {
        System.out.println(RoomIdGenerateUtil.generate(34323L,34231L,RoomType.Video));
    }
}
