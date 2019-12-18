package tech.hoody.platform.domain

import javax.persistence.*

@Entity
@Table(name = "role_permission")
class RolePermission {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(targetEntity = Role.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    Role role

    @JoinColumn(name = "permission_id", referencedColumnName = "id")
    @OneToOne(targetEntity = Permission.class, cascade = CascadeType.ALL)
    Permission permission

    Long getId() {
        return id
    }

    void setId(Long id) {
        this.id = id
    }

    Role getRole() {
        return role
    }

    void setRole(Role role) {
        this.role = role
    }

    Permission getPermission() {
        return permission
    }

    void setPermission(Permission permission) {
        this.permission = permission
    }

    @Override
    public String toString() {
        return "RolePermission{" +
                "id=" + id +
                ", role=" + role +
                ", permission=" + permission +
                '}';
    }
}
