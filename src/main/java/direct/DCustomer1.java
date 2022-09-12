package direct;

import com.rabbitmq.client.*;
import utils.RabbitMQUtils;

import java.io.IOException;

public class DCustomer1 {
    public static void main(String[] args) {
        Connection connection = RabbitMQUtils.getConnect();
        Channel channel = null;

        try {
            // 创建信道
            channel = connection.createChannel();

            // 声明交换机,与生产者保持一致
            channel.exchangeDeclare("aaa", "direct");

            // 声明临时队列
            String queue = channel.queueDeclare().getQueue();

            // 绑定临时队列 与 交换机，订阅交换机中具体路由规则分发的信息, 交换机和路由规则都是 生产者指定的
            // 如果绑定了路由，那么相当于订阅了消息，一种符合规则的广播
            channel.queueBind(queue, "aaa", "info");
            channel.queueBind(queue, "aaa", "warning");

            // 通过队列 接受消息
            channel.basicConsume(queue, false, new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println(new String(body)+"    routingkey:"+envelope.getRoutingKey());
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
