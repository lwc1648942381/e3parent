package cn.e3mall.jedis;


import com.e3mall.common.jedis.JedisClient;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JedisClienTest {
    @Test
    public void jedisClient() throws  Exception{
//        初始化spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
        JedisClient jedis = applicationContext.getBean(JedisClient.class);
        jedis.set("mytest","jedisclient");
        String mytest = jedis.get("mytest");
        System.out.println(mytest);
    }
}
