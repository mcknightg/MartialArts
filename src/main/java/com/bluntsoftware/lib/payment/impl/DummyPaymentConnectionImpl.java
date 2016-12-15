package com.bluntsoftware.lib.payment.impl;

import com.bluntsoftware.app.modules.user_manager.domain.ApplicationUser;
import com.bluntsoftware.lib.payment.PaymentConnection;

/**
 * Created by Alex Mcknight on 7/19/2016.
 */
public class DummyPaymentConnectionImpl implements PaymentConnection {
    @Override
    public Object generateToken(ApplicationUser user) {
        return "3435lkj423jlkld656";
    }

    @Override
    public Boolean subscribe(ApplicationUser user, String payment_nonce, String subscription_plan) {
        return true;
    }

    @Override
    public Boolean isPaid(ApplicationUser user, String service) {
        return true;
    }

    @Override
    public Boolean purchase(ApplicationUser user, String payment_nonce, String item) {
        return true;
    }
}
