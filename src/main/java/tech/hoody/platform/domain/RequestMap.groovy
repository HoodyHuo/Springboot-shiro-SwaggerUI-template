package tech.hoody.platform.domain

import javax.persistence.*

/**
 * Created by Hoody on 2019/1/17.
 */
@Entity
@Table(name = "request_map")
class RequestMap {

    @Id
    @GeneratedValue
    Long id

    String configAttribute

    String url

    @Override
    public String toString() {
        return "RequestMap{" +
                "id=" + id +
                ", configAttribute='" + configAttribute + '\'' +
                ", url='" + url + '\'' +
                ", action=" + action.toString() +
                '}';
    }
}
