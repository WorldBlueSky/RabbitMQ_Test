package workAver;

import com.rabbitmq.client.*;
import utils.RabbitMQUtils;

import java.io.IOException;

public class AvgCustomer2 {
    public static void main(String[] args) {

        try {

            Connection connection = RabbitMQUtils.getConnect();
            final Channel channel = connection.createChannel();

            //声明信道中一次只能接受一条信息
            channel.basicQos(1);

            // 声明队列
            channel.queueDeclare("work", false, false, false, null);

            // 该消费者1 接收队列中的消息
            channel.basicConsume("work", false, new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    // 休眠1秒
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 处理业务
                    System.out.println(new String(body));

                    // 手动确认
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
