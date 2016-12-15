package com.bluntsoftware.lib.payment.impl;

import com.bluntsoftware.app.modules.user_manager.domain.ApplicationUser;
import com.bluntsoftware.lib.payment.PaymentConnection;
import com.braintreegateway.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Alex Mcknight on 7/18/2016.
 */
public class BrainTreePaymentConnectionImpl implements PaymentConnection {
    private BraintreeGateway gateway;
    public BrainTreePaymentConnectionImpl(String merchantID, String publicKey, String privateKey) {
        gateway = new BraintreeGateway(
                Environment.SANDBOX,
                merchantID,
                publicKey,
                privateKey
        );
    }

    @Override
    public Object generateToken(ApplicationUser user) {
        return gateway.clientToken().generate();
    }


    public Customer getCustomerById(String id) {
        return gateway.customer().find(id);
    }

    @Override
    public Boolean isPaid(ApplicationUser user, String service) {
        List<String> allowedPlans =  Arrays.asList("8n76", "plan2", "plan3");

        if(user != null){
            Customer customer = get(user);
            for(CreditCard creditCard:customer.getCreditCards()){

                List<Subscription> subscriptions = creditCard.getSubscriptions();

                for(Subscription subscription:subscriptions){
                    String planID = subscription.getPlanId();
                    if( allowedPlans.contains(planID)){
                        return true;
                    }
                }

            }
            for(PayPalAccount creditCard:customer.getPayPalAccounts()){

                List<Subscription> subscriptions = creditCard.getSubscriptions();

                for(Subscription subscription:subscriptions){
                    String planID = subscription.getPlanId();
                    if( allowedPlans.contains(planID)){
                        return true;
                    }
                }

            }

            /*
            if(customer != null){
                SubscriptionSearchRequest subscriptionSearchRequest = new SubscriptionSearchRequest();
                subscriptionSearchRequest.status().is(Subscription.Status.ACTIVE);


                ResourceCollection<Subscription> subscriptions =  gateway.subscription().search(subscriptionSearchRequest);
                for(Subscription subscription:subscriptions){
                      String planID = subscription.getPlanId();
                      if( allowedPlans.contains(planID)){
                            return true;
                      }
                }
            }  */
        }
        return false;
    }

    @Override
    public Boolean purchase(ApplicationUser user, String payment_nonce, String item) {
        return true;
    }


    @Override
    public Boolean subscribe(ApplicationUser user,String payment_nonce,String subscription_plan){    //"8n76"
        //,String payment_nonce
        Result<Customer> customerResult = assignPaymentMethod(user,payment_nonce);
        if(customerResult.isSuccess()){
            Customer customer = customerResult.getTarget();
            String paymentToken = customer.getDefaultPaymentMethod().getToken();//customer.getPaymentMethods().get(0).getToken();
            //Create the subscription
            SubscriptionRequest subscriptionRequest = new SubscriptionRequest()
                    .paymentMethodToken(paymentToken)
                    .planId(subscription_plan);

            Result<Subscription> subscription =  gateway.subscription().create(subscriptionRequest);
            return true;

        }
        return false;
    }
    public Result<Customer> assignPaymentMethod(ApplicationUser user,String payment_nonce){
        String tenantId = user.getTenantId();
        CustomerRequest customerRequest = new CustomerRequest().paymentMethodNonce(payment_nonce);
        if(tenantId == null){
            saveCustomer(user);
            if(user.getTenantId() != null){
                tenantId = user.getTenantId();
            }
        }
        return gateway.customer().update(tenantId,customerRequest);
    }
    public Result<Customer> saveCustomer(ApplicationUser user) {
        String tenantId = user.getTenantId();
        Result<Customer> ret = null;
        CustomerRequest customerRequest = new CustomerRequest()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail());

        if(tenantId != null){
            ret = gateway.customer().update(tenantId,customerRequest);
        }else{
            ret = gateway.customer().create(customerRequest);
            if(ret.isSuccess()) {
                Customer customer = ret.getTarget();
                user.setTenantId(customer.getId());
            }
        }
        return ret;
    }

    public Customer get(ApplicationUser user){
        Result<Customer> ret = saveCustomer(user);
        if(ret.isSuccess()){
            return ret.getTarget();
        }
        return null;
    }

    public List<? extends PaymentMethod> getPaymentMethods(ApplicationUser user){
        Customer customer = get(user);
        return customer.getPaymentMethods();
    }
}
