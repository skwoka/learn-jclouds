package org.example.jclouds;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

public class JCloudsConfiguration extends Configuration {

	@NotEmpty
	@JsonProperty
	private String rackspaceUser;

	@NotEmpty
	@JsonProperty
	private String rackspaceKey;

	@NotEmpty
	@JsonProperty
	private String rackspaceAuthUrl = "https://auth.api.rackspacecloud.com/v1.0";
}
