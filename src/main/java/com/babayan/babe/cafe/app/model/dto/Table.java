package com.babayan.babe.cafe.app.model.dto;

import com.babayan.babe.cafe.app.model.enums.TableStatusEnum;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

/**
 * @author by artbabayan
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Table extends AbstractModel {
    private Long id;

    @NotEmpty
    private int tableNumber;

    @NotEmpty
    private TableStatusEnum status = TableStatusEnum.AVAILABLE;

    private User owner;

}
