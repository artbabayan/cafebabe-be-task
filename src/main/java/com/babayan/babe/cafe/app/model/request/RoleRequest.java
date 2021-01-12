package com.babayan.babe.cafe.app.model.request;

import com.babayan.babe.cafe.app.model.dto.AbstractModel;
import com.babayan.babe.cafe.app.model.enums.RoleEnum;
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
public class RoleRequest extends AbstractModel {

    @NotBlank(message = "Role cannot be blank")
    private RoleEnum roleEnum;

}
