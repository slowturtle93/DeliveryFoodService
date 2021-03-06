package dev.toyproject.foodDelivery.common.config;

import dev.toyproject.foodDelivery.order.domain.OrderInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String BOOTSTRAP_SERVERS;

    @Bean
    public ConsumerFactory<String, OrderInfo.OrderPaymentConfirmRequest>  orderPaymentConsumer() {

        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, "khs");
        return new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), new ErrorHandlingDeserializer(new JsonDeserializer<>(OrderInfo.OrderAPIPaymentResponse.class)));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderInfo.OrderPaymentConfirmRequest> orderPaymentListener() {
        ConcurrentKafkaListenerContainerFactory<String, OrderInfo.OrderPaymentConfirmRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderPaymentConsumer());
        return factory;
    }
}
