package test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import utils.RabbitMQUtils;
import java.io.IOException;

public class Provider1 {


    public static void main(String[] args) {

        // 1、通过工具类获取 rabbitMQ 服务的连接对象
        Connection connection = RabbitMQUtils.getConnect();

        Channel channel = null;


        try {
            // 2、通过连接获取信道
            assert connection != null;
            channel = connection.createChannel();

            //3、通过信道声明队列
            channel.queueDeclare("hello", false, false, false, null);

            String body = "这是传递的消息!";

            //4、通过交换机、路由规则绑定队列传递信息
            channel.basicPublish("", "hello", MessageProperties.PERSISTENT_TEXT_PLAIN,body.getBytes());

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }finally {
            //5、关闭信道与连接
            RabbitMQUtils.close(channel,connection);
        }




    }


}
