package com.learnjava.service;

import com.learnjava.domain.checkout.Cart;
import com.learnjava.domain.checkout.CheckoutResponse;
import com.learnjava.domain.checkout.CheckoutStatus;
import com.learnjava.util.DataSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckoutServiceTest {

    CheckoutService checkoutService=new CheckoutService();

    @Test
    void checkout_6_items() {
         Cart cart=DataSet.createCart(6);
         CheckoutResponse checkoutResponse=checkoutService.checkout(cart);
         assertEquals(CheckoutStatus.SUCCESS,checkoutResponse.getCheckoutStatus());
    }

    @Test
    void checkout_10_items() {
        Cart cart=DataSet.createCart(10);
        CheckoutResponse checkoutResponse=checkoutService.checkout(cart);
        assertEquals(CheckoutStatus.FAILURE,checkoutResponse.getCheckoutStatus());
    }

    @Test
    void checkout_25_items() {
        Cart cart=DataSet.createCart(25);
        CheckoutResponse checkoutResponse=checkoutService.checkout(cart);
        assertEquals(CheckoutStatus.FAILURE,checkoutResponse.getCheckoutStatus());
    }

    @Test
    void noOfCores(){
        System.out.println("Number of cores "+Runtime.getRuntime().availableProcessors());
    }
}