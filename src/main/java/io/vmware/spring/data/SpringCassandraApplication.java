package io.vmware.spring.data;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Optional;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.cassandra.SessionFactory;
import org.springframework.data.cassandra.core.cql.session.init.KeyspacePopulator;
import org.springframework.data.cassandra.core.cql.session.init.ResourceKeyspacePopulator;
import org.springframework.data.cassandra.core.cql.session.init.SessionFactoryInitializer;
import org.springframework.lang.NonNull;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vmware.spring.data.model.User;
import io.vmware.spring.data.repo.UserRepository;
import io.vmware.spring.data.support.CassandraDatabaseInitializer;

/**
 * {@link SpringBootApplication} used to bootstrap, configure and initialize Apache Cassandra.
 *
 * @author John Blum
 * @see org.springframework.boot.ApplicationRunner
 * @see org.springframework.boot.SpringApplication
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 * @see org.springframework.context.annotation.Bean
 * @see org.springframework.context.annotation.Configuration
 * @see org.springframework.data.cassandra.core.cql.session.init.KeyspacePopulator
 * @see org.springframework.data.cassandra.core.cql.session.init.SessionFactoryInitializer
 * @since 1.0.0
 */
@SpringBootApplication
@SuppressWarnings("unused")
public class SpringCassandraApplication {

	private static final boolean CONTINUE_ON_ERROR = false;
	private static final boolean IGNORE_FAILED_DROPS = false;

	private static final CassandraDatabaseInitializer cassandraDatabaseInitializer =
		CassandraDatabaseInitializer.init();

	private static final Logger logger = LoggerFactory.getLogger(SpringCassandraApplication.class);

	private static final String CASSANDRA_DATA_CQL = "cassandra-data.cql";
	private static final String CQL_SCRIPT_ENCODING = null;

	public static void main(String[] args) {
		SpringApplication.run(SpringCassandraApplication.class);
	}

	@Configuration
	static class CassandraApplicationConfiguration {

		@Bean
		CqlSessionBuilderCustomizer cqlSessionBuilderCustomizer() {

			logger.debug("Customizing the CqlSessionBuilder...");

			return cqlSessionBuilder -> cqlSessionBuilder
				.withLocalDatacenter(CassandraDatabaseInitializer.CASSANDRA_LOCAL_DATACENTER)
				.withKeyspace(CassandraDatabaseInitializer.CASSANDRA_KEYSPACE_NAME)
				.addContactPoint(newSocketAddress());
		}

		private @NonNull InetSocketAddress newSocketAddress() {

			String hostname = resolveHostname();
			int port = resolvePort();

			logger.info("Connecting to Apache Cassandra database on host [{}] listening on port [{}]", hostname, port);

			return new InetSocketAddress(hostname, port);
		}

		private @NotNull String resolveHostname() {

			try {
				return InetAddress.getLocalHost().getHostName();
			}
			catch (UnknownHostException ignore) {
				return CassandraDatabaseInitializer.CASSANDRA_DEFAULT_HOSTNAME;
			}
		}

		private int resolvePort() {

			try {
				return cassandraDatabaseInitializer.getCassandraContainer()
					.getMappedPort(CassandraDatabaseInitializer.CASSANDRA_DEFAULT_PORT);
			}
			catch (Exception ignore) {
				return CassandraDatabaseInitializer.CASSANDRA_DEFAULT_PORT;
			}
		}

		@Bean
		SessionFactoryInitializer sessionFactoryInitializer(SessionFactory sessionFactory) {

			SessionFactoryInitializer sessionFactoryInitializer = new SessionFactoryInitializer();

			sessionFactoryInitializer.setKeyspacePopulator(newKeyspacePopulator());
			sessionFactoryInitializer.setSessionFactory(sessionFactory);

			return sessionFactoryInitializer;
		}

		private @NonNull KeyspacePopulator newKeyspacePopulator() {
			return new ResourceKeyspacePopulator(CONTINUE_ON_ERROR, IGNORE_FAILED_DROPS, CQL_SCRIPT_ENCODING,
				new ClassPathResource(CASSANDRA_DATA_CQL));
		}
	}

	@Bean
	@Profile("manual")
	ApplicationRunner runner(UserRepository userRepository) {

		return args -> {

			assertThat(userRepository.count()).isOne();

			Optional<User> jonDoe = userRepository.findByName("Jon Doe");

			assertThat(jonDoe).isNotNull();
			assertThat(jonDoe.isPresent()).isTrue();
			assertThat(jonDoe.map(User::getName).orElse(null)).isEqualTo("Jon Doe");

			logger.info("Spring Cassandra Application Initialized!!!");
		};
	}
}
