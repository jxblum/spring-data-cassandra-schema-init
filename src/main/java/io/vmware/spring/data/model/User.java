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
package io.vmware.spring.data.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import io.vmware.spring.data.support.CassandraDatabaseInitializer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * ADT modeling a user.
 *
 * @author John Blum
 * @since 1.0.0
 */
@Getter
@ToString(of = "name")
@EqualsAndHashCode(of = "name")
@RequiredArgsConstructor(staticName = "as")
@Table(CassandraDatabaseInitializer.CASSANDRA_USERS_TABLE_NAME)
@SuppressWarnings("unused")
public class User {

	@Id @PrimaryKey @Setter
	private Integer id;

	@lombok.NonNull @Indexed
	private final String name;

	public @NonNull User identifiedBy(@Nullable Integer id) {
		setId(id);
		return this;
	}
}
