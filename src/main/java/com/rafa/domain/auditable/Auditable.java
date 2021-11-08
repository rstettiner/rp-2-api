package com.rafa.domain.auditable;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
public abstract class Auditable implements Serializable {
    
    @Column(name = "create_user", updatable = false)
    String createUser;
    
    @Column(name = "create_dt", updatable = false)
    Date createDt;
    
    @Column(name = "update_user")
    String updateUser;
    
    @Column(name = "update_dt")
    Date updateDt;
}
