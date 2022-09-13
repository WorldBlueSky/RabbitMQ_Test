package confirm;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.Connection;
import utils.RabbitMQUtils;

public class MultipleConfirm {
    public static void main(String[] args) {
        Connection connection = RabbitMQUtils.getConnect();
        Channel channel = null;

        try {

            channel = connection.createChannel();

            // 开启确认模式
            channel.confirmSelect();

            // 声明队列
            channel.queueDeclare("confirm", true, false, false, null);

            // 作为接收成功的函数式接口 参数
            ConfirmCallback ackCallback =(deliveryTag, multiple)-> System.out.println("确认的消息: "+deliveryTag);
                // 表示接收成功的回调函数


            // 作为接收失败的函数式接口 参数
            ConfirmCallback nackCallback = (deliveryTag,multiple)-> System.out.println("接收失败!");
                // 表示接收失败的回调函数


            // 这是一个异步的监听 消息返回确认信息的 反应
            channel.addConfirmListener(ackCallback,nackCallback);

            long begin = System.currentTimeMillis();


            // 批量发送消息，每次发送进行确认
            for (int i = 0; i <1000 ; i++) {
                String message = i+"";
                // 发布单条消息
                channel.basicPublish("", "confirm", null, message.getBytes());

            }

            long end = System.currentTimeMillis();

            System.out.println("发送1000条数据，使用异步发布确认的时间为："+(end-begin)+"ms");

        } catch (Exception e){
            e.printStackTrace();
        }

        // 执行完不能关闭连接，还要继续监听确认的信息
        /**
         finally {
            RabbitMQUtils.close(channel, connection);
        }
         */

    }
}
