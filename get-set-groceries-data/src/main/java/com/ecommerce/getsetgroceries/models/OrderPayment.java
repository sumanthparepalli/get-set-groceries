package com.ecommerce.getsetgroceries.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "order_payment")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPayment {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @Column(name = "order_id",unique = true)
    private long orderId;
    private Date paidDate;
    private String paidByUser;
    @Column(name = "callid", unique = true)
    private String callid;


    @OneToOne
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    public Order order;

}
