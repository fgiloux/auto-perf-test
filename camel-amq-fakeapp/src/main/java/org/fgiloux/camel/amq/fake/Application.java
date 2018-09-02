package org.fgiloux.camel.amq.fake;

import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.apache.camel.component.amqp.AMQPComponent;
import org.apache.camel.opentracing.starter.CamelOpenTracing;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

/**
 * The Spring-boot main class.
 */
@SpringBootApplication
@CamelOpenTracing
@ImportResource({"classpath:spring/camel-context.xml"})
public class Application {
	
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean(name = "amqp")
    AMQPComponent amqpComponent(AMQPConfiguration config) {
        JmsConnectionFactory qpid = new JmsConnectionFactory();
        qpid.setTopicPrefix("topic://");
        qpid.setUsername(config.getUsername());
        qpid.setPassword(config.getPassword());
        qpid.setRemoteURI(config.getFullUri());
        PooledConnectionFactory factory = new PooledConnectionFactory();
        factory.setConnectionFactory(qpid);
        return  new AMQPComponent(factory);
    }
    
    @Bean(name = "loadSimulator")
    LoadSimulator loadSimulator() {
    	return new LoadSimulator();
    }

}
