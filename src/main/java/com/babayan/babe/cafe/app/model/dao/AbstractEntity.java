package com.babayan.babe.cafe.app.model.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * Superclass for all entities
 *
 * @author by artbabayan
 */
@Setter
@Getter
@MappedSuperclass
public class AbstractEntity implements Serializable {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_created")
    private Date timeCreated;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_modified")
    private Date timeModified;

    @PrePersist
    protected void prePersist() {
        timeCreated = new Date();
        timeModified = new Date();
    }

    @PreUpdate
    protected void preUpdate() {
        timeModified = new Date();
    }

}
