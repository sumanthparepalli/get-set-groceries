package com.ecommerce.getsetgroceries.models;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "credit_scheme_req")
@Data
@AllArgsConstructor
@Builder
public class CreditSchemeReq {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Double amountRequested;
  private Double amountReceived;
  private Double amountCleared;
  private Double discount;
  private Long lockingPeriod;
  @Column(name = "sellers_id")
  private Long sellerId;

  @ManyToOne
  @JoinColumn(name = "sellers_id", insertable = false, updatable = false)
  public @EqualsAndHashCode.Exclude @ToString.Exclude
  Seller seller;

  @OneToMany(mappedBy = "creditSchemeReq")
  public @EqualsAndHashCode.Exclude @ToString.Exclude
  Set<CreditSchemeContri> contris;

  public CreditSchemeReq() {
  }

  public CreditSchemeReq(Double amountRequested, Double amountReceived, Double discount, Long lockingPeriod, Long sellerId) {
    this.amountRequested = amountRequested;
    this.amountReceived = amountReceived;
    this.discount = discount;
    this.lockingPeriod = lockingPeriod;
    this.sellerId = sellerId;
  }

  public Double getAmount()
  {
    return amountRequested-amountReceived;
  }

  public void addAmount(double amount) {
    amountReceived+=amount;
  }

  public void addToCleared(double val)
  {
    if(amountCleared!=null)
      amountCleared+=val;
    else
      amountCleared=val;
  }
}
