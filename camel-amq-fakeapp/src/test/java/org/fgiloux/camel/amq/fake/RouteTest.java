package org.fgiloux.camel.amq.fake;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.MockEndpoints;
import org.apache.camel.test.spring.UseAdviceWith;
import org.apache.commons.io.FileUtils;
import org.fgiloux.camel.amq.fake.Application;
import org.apache.camel.test.spring.CamelSpringBootRunner;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
// import org.springframework.test.context.ActiveProfiles;

//@TestPropertySource("classpath:test-application.properties")
@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(classes = Application.class)
//@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@MockEndpoints
@UseAdviceWith
public class RouteTest {
	
	protected static final Logger LOG = LoggerFactory.getLogger(RouteTest.class);
	
	@Autowired
    CamelContext context;

    @Autowired
    ProducerTemplate producer;
    
    @Autowired
    ConsumerTemplate consumer;
    
	@Before
	public void configureMocks() throws Exception {
		context.getRouteDefinition("main")
            .adviceWith(context, new AdviceWithRouteBuilder() {
              @Override
              public void configure() throws Exception {
            	// mocking endpoints is done through annotations
  		        //mockEndpoints();
  			    replaceFromWith("direct:input");
              }
            });
	}
		
	@Test
	public void testRouteRunning() throws Exception {
		context.start();
		assertTrue(context.getRouteStatus("main").isStarted()); 
		context.stop();
	}
	
	@Test
	public void testRouteInternal() throws Exception {
		context.start();
		String body = FileUtils.readFileToString(new File("src/test/data/body1.txt"));
		producer.sendBody("direct:input", body);
		MockEndpoint resultEndpoint = MockEndpoint.resolve(context, "mock:amqp:{{message.output}}");
		LOG.info("endpoint id:" + resultEndpoint.getId());
		LOG.info("endpoint name:" + resultEndpoint.getName());
		String message = resultEndpoint.getExchanges().get(0).getIn().getBody(String.class);
		LOG.info("Message received: {}", message);
		resultEndpoint.expectedMessageCount(1);
		resultEndpoint.assertIsSatisfied();
		assertNotNull(message);
		assertEquals(body.trim(),message.trim());
		context.stop();
	}

}
