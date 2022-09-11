package utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQUtils {

    // 单例模式--懒汉式，类加载的时候在去创建实例
    private static ConnectionFactory connectionFactory;

    // 静态的资源属性只需要赋值一次
    static{
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("120.46.143.156"); // 部署rabbitMQ的
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/test");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123456");
    }

    // 定义提供连接对象的方法
    public static Connection getConnect(){
        try {
            return connectionFactory.newConnection();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // 定义关闭资源的方法
    public static void close(Channel channel,Connection connection){
        if(channel!=null){
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }

        if(connection!=null){
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
