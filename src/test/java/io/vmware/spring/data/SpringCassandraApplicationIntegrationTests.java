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
package io.vmware.spring.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.jetbrains.annotations.NotNull;

import io.vmware.spring.data.model.User;
import io.vmware.spring.data.repo.UserRepository;

/**
 * Integration Tests for {@link SpringCassandraApplication}.
 *
 * @author John Blum
 * @see org.junit.jupiter.api.Test
 * @see org.springframework.boot.test.context.SpringBootTest
 * @see io.vmware.spring.data.SpringCassandraApplication
 * @see io.vmware.spring.data.model.User
 * @see io.vmware.spring.data.repo.UserRepository
 * @since 1.0.0
 */
@SpringBootTest
public class SpringCassandraApplicationIntegrationTests {

	@Autowired
	private UserRepository userRepository;

	@SuppressWarnings("all")
	private void assertUser(@NotNull Optional<User> user, @NotNull String expectedName) {

		assertThat(user).isNotNull();
		assertThat(user.isPresent()).isTrue();
		assertThat(user.map(User::getName).orElse(null)).isEqualTo(expectedName);
	}

	@Test
	public void queryExistingUserIsCorrect() {

		Optional<User> jonDoe = this.userRepository.findByName("Jon Doe");

		assertUser(jonDoe, "Jon Doe");
	}

	@Test
	public void saveAndQueryNewUserIsCorrect() {

		User pieDoe = User.as("Pie Doe").identifiedBy(21);

		this.userRepository.save(pieDoe);

		Optional<User> actualPieDoe = this.userRepository.findByName(pieDoe.getName());

		assertUser(actualPieDoe, "Pie Doe");
	}
}
