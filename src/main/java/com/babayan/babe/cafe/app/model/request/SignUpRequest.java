package com.babayan.babe.cafe.app.model.request;

import com.babayan.babe.cafe.app.model.dto.AbstractModel;
import com.babayan.babe.cafe.app.model.dto.Role;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author by artbabayan
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SignUpRequest extends AbstractModel {
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "FullName cannot be blank")
    private String fullName;

}
