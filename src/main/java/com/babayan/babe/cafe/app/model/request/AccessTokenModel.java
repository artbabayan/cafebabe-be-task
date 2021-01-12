package com.babayan.babe.cafe.app.model.request;

import com.babayan.babe.cafe.app.model.dto.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author by artbabayan
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AccessTokenModel extends AbstractModel {
    private String accessToken;

    private String refreshToken;

    private String tokenType;

    private int expiresIn;

    private Date expiration;

}
