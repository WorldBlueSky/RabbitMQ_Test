package direct;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import utils.RabbitMQUtils;

import java.io.IOException;

public class DProvider {
    public static void main(String[] args) {
        Connection connection = RabbitMQUtils.getConnect();
        Channel channel = null;

        try {
            // 创建信道
            channel = connection.createChannel();

            // 声明交换机以及类型
            channel.exchangeDeclare("aaa", "direct");

            // 设置消息
            String body = "direct模式发送消息";

            // 在信道中 将消息 发送到交换机 同时设置路由规则

            //发送路由为 info的消息
            channel.basicPublish("aaa", "info", null,(body+":info").getBytes() );

            // 发送路由为 warning的消息
            channel.basicPublish("aaa", "warning", null, (body+":warning").getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            RabbitMQUtils.close(channel, connection);
        }
    }
}
