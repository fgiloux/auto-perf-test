package org.fgiloux.camel.amq.fake;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration parameters filled in from application.properties and overridden using env variables on Openshift.
 */
@Configuration
@ConfigurationProperties(prefix = "amqp")
public class AMQPConfiguration {

	private static final Logger LOG = LoggerFactory.getLogger(AMQPConfiguration.class);

    private String host;
    private Integer port;
    private String username;
    private String password;
    private String truststoreLocation;
    private String truststorePassword;
    private String scheme;
    
    private Map<String,String> options = new HashMap<String, String>();
    
    public AMQPConfiguration() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public String getTruststoreLocation() {
		return truststoreLocation;
	}

	public void setTruststoreLocation(String truststoreLocation) {
		this.truststoreLocation = truststoreLocation;
	}

	public String getTruststorePassword() {
		return truststorePassword;
	}

	public void setTruststorePassword(String truststorePassword) {
		this.truststorePassword = truststorePassword;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public Map<String, String> getOptions() {
		return options;
	}
	
	public String getFullUri() {
		StringBuilder optionStringBuilder = new StringBuilder();
		for (Map.Entry<String, String> option: options.entrySet())
			optionStringBuilder.append(option.getKey()).append("=").append(option.getValue()).append("&");
		optionStringBuilder.setLength(Math.max(optionStringBuilder.length() - 1, 0));
		String res = new StringBuilder(scheme).
				append("://").
				append(host).
				append(":").
				append(port).
				append("?").
				append(optionStringBuilder).toString();
		LOG.debug("URI: {}", res);
		return res;
	}
}
