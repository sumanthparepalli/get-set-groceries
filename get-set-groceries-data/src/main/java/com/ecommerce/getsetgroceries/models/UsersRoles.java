package com.ecommerce.getsetgroceries.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users_roles")
@IdClass(UsersRolesID.class)
@Data
public class UsersRoles implements Serializable {

//  @Id
//  @Column(name = "user_id")
//  private Long userId;
//  @Id
//  @Column(name = "role_id")
//  private Long roleId;

  @Id
  @ManyToOne
  @JoinColumn(name = "user_id")
  private @EqualsAndHashCode.Exclude User user;

  @Id
  @ManyToOne
  @JoinColumn(name = "role_id")
  private @EqualsAndHashCode.Exclude Role role;

  public UsersRoles(User user, Role role) {
    this.user = user;
    this.role = role;
  }

  public UsersRoles() {
  }
}
