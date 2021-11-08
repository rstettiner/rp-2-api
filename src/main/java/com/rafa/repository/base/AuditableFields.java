package com.rafa.repository.base;


import com.rafa.domain.auditable.Auditable;
import com.rafa.web.base.ServiceContext;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

public class AuditableFields {
    
    @PreUpdate
    public void updateAuditableFields(final Auditable obj) {
        String user = ServiceContext.getContext().getCurrentUser();
        obj.setUpdateDt(new Date());
        obj.setUpdateUser(user);
    }
    
    // TIP: if the createDt and createUser of an obj are null, check where the object is being updated.
    // It can be the an obj not coming from the db being updated. All obj that are updated MUST come from the db.
    @PrePersist
    public void createAuditableFields(final Auditable obj) {
        String user = ServiceContext.getContext().getCurrentUser();

        Date now = new Date();
        obj.setCreateDt(now);
        obj.setUpdateDt(now);
        
        obj.setCreateUser(user);
        obj.setUpdateUser(user);
    }
}
