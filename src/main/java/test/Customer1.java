package test;

import com.rabbitmq.client.*;
import utils.RabbitMQUtils;

import java.io.IOException;

public class Customer1 {

    public static void main(String[] args) {
        Connection connect = RabbitMQUtils.getConnect();

        try {
            //1、获取信道
            assert connect != null;
            Channel channel = connect.createChannel();

            //2、声明队列，与生产者队列保持一致
            channel.queueDeclare("hello", false, false, false, null);

            //3、使用队列 接收消息
            channel.basicConsume("hello", false, new DefaultConsumer(channel){
                // 接受完消息之后执行回调函数，执行后续的业务
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println(new String(body));
                }
            });

            // 4、 不用关闭资源，否则可能接收完参数还未等执行完业务操作，线程结束了

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
