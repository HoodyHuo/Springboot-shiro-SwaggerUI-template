package tech.hoody.platform.repository

import org.springframework.data.jpa.repository.JpaRepository
import tech.hoody.platform.domain.Role
import tech.hoody.platform.domain.RolePermission

interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    public List<RolePermission> findAllByRole(Role role)
}
