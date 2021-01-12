package com.babayan.babe.cafe.app.model.dao;

import com.babayan.babe.cafe.app.model.enums.GenderEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * @author by artbabayan
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "user")
public class UserEntity extends AbstractEntity {
    private static final int MAX_SIZE_FULL_NAME = 52;
    private static final int MAX_SIZE_USER_NAME = 52;
    private static final int MAX_SIZE_EMAIL = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = MAX_SIZE_FULL_NAME)
    @Column(name = "fullname")
    private String fullName;

    @Size(max = MAX_SIZE_USER_NAME)
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Email(message = "Please provide a valid Email")
    @NotEmpty
    @Size(max = MAX_SIZE_EMAIL)
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "temp_password", nullable = false)
    private String tempPassword;

    @Column(name = "is_active")
    private boolean active = false;

    @Column(name = "is_email_verified")
    private boolean emailVerified = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GenderEnum gender;

    @Column(name = "note")
    private String note;

    @JsonBackReference
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();

}
