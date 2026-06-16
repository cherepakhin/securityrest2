package ru.perm.v.securityrest2.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.perm.v.securityrest2.model.ERole;
import ru.perm.v.securityrest2.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(ERole name);
}
