package org.springframework.cloud.config.server.bootstrap;

import java.util.Collections;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

public class ConfigServerBootstrapApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered {

	/**
	 * Default order of the bootstrap application listener.
	 */
	public static final int DEFAULT_ORDER = Ordered.HIGHEST_PRECEDENCE + 4;

	private int order = DEFAULT_ORDER;

	private PropertySource<?> propertySource = new MapPropertySource("configServerClient", Collections.<String, Object>singletonMap("spring.cloud.config.enabled", "false"));

	@Override
	public int getOrder() {
		return this.order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
		ConfigurableEnvironment environment = event.getEnvironment();
		if (!environment.resolvePlaceholders("${spring.cloud.config.enabled:false}")
			.equalsIgnoreCase("true")) {
			if (!environment.getPropertySources()
				.contains(this.propertySource.getName())) {
				environment.getPropertySources().addLast(this.propertySource);
			}
		}
	}
}
