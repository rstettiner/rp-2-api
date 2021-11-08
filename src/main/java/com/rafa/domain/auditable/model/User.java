package com.rafa.domain.auditable.model;

import com.rafa.domain.auditable.Auditable;
import com.rafa.repository.base.AuditableFields;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@EntityListeners(AuditableFields.class)
@Table(name = "user")
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id") private Long userId;

    @Column(name = "username", length = 200)private String username;

    @Column(name = "password", length = 200)private String password;

    @Column(name = "name", length = 200)private String name;

}
