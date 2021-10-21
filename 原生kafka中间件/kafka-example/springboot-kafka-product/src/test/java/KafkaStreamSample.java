import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;

import java.util.Properties;

public class KafkaStreamSample {
    private static final String INPUT_TOPIC="article_behavior_input";
    private static final String OUT_TOPIC="article_behavior_out";

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.60.128:9092");
        props.put(StreamsConfig.APPLICATION_ID_CONFIG,"article_behavior_count");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        final StreamsBuilder builder = new StreamsBuilder();

        // 构建过滤聚合条件
        group(builder);
        final KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
    }

    // 如果定义流计算过程
    static void group(final StreamsBuilder builder){
        /**
         * 第一个参数：消费的消息名称
         * 第二个参数：LATEST  最近的   EARLIEST 更早的
         */
        KStream<String,String> stream = builder.stream(INPUT_TOPIC, Consumed.with(Topology.AutoOffsetReset.LATEST));
        //聚合的时间窗口，10秒中聚合一次
        KStream<String, String> map = stream.groupByKey().windowedBy(TimeWindows.of(10000)).aggregate(new Initializer<String>() {
            //初始聚合
            @Override
            public String apply() {
                return "啥都没有";
            }
        }, new Aggregator<String, String, String>() {
            /**
             * 多次聚合
             * @param aggKey  上面apply的方法的返回值
             * @param value   消息的key值，发送消息的key
             * @param aggValue  消息的value值  发送消息的value值
             * @return
             */
            @Override
            public String apply(String aggKey, String value, String aggValue) {
                System.out.println(aggKey);
                System.out.println(value);
                System.out.println(aggValue);
                return "有值....";
            }
        }, Materialized.as("count-article-num-miukoo-1"))
                /**
                 * key ： 发消息的key值
                 * value : 上面聚合以后的返回值
                 */
                .toStream().map((key, value) -> {
                    return new KeyValue<>(key.key().toString(), value);
                });
        /**
         * 把聚合之后的消息发送到另外一个topic中
         */
        map.to(OUT_TOPIC, Produced.with(Serdes.String(), Serdes.String()));
    }
}
