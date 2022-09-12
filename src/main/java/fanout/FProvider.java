package fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import utils.RabbitMQUtils;

import java.io.IOException;

public class FProvider {
    public static void main(String[] args) {
        Connection connection = RabbitMQUtils.getConnect();
        Channel channel = null;

        try {
            // 创建信道
            channel = connection.createChannel();

            // 声明交换机
            channel.exchangeDeclare("exch", "fanout");

            String body = "广播模式发送消息!";

            // 信道使用交换机接收发送的消息
            channel.basicPublish("exch", "", MessageProperties.PERSISTENT_TEXT_PLAIN, body.getBytes());


        } catch (IOException e) {

            e.printStackTrace();
        }finally {
            RabbitMQUtils.close(channel,connection);
        }

    }
}
