package hello;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Provider {


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
            /**
             * @Params1 queue:队列的名字
             * @Params1 durable: 是否支持队列持久化？ 这里的持久化以及就是队列信息写入磁盘，如果rabbitmq服务器重启也会恢复队列信息，但不是信息持久化，信息会失去
             * @Params1 exclusive: 该队列是否支持独占？ 就是这个队列在被一个信道占用的时候不能被其他进行访问
             * @Params1 autoDelete： 该队列是否自动删除？ 就是说这个队列中的信息被接收方拿完之后要自动删除
             * @Params1 arguments: map类型，一些额外的参数，比如说过期时间设置等
             */
            channel.queueDeclare("queue",false,false,false,null);

            //5、使用该信道使用exchange、routineKey 进行发送消息 Bytes，

            // 默认的交换机有一个特点，routineKey 如果和 队列名一致的话 ，那么匹配成功

            // 生产者不是将消息放在queue队列中，而是放在默认交换机中等待符合routingkey的队列匹配，routineKey 名字和队列名一致则匹配成功
            // queue在消费者这里生成，匹配成功之后消费者在queue中取消息

            /**
             * 参数1：exchange 写交换机的名字，如果不写说明使用默认default amqp
             * 参数2：routinekey 路由器，如果没有路由器的话默认路由器和队列同名，需要写上队列的名字
             * 参数3：props 这里写发送的消息是否支持持久化
             * 参数4： bytes 类型，这是我们要传递的具体消息
             */
            for(int i=0;i<10;i++){
                channel.basicPublish("", "queue", MessageProperties.PERSISTENT_TEXT_PLAIN, ("简单模型信息传递:"+i).getBytes());
            }

        } catch (Exception e){
            e.printStackTrace();
        }finally {
            if(channel!=null){
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }

            if(connection!=null){
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
