package com.babayan.babe.cafe.app.model.dto;

import com.babayan.babe.cafe.app.model.enums.GenderEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
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
public class User extends AbstractModel {
    private Long id;

    private String fullName;

    private String username;

    @NotEmpty
    private String email;

    @JsonIgnore
    @Size(min = 6, max = 15, message = "Password must be between 5 and 15 characters long")
    private String password;

    @JsonIgnore
    private String tempPassword;

    private boolean active = false;

    private boolean emailVerified = false;

    private GenderEnum gender;

    private String note;

    @NotEmpty
    private Set<Role> roles = new HashSet<>();

}
