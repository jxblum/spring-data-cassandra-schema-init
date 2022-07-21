/*
 * Copyright 2022-Present Author or Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vmware.spring.data.support;

import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import org.springframework.lang.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.CassandraContainer;

/**
 * Abstract base class using Testcontainers to bootstrap, configure and initialize an Apache Cassandra database.
 *
 * @author John Blum
 * @see org.testcontainers.containers.CassandraContainer
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public abstract class CassandraDatabaseInitializer implements Runnable {

	public static final int CASSANDRA_DEFAULT_PORT = CqlSessionFactoryBean.DEFAULT_PORT;

	public static final String CASSANDRA_CONTACT_POINTS_PROPERTY = "spring.app.cassandra.contact-points";
	public static final String CASSANDRA_DEFAULT_HOSTNAME = "localhost";
	public static final String CASSANDRA_LOCAL_DATACENTER = "datacenter1";
	public static final String CASSANDRA_SCHEMA_CQL = "cassandra-schema.cql";
	public static final String CASSANDRA_SYSTEM_KEYSPACE_NAME = "system";
	public static final String CASSANDRA_TEST_KEYSPACE_NAME = "test";
	public static final String CASSANDRA_USERS_TABLE_NAME = "Users";

	protected static final String CASSANDRA_DOCKER_IMAGE_NAME = "cassandra:latest";

	public static CassandraDatabaseInitializer init() {
		CassandraDatabaseInitializer initializer = new CassandraDatabaseInitializer() { };
		initializer.run();
		return initializer;
	}

	private final CassandraContainer<?> cassandraContainer;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	protected CassandraDatabaseInitializer() {
		this.cassandraContainer = newCassandraContainer();
	}

	protected CassandraContainer<?> newCassandraContainer() {

		return new CassandraContainer<>(CASSANDRA_DOCKER_IMAGE_NAME)
			.withExposedPorts(CASSANDRA_DEFAULT_PORT)
			.withInitScript(CASSANDRA_SCHEMA_CQL)
			.withReuse(true);
	}

	public @NonNull CassandraContainer<?> getCassandraContainer() {
		return this.cassandraContainer;
	}

	protected @NonNull Logger getLogger() {
		return this.logger;
	}

	@Override
	public void run() {

		getLogger().info("Bootstrapping Apache Cassandra database...");

		// Run the Apache Cassandra database.
		getCassandraContainer().start();

		configureEnvironment(getCassandraContainer());

		getLogger().info("Apache Cassandra databased initialized!");
	}

	private @NonNull CassandraContainer<?> configureEnvironment(@NonNull CassandraContainer<?> cassandraContainer) {

		String contactPoints = String.format("%s[%d]",
			CASSANDRA_DEFAULT_HOSTNAME, cassandraContainer.getMappedPort(CASSANDRA_DEFAULT_PORT));

		getLogger().info("Apache Cassandra database contact points [{}]", contactPoints);

		System.setProperty(CASSANDRA_CONTACT_POINTS_PROPERTY, contactPoints);

		getLogger().info("System-Property['{}'] = {}", CASSANDRA_CONTACT_POINTS_PROPERTY,
			System.getProperty(CASSANDRA_CONTACT_POINTS_PROPERTY));

		return cassandraContainer;
	}
}
