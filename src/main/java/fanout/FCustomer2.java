package fanout;

import com.rabbitmq.client.*;
import utils.RabbitMQUtils;

import java.io.IOException;

public class FCustomer2 {
    public static void main(String[] args) {
        Connection connection = RabbitMQUtils.getConnect();
        try {
            Channel channel = connection.createChannel();

            // 声明交换机，与生产者一致
            channel.exchangeDeclare("exch", "fanout");

            // 声明临时队列
            String queue = channel.queueDeclare().getQueue();

            // 交换机与队列进行绑定，临时队列绑定交换机才能听到广播的内容，routingKey不起作用，一直为空
            channel.queueBind(queue, "exch", "");

            // 使用临时队列接收 生产者在交换机中 广播的消息
            channel.basicConsume(queue, false, new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println(new String(body));;
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
