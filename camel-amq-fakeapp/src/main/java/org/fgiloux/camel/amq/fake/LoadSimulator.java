package org.fgiloux.camel.amq.fake;

import java.util.concurrent.ThreadLocalRandom;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration parameters filled in from application.properties and overridden using env variables on Openshift.
 */
@Configuration
@ConfigurationProperties(prefix = "processing")
public class LoadSimulator implements Processor {

	protected static final Logger LOG = LoggerFactory.getLogger(LoadSimulator.class);
	
	private double load;
	private double duration;
	private double deviation4load;
	private double deviation4duration;
	
	public double getLoad() {
		return load;
	}
	public void setLoad(double load) {
		this.load = load;
	}
	public double getDuration() {
		return duration;
	}
	public void setDuration(double duration) {
		this.duration = duration;
	}
	public double getDeviation4load() {
		return deviation4load;
	}
	public void setDeviation4load(double deviation4load) {
		this.deviation4load = deviation4load;
	}
	public double getDeviation4duration() {
		return deviation4duration;
	}
	public void setDeviation4duration(int deviation4duration) {
		this.deviation4duration = deviation4duration;
	}
	
	public void process(Exchange exchange) throws Exception {
		// TODO: It would be possible to make the load dependent on messages.
		// TODO: and also to simulate IO wait.
		long startTime = System.currentTimeMillis();
		double factualDuration =  ThreadLocalRandom.current().nextGaussian() * deviation4duration + duration;
		double factualLoad = ThreadLocalRandom.current().nextGaussian() * deviation4load + load;
        try {
            // Loop for the given duration
            while (System.currentTimeMillis() - startTime < factualDuration) {
                // Every 100ms, sleep for the percentage of not loaded time
                if (System.currentTimeMillis() % 100 == 0) {
                    Thread.sleep((long) Math.floor((1 - factualLoad) * 100));
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}
}
