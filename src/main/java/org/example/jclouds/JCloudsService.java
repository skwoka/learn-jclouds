package org.example.jclouds;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class JCloudsService extends Service<JCloudsConfiguration> {

	public static void main(String[] args) throws Exception {
		new JCloudsService().run(args);
	}

	@Override
	public void initialize(Bootstrap<JCloudsConfiguration> bootstrap) {
		bootstrap.setName("learn-jclouds");
	}

	@Override
	public void run(JCloudsConfiguration configuration, Environment environment)
			throws Exception {
		// TODO
	}
}
