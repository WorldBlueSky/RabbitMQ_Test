package workAver;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import utils.RabbitMQUtils;

import java.io.IOException;

public class AvgProvider {
    public static void main(String[] args) {
        Connection connection =null;
        Channel channel =null;
        try {
            // 1、获取连接对象
            connection = RabbitMQUtils.getConnect();

            //2、通过连接获取信道
            assert connection != null;
            channel = connection.createChannel();

            // 声明发送的消息
            String message = "work平均分配生产的消息!";

            //3、声明队列信息
            channel.queueDeclare("work", false, false, false, null);

            for (int i = 1; i <= 10; i++) {
                //4、使用信道发送消息, routineKey与队列同名方便匹配
                channel.basicPublish("", "work", MessageProperties.PERSISTENT_TEXT_PLAIN, (message+": "+i).getBytes());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            RabbitMQUtils.close(channel,connection);
        }
    }
}
