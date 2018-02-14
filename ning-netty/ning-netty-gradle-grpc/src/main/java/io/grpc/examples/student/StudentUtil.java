package io.grpc.examples.student;

import java.util.Random;

/**
 * Created by ning on 2018/2/12.
 * User:ning
 * Date:2018/2/12
 * tIME:17:08
 */
public class StudentUtil {
    public static StudentResponse buildRandomResponse(){
        Random random = new Random();
        return StudentResponse.newBuilder()
                .setName("user" + random.nextInt())
                .setAge(random.nextInt())
                .setCity("city" + random.nextInt())
                .setProvince("province" + random.nextInt())
                .build();

    }
    public static StudentRequest buildRandomRequest(){
        Random random = new Random();
        return StudentRequest.newBuilder()
                .setName("user" + random.nextInt())
                .setAge(random.nextInt())
                .setCity("city" + random.nextInt())
                .setProvince("province" + random.nextInt())
                .build();
    }
}
