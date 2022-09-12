package topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import utils.RabbitMQUtils;

import java.io.IOException;

public class TProvider {
    public static void main(String[] args) {
        Connection connection = RabbitMQUtils.getConnect();
        Channel channel =null;
        try {
            // 创建信道
            assert connection != null;
            channel = connection.createChannel();

            // 声明交换机，声明类型Topic
            channel.exchangeDeclare("bbb", "topic");

            // 通过信道，发送交换机设置路由规则
            channel.basicPublish("bbb", "admin.user", null, ("生产者 admin.user 的消息!").getBytes());

            channel.basicPublish("bbb", "admin", null, ("生产者 admin 的消息").getBytes());

            channel.basicPublish("bbb", "admin.user.name", null, ("生产者 admin.user.name 的消息").getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            RabbitMQUtils.close(channel, connection);
        }
    }
}
