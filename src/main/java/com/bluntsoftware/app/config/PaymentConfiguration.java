package com.bluntsoftware.app.config;



import com.bluntsoftware.lib.payment.PaymentConnection;
import com.bluntsoftware.lib.payment.impl.BrainTreePaymentConnectionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Created by Alex Mcknight on 7/18/2016.
 *
 */
@Configuration
@ComponentScan({"com.bluntsoftware.lib.payment.service"})
public class PaymentConfiguration implements EnvironmentAware {

    private final Logger log = LoggerFactory.getLogger(PaymentConfiguration.class);

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public PaymentConnection paymentConnection() {
         return new BrainTreePaymentConnectionImpl(
                 environment.getProperty("braintree.merchant.id"),
                 environment.getProperty("braintree.public.key"),
                 environment.getProperty("braintree.private.key")
         );
    }
}
