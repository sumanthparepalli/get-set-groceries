package com.ecommerce.getsetgroceries.models;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "credit_scheme_contri")
@IdClass(CreditSchemeContriId.class)
@Data
public class CreditSchemeContri {

//  @Id
//  @Column(name = "credit_scheme_id")
//  private Long creditSchemeId;
//  @Id
//  @Column(name = "user_id")
//  private Long userId;
  private Double amount;
  private Double amountResolved;

  @Id
  @ManyToOne
  @JoinColumn(name = "user_id")
  public @EqualsAndHashCode.Exclude User user;

  @Id
  @ManyToOne
  @JoinColumn(name = "credit_scheme_id")
  public @EqualsAndHashCode.Exclude CreditSchemeReq creditSchemeReq;

  public CreditSchemeContri() {
  }

  public CreditSchemeContri(Double amount, Double amountResolved, User user, CreditSchemeReq creditScheme) {
    this.amount = amount;
    this.amountResolved = amountResolved;
    this.user = user;
    this.creditSchemeReq = creditScheme;
  }
}
