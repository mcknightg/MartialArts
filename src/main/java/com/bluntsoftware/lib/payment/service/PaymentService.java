package com.bluntsoftware.lib.payment.service;


import com.bluntsoftware.app.modules.user_manager.domain.ApplicationUser;
import com.bluntsoftware.lib.payment.PaymentConnection;
import com.bluntsoftware.lib.security.AccountService;
import com.bluntsoftware.lib.security.SecurityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by Alexander on 8/24/2015.
 */
@Controller("PaymentService")
@RequestMapping(value = "/payment")
@Transactional
public class PaymentService {

    @Autowired
    AccountService accountService;
    @Autowired
    PaymentConnection paymentConnection;

    @RequestMapping(value = "/client_token", method = RequestMethod.POST)
    public @ResponseBody
    Object clientToken(HttpServletRequest request, HttpServletResponse response) {
        ApplicationUser user = accountService.getByLogin(SecurityUtils.getCurrentLogin());
        return paymentConnection.generateToken(user);
    }
    @RequestMapping(value = "/payment-methods", method = RequestMethod.POST)
    public @ResponseBody
    Object paymentMethods(HttpServletRequest request, HttpServletResponse response) {
        String nonce = request.getParameter("payment_method_nonce");
        // Use payment method nonce here
        return "";
    }

    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public @ResponseBody
    ApplicationUser get(HttpServletRequest request, HttpServletResponse response) {
        return accountService.getByLogin(SecurityUtils.getCurrentLogin());
    }
}
