package com.ecommerce.getsetgroceries.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "credit_scheme_req")
@Data
public class CreditSchemeReq {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Double amountRequested;
  private Double amountReceived;
  private Double discount;
  private Long lockingPeriod;
//  @Column(name = "sellers_id")
//  private Long sellerId;

  @ManyToOne
  @JoinColumn(name = "sellers_id")
  public @EqualsAndHashCode.Exclude Seller seller;

  @OneToMany(mappedBy = "creditSchemeReq")
  public @EqualsAndHashCode.Exclude  Set<CreditSchemeContri> contris;

  public CreditSchemeReq() {
  }

  public CreditSchemeReq(Double amountRequested, Double amountReceived, Double discount, Long lockingPeriod, Long sellerId) {
    this.amountRequested = amountRequested;
    this.amountReceived = amountReceived;
    this.discount = discount;
    this.lockingPeriod = lockingPeriod;
//    this.sellerId = sellerId;
  }
}
