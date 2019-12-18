package tech.hoody.platform.domain

import org.hibernate.validator.constraints.Length

import javax.persistence.*
import java.sql.Timestamp

/**
 * Created by Hoody on 2019/1/14.
 */
@Entity
@Table(name = "user")
public class User implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Length(min = 2, max = 30)
    @Column(nullable = false, unique = true, name = "username")
    private String username;

    @Length(min = 2, max = 30)
    @Column(nullable = false, name = "password")
    private String password;

    @Column(nullable = false, name = "accountLocked")
    private boolean isLocke

    @Column(name = "salt")
    private String salt;

    @Column(name = "lastLogin")
    private Timestamp lastLogin

    @Transient
    private Set<String> roles = new HashSet<String>()

    @Transient
    private Set<String> perms = new HashSet<String>()

    Set<String> getRoles() {
        return roles
    }

    void setRoles(Set<String> roles) {
        this.roles = roles
    }

    Set<String> getPerms() {
        return perms
    }

    void setPerms(Set<String> perms) {
        this.perms = perms
    }

    Timestamp getLastLogin() {
        return lastLogin
    }

    void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin
    }

    String getSalt() {
        return salt
    }

    void setSalt(String salt) {
        this.salt = salt
    }

    boolean isCredentialsExpired() {
        return false
    }

    boolean isLocked() {
        return isLocke
    }

    void setIsLocke(boolean isLocke) {
        this.isLocke = isLocke
    }

    public Long getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username +
//                ", password='" + password + '\'' +
//                ", isLocke=" + isLocke +
                '}';
    }
}
