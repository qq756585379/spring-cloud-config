package org.springframework.cloud.config.server.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.cloud.config.server.config.ConfigServerProperties;
import org.springframework.cloud.config.server.config.EnvironmentRepositoryConfiguration;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.cloud.config.server.environment.EnvironmentRepositoryPropertySourceLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty("spring.cloud.config.server.bootstrap")
public class ConfigServerBootstrapConfiguration {

	@EnableConfigurationProperties(ConfigServerProperties.class)
	@Import({EnvironmentRepositoryConfiguration.class})
	protected static class LocalPropertySourceLocatorConfiguration {

		@Autowired
		private EnvironmentRepository repository;

		@Autowired
		private ConfigClientProperties client;

		@Autowired
		private ConfigServerProperties server;

		@Bean
		public EnvironmentRepositoryPropertySourceLocator environmentRepositoryPropertySourceLocator() {
			return new EnvironmentRepositoryPropertySourceLocator(this.repository, this.client.getName(), this.client.getProfile(), getDefaultLabel());
		}

		private String getDefaultLabel() {
			if (StringUtils.hasText(this.client.getLabel())) {
				return this.client.getLabel();
			} else if (StringUtils.hasText(this.server.getDefaultLabel())) {
				return this.server.getDefaultLabel();
			}
			return null;
		}
	}
}
