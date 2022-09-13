package confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import utils.RabbitMQUtils;

import java.io.IOException;

public class SingleConfirm {
    /**
     * 发布确认模式
     * 1、单个确认
     * @param args
     */
    public static void main(String[] args) {
        Connection connection = RabbitMQUtils.getConnect();
        Channel channel = null;

        try {

            channel = connection.createChannel();

            // 开启确认模式
            channel.confirmSelect();

            // 声明队列
            channel.queueDeclare("confirm", true, false, false, null);

            long begin = System.currentTimeMillis();

            // 批量发送消息，每次发送进行确认
            for (int i = 0; i <1000 ; i++) {

                String message = i+"";

                // 发布单条消息
                channel.basicPublish("", "confirm", null, message.getBytes());

                // 单个消息发送之后，马上发布确认,使用 waitForConfirms
                channel.waitForConfirmsOrDie();

            }

            long end = System.currentTimeMillis();

            System.out.println("发送1000条数据，使用单个发布确认的时间为："+(end-begin));

        } catch (Exception e){
            e.printStackTrace();
        }finally {
            RabbitMQUtils.close(channel, connection);
        }

    }
}
