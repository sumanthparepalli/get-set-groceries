package com.ecommerce.getsetgroceries.services;

import com.ecommerce.getsetgroceries.models.*;
import com.ecommerce.getsetgroceries.repositories.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.io.IOException;
import java.security.SignatureException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static com.ecommerce.getsetgroceries.checkout.xPay.generateXpaytoken;

@Service
public class PaymentService {

    private final PaymentRepo paymentRepo;
    private final OrderRepo orderRepo;
    private final CreditSchemeContriRepo creditSchemeContriRepo;
    private final CreditSchemeReqRepo creditSchemeReqRepo;
    @PersistenceContext
    private EntityManager em;
    private final OrderDetailsRepo orderDetailsRepo;

    public PaymentService(PaymentRepo paymentRepo, OrderRepo orderRepo, CreditSchemeContriRepo creditSchemeContriRepo, CreditSchemeReqRepo creditSchemeReqRepo, OrderDetailsRepo orderDetailsRepo) {
        this.paymentRepo = paymentRepo;
        this.orderRepo = orderRepo;
        this.creditSchemeContriRepo = creditSchemeContriRepo;
        this.creditSchemeReqRepo = creditSchemeReqRepo;
        this.orderDetailsRepo = orderDetailsRepo;
    }

    private JSONObject completePayment(String callid) throws SignatureException, IOException, JSONException {
        String xpaytoken = generateXpaytoken("payment/data/" + callid,
                "apikey=3LXNZKOIF92IZSK6ENHZ21cV7A_TdR5y6u08gqnUP2tujkhRE",
                "41KyfrlSuiLCCZube/Rf1k3#osxxwk63OegZJ0wQ",
                "");
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://sandbox.api.visa.com/wallet-services-web/payment/data/" + callid + "?apikey=3LXNZKOIF92IZSK6ENHZ21cV7A_TdR5y6u08gqnUP2tujkhRE")
                .method("GET", null)
                .addHeader("Accept", "application/json")
                .addHeader("x-pay-token", xpaytoken)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        return new JSONObject(response.body().string());
    }

    @Transactional
    public void completeOrder(String callid, User user) throws SignatureException, IOException, JSONException, SQLException {
        DecimalFormat df = new DecimalFormat("0.00");
        String username= user.getUsername();
        JSONObject jsonObject = completePayment(callid);
        OrderPayment orderPayment = OrderPayment.builder()
                .paidDate(new Date(jsonObject.getLong("creationTimeStamp")))
                .paidByUser(jsonObject.getJSONObject("userData").getString("userName"))
                .callid(callid)
                .build();
        Optional<Order> optionalOrder = orderRepo.getOrderByPaidFalseAndUsername(username);
        Order order = optionalOrder.get();
        for (var x : order.orderDetails) {
            System.out.println(x);
            try {
                em.createQuery("update Inventory set quantity=quantity-:qty where sellerId=:sellerId and productId=:productId")
                        .setParameter("qty", x.getQuantity())
                        .setParameter("sellerId", x.getSellerId())
                        .setParameter("productId", x.getProductId())
                        .executeUpdate();
            } catch (GenericJDBCException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                orderDetailsRepo.delete(x);
//                throw new SQLException(x.product.getName()+" is out of stock, hence Removed from cart");
            }
            orderPayment.setOrderId(order.getId());
            order.setPaid(true);
            order.setOrderDate(new Date(System.currentTimeMillis()));
            paymentRepo.save(orderPayment);
        }
        optionalOrder.ifPresent(cart -> {
            List<Tuple> resultList = em.createQuery("select  o.sellerId,sum(o.price) as amount from OrderDetails o where o.orderId=:orderId group by o.sellerId", Tuple.class)
                    .setParameter("orderId", cart.getId())
                    .getResultList();
            for(var i:resultList)
            {
                List<Tuple> tuple = (List<Tuple>) em.createNativeQuery("select cc.credit_scheme_id,cc.amount, csr.discount " +
                        "from credit_scheme_contri cc join credit_scheme_req csr " +
                        "on cc.credit_scheme_id = csr.id where csr.sellers_id=:sellerId and cc.amount > cc.amount_resolved " +
                        " and DATE_ADD(cc.date,INTERVAL csr.locking_period DAY)<=DATE(NOW()) ORDER BY cc.date LIMIT 1", Tuple.class)
                        .setParameter("sellerId", i.get(0, Long.class))
                        .getResultList();
                if(tuple != null && tuple.size()>0)
                {
                    Double amount = i.get("amount", Double.class);
                    Float discount = tuple.get(0).get("discount",Float.class);
                    Float balance = tuple.get(0).get("amount",Float.class)*(1+discount/100);
                    Float bal = tuple.get(0).get("amount", Float.class);
                    long csrId = tuple.get(0).get(0, Integer.class);
                    CreditSchemeContri cc = (CreditSchemeContri) em.createNativeQuery("select cc.* from credit_scheme_contri cc inner join credit_scheme_req csr " +
                            "where cc.user_id=:userId and cc.credit_scheme_id=:csrId and cc.amount>cc.amount_resolved and " +
                            "DATE_ADD(cc.date,INTERVAL csr.locking_period DAY)<=DATE(NOW()) order by cc.date", CreditSchemeContri.class)
                            .setParameter("csrId", csrId)
                            .setParameter("userId", user.getId())
                            .setMaxResults(1)
                            .getSingleResult();
                    if(amount>balance)
                    {
                        bal = Float.valueOf(df.format(bal));
                        cc.addToResolved(bal*1.0d);
                        cc.creditSchemeReq.addToCleared(bal);
//                        promotionAmount.updateAndGet(x -> (balance+balance*discount/100)+x);
                    }
                    else
                    {
                        amount-=amount*discount/100;
                        amount = Double.valueOf(df.format(amount));
                        cc.addToResolved(amount);
                        cc.creditSchemeReq.addToCleared(amount);
//                        promotionAmount.updateAndGet(x -> (amount+amount*discount/100)+x);
                    }
                    creditSchemeReqRepo.save(cc.creditSchemeReq);
                    creditSchemeContriRepo.save(cc);
                }
            }
//            cart.setPromotion(promotionAmount.get());
//            cart.setTotal(cart.getAmount()-promotionAmount.get());
        });
    }

    @Transactional
    public void CompleteCreditPayment(String callid, User user, long creditSchemeId, double amount) throws SignatureException, IOException, JSONException {
        JSONObject jsonObject = completePayment(callid);
        CreditSchemeContri creditSchemeContri = new CreditSchemeContri(creditSchemeId, user.getId(), amount, 0.0, Instant.ofEpochMilli(jsonObject.getLong("creationTimeStamp")).atZone(ZoneId.systemDefault()).toLocalDate(), callid);
        CreditSchemeReq creditSchemeReq = creditSchemeReqRepo.findById(creditSchemeId).get();
        creditSchemeReq.addAmount(amount);
        creditSchemeReqRepo.save(creditSchemeReq);
        creditSchemeContriRepo.save(creditSchemeContri);
    }

}
