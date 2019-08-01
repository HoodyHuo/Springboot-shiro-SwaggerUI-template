package vip.hoody.pi.domain

import javax.persistence.*

@Entity
@Table(name = "permission")
class Permission {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "permission")
    private String permission

    @Column(name = "available")
    private boolean isAvailable

    @Column(name = "description")
    private String description

//    @ManyToOne(targetEntity = Role.class)
//    @JoinColumn(name = "rid")
//    private Role parentRole


    Long getId() {
        return id
    }

    void setId(Long id) {
        this.id = id
    }

    String getPermission() {
        return permission
    }

    void setPermission(String permission) {
        this.permission = permission
    }

    boolean getIsAvailable() {
        return isAvailable
    }

    void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable
    }

    String getDescription() {
        return description
    }

    void setDescription(String description) {
        this.description = description
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", permission='" + permission + '\'' +
                ", isAvailable=" + isAvailable +
                ", description='" + description + '\'' +
                '}';
    }
}
