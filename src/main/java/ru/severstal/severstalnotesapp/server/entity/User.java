package ru.severstal.severstalnotesapp.server.entity;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = ("username_password"))
@NamedQueries({
        @NamedQuery(name = "User.findUser", query = "SELECT u FROM User u WHERE u.username = :username AND u.password = :password"),
        @NamedQuery(name = "User.findUserByUsername", query = "SELECT u FROM User u WHERE u.username = :username")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
}
