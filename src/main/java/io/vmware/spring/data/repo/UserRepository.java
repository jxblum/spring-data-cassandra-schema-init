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
