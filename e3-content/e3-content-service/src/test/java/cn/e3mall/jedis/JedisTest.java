package cn.e3mall.jedis;

import com.sun.javadoc.SeeTag;
import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

public class JedisTest {

    @Test
    public void testJedis() throws Exception{
        //创建一个连接jedis对象,参数: host,port
        Jedis jedis = new Jedis("192.168.25.161",6379);
        //直接使用jedis操作redis，所有jedis的命令都对应一个方法
        jedis.set("test123","my first jedis test");
        String test123 = jedis.get("test123");
        System.out.println(test123);
        //关闭连接
        jedis.close();
    }

    @Test
    public void testJedisPool() throws Exception{
        //创建一个连接池对象,两个参数host,port
        JedisPool jedisPool = new JedisPool("192.168.25.161",6379);
        //从连接池获得一个连接，就是一个jedis对象
        Jedis resource = jedisPool.getResource();
        //使用jedis操作redis
        String test123 = resource.get("test123");
        System.out.println(test123);
        //关闭连接，每次使用完关闭连接，连接池回收资源
        resource.close();
        //关闭连接池,
        jedisPool.close();
    }


    @Test
    public void testJedisCluster() throws Exception{
        //创建一个jedisCluster对象，有一个参数nodes是一个set类型，nodes包含若干个host port对象。
        Set<HostAndPort> nodes=new HashSet<>();
        nodes.add(new HostAndPort("192.168.25.161",7001));
        nodes.add(new HostAndPort("192.168.25.161",7002));
        nodes.add(new HostAndPort("192.168.25.161",7003));
        nodes.add(new HostAndPort("192.168.25.161",7004));
        nodes.add(new HostAndPort("192.168.25.161",7005));
        nodes.add(new HostAndPort("192.168.25.161",7006));
        JedisCluster jedisCluster = new JedisCluster(nodes);
        //直接使用JedisCluster对象操作redis.
        jedisCluster.set("test","123");
        String test = jedisCluster.get("test");
        System.out.println(test);
        //关闭jediscluster对象
        jedisCluster.close();
    }
}
