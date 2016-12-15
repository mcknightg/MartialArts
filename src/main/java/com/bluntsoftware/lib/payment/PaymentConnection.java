package com.bluntsoftware.lib.payment;

import com.bluntsoftware.app.modules.user_manager.domain.ApplicationUser;

/**
 * Created by Alex Mcknight on 7/18/2016.
 */
public interface PaymentConnection {
    Object generateToken(ApplicationUser user);
    Boolean subscribe(ApplicationUser user, String payment_nonce, String subscription_plan);
    Boolean isPaid(ApplicationUser user,String service);
    Boolean purchase(ApplicationUser user, String payment_nonce, String item);

}
