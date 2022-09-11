package hello;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Customer {
    public static void main(String[] args) {
        //1、创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("120.46.143.156");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123456");

        // 虚拟主机相当于一个节点，作用相当于数据库软件中的 存储各种表数据的数据库
        // 发送消息接收消息 （channel、exchange、routinekey、queue）都在虚拟主机中完成
        connectionFactory.setVirtualHost("/test");

        Connection connection = null;
        Channel channel =null;

        try {
            //2、根据连接工厂创建连接对象
            connection = connectionFactory.newConnection();
            //3、根据连接对象创建信道
            channel = connection.createChannel();
            // 这里使用的是 helloworld 简单模型，不需要交换机，不需要路由，只需要队列
            //4、在信道中声明队列
            channel.queueDeclare("queue",false,false,false,null);
            //5、使用该信道进行发送消息
            /**
             * 参数1 ： 队列的名字
             * 参数2 ： 是否自动确认，如果接收方接受了消息之后是否确认收到
             * 参数3 ： 接收到消息之后的业务操作
             */
            channel.basicConsume("queue", true, new DefaultConsumer(channel){
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("接受了发送方的消息："+ new String(body));
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
