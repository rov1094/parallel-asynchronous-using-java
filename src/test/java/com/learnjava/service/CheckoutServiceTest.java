package com.learnjava.service;

import com.learnjava.domain.checkout.Cart;
import com.learnjava.domain.checkout.CheckoutResponse;
import com.learnjava.domain.checkout.CheckoutStatus;
import com.learnjava.util.DataSet;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.*;

class CheckoutServiceTest {

    CheckoutService checkoutService=new CheckoutService();

    @Test
    void checkout_6_items() {
         Cart cart=DataSet.createCart(6);
         CheckoutResponse checkoutResponse=checkoutService.checkout(cart);
         assertEquals(CheckoutStatus.SUCCESS,checkoutResponse.getCheckoutStatus());
         assertTrue(checkoutResponse.getFinalRate()>0);
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

    @Test
    void forkJoinCommonPool(){
        System.out.println(ForkJoinPool.getCommonPoolParallelism()); // We have 8 core but 7 are shown remaining 1 is used to run ForkJoinPool.commonPool-worker to avoid deadlock scenarios
    }

    @Test
    void modify_parallelism() {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism","100");

        Cart cart=DataSet.createCart(100);
        CheckoutResponse checkoutResponse=checkoutService.checkout(cart);
        assertEquals(CheckoutStatus.FAILURE,checkoutResponse.getCheckoutStatus());
    }
}