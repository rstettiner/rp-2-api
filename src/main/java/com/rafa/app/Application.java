package com.rafa.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.TimeZone;

@Slf4j
@EnableCaching
@ComponentScan({
        "com.rafa.config",
        "com.rafa.service",
        "com.rafa.repository",
        "com.rafa.web.rest",
        "com.rafa.web.base",
        "com.rafa.web.config",
        "com.rafa.web.filters",
})
@EntityScan(basePackages = {"com.rafa.domain"})
@EnableJpaRepositories({"com.rafa.repository"})
@EnableTransactionManagement
@EnableConfigurationProperties
@EnableScheduling
@SpringBootApplication
public class Application {

	static ConfigurableApplicationContext ctx;

	/**
	 * Main method, used to run the application.
	 *
	 * @param args the command line arguments
	 * @throws UnknownHostException if the local host name could not be resolved into an address
	 */
	public static void main(String[] args) throws UnknownHostException {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));   // It will set UTC timezone

		ctx = SpringApplication.run(Application.class, args);

		Environment env = ctx.getEnvironment();

		String protocol = "http";
		if (env.getProperty("server.ssl.key-store") != null) {
			protocol = "https";
		}
		log.info("\n----------------------------------------------------------\n\t" +
						"Application '{}' is running!\n\t" +
						"Access URLs:\n\t" + 
						"Local: \t\t{}://localhost:{}\n\t" +
						"External: \t{}://{}:{}\n\t" +
						"Profile(s): \t{}\n----------------------------------------------------------",
				env.getProperty("spring.application.name"),
				protocol,
				env.getProperty("server.port"),
				protocol,
				InetAddress.getLocalHost().getHostAddress(),
				env.getProperty("server.port"),
				env.getActiveProfiles());
	}

	public static <T> T getBean(Class<T> type) {
		return ctx.getBean(type);
	}

    public static String getProperty(String path) {
        return ctx.getEnvironment().getProperty(path);
    }

}
