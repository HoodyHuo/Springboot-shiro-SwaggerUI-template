package vip.hoody.pi.repository

import org.springframework.data.jpa.repository.JpaRepository
import vip.hoody.pi.domain.Role
import vip.hoody.pi.domain.RolePermission

interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    public List<RolePermission> findAllByRole(Role role)
}