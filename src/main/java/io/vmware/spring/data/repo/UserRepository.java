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
package io.vmware.spring.data.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import io.vmware.spring.data.model.User;

/**
 * Spring Data {@link CrudRepository} and {@literal Data Access Object (DAO)} used to perform basic {@literal CRUD}
 * and simple query data access operations on {@link User} application domain model objects.
 *
 * @author John Blum
 * @see org.springframework.data.repository.CrudRepository
 * @see io.vmware.spring.data.model.User
 * @since 1.0.0
 */
public interface UserRepository extends CrudRepository<User, Integer> {

	Optional<User> findByName(String name);

}
