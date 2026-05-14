package com.briup.shop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;


//@SpringBootTest
class ShopApplicationTests {

    @Test
public void md5(){
    String s = DigestUtils.md5DigestAsHex("ShopRecommendation".getBytes());
    System.out.println("s = " + s);

}
}
