package com.ecommerce.getsetgroceries.models;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "credit_scheme_contri")
@IdClass(CreditSchemeContriId.class)
@Data
public class CreditSchemeContri {

  @Id
  @Column(name = "credit_scheme_id")
  private Long creditSchemeId;
  @Id
  @Column(name = "user_id")
  private Long userId;
  private Double amount;
  private Double amountResolved;
  @Id
  @CreatedDate
  private LocalDate date;
  @Column(name = "callid", unique = true)
  private String callid;

//  @Id
  @ManyToOne
  @JoinColumn(name = "user_id", insertable = false, updatable = false)
  public @EqualsAndHashCode.Exclude User user;

//  @Id
  @ManyToOne
  @JoinColumn(name = "credit_scheme_id", insertable = false, updatable = false)
  public @EqualsAndHashCode.Exclude CreditSchemeReq creditSchemeReq;

  public CreditSchemeContri() {
  }

  public CreditSchemeContri(Long creditSchemeId, Long userId, Double amount, Double amountResolved, LocalDate date, String callid) {
    this.creditSchemeId = creditSchemeId;
    this.userId = userId;
    this.amount = amount;
    this.amountResolved = amountResolved;
    this.date = date;
    this.callid = callid;
  }

  public void addToResolved(Double val)
  {
    this.amountResolved+=val;
  }
}
