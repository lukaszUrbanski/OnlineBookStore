package pl.urbanskilukasz.onlineLibrary.order.domain;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

@Value
@ConstructorBinding
@ConfigurationProperties("app.orders")
public class OrdersProperties {
    Duration paymentPeriod;
    String abandonedCron;
}
