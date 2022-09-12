package topic;

import com.rabbitmq.client.*;
import utils.RabbitMQUtils;

import java.io.IOException;

public class TCustomer2 {
    public static void main(String[] args) {
        Connection connection = RabbitMQUtils.getConnect();

        try {
            // 创建信道
            Channel channel = connection.createChannel();

            // 声明交换机
            channel.exchangeDeclare("bbb", "topic");

            // 声明临时队列
            String queue = channel.queueDeclare().getQueue();

            // 队列绑定交换机，并订阅路由（使用通配符）
            channel.queueBind(queue, "bbb", "admin.#");

            // 通过队列接收消息
            channel.basicConsume(queue, false, new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println(new String(body)+"  消费者接收路由通配: admin.#");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
