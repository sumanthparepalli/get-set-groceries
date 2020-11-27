function onVisaCheckoutReady() {
    V.init({
        apikey: "3LXNZKOIF92IZSK6ENHZ21cV7A_TdR5y6u08gqnUP2tujkhRE",
        encryptionKey: "XEXL13IOWKBH18TIO3GO13bFMW28iFzEdVO_Xia_GQVFxc2uA",
        paymentRequest: {
            currencyCode: "INR",
            subtotal: /*[[${cartModel.order.amount}]]*/ '500'
        }
    });
    V.on("payment.success", async function (payment) {
        // document
        //     .getElementById("message")
        //     .innerHTML = "Success" + JSON.stringify(payment);
        console.log(payment);
        let response = await fetch('http://localhost:8082/payment/checkout/'+payment.callid, {
            method: 'POST',
            headers: {
                'Content-Type': "application/json"
            }
        });
        const res = await response.json();
        Promise.resolve(res).then(r => {
            window.location.href="http://localhost:8082/payment/success";
        });
    });
    V.on("payment.cancel", function (payment) {
        // document
        //     .getElementById("message")
        //     .innerHTML = ("Cancel" + JSON.stringify(payment));
        console.log(payment);
    });
    V.on("payment.error", function (payment, error) {
        // document
        //     .getElementById("message")
        //     .innerHTML = ("Error" + JSON.stringify(error));
        console.log(payment);
        console.log(error);
    });
}