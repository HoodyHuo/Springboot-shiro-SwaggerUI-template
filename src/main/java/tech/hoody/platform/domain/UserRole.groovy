package tech.hoody.platform.domain

import javax.persistence.*

/**
 * Created by Hoody on 2019/1/15.
 */
@Entity
@Table(name = "user_role")
class UserRole implements Serializable {


    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(targetEntity = User.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user

    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @OneToOne(targetEntity = Role.class, cascade = CascadeType.ALL)
    Role role


    @Override
    public String toString() {
        return "UserRole{" +
                "id=" + id +
                ", user=" + user +
                ", role=" + role +
                '}';
    }

    Long getId() {
        return id
    }

    void setId(Long id) {
        this.id = id
    }

    User getUser() {
        return user
    }

    void setUser(User user) {
        this.user = user
    }

    Role getRole() {
        return role
    }

    void setRole(Role role) {
        this.role = role
    }
}
