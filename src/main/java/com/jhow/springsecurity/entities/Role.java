package com.jhow.springsecurity.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_roles")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "role_id")
  private Long roleID;

  private String name;

  // #region getters and setters
  public Long getRoleID() {
    return roleID;
  }

  public void setRoleID(Long roleID) {
    this.roleID = roleID;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  // #endregion

  public enum Values {
    ADMIN(1),
    BASIC(2);

    long roleID;

    private Values(long roleID) {
      this.roleID = roleID;
    }
  }
}
