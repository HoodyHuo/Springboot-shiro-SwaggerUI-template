package tech.hoody.platform.domain

import javax.persistence.*

/**
 *
 * Created by Hoody on 2019/1/15.
 */
@Entity
@Table(name = "role")
public class Role implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id

    @Column(name = "role")
    private String role

    @Column(name = "available")
    private Boolean isAvailable

    @ManyToOne(targetEntity = Role.class)
    @JoinColumn(name = "pid")
    private Role parentRole

    @Column(name = "description")
    private String description

    @OneToMany(cascade = CascadeType.ALL, targetEntity = Role.class, fetch = FetchType.LAZY, mappedBy = "parentRole")
    private Set<Role> children = new HashSet<Role>()

    Set<Role> getChildren() {
        return children
    }

    void setChildren(Set<Role> children) {
        this.children = children
    }

    Long getId() {
        return id
    }

    void setId(Long id) {
        this.id = id
    }

    String getRole() {
        return role
    }

    void setRole(String role) {
        this.role = role
    }

    boolean getIsAvailable() {
        return isAvailable
    }

    void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable
    }

    Role getParentRole() {
        return parentRole
    }

    void setParentRole(Role parentRole) {
        this.parentRole = parentRole
    }

    String getDescription() {
        return description
    }

    void setDescription(String description) {
        this.description = description
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", role='" + role + '\'' +
                ", isAvailable=" + isAvailable +
                ", parentRole=" + parentRole +
                ", description='" + description + '\'' +
                '}';
    }
}
