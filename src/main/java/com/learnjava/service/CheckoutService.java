package com.learnjava.service;

import com.learnjava.domain.checkout.Cart;
import com.learnjava.domain.checkout.CartItem;
import com.learnjava.domain.checkout.CheckoutResponse;
import com.learnjava.domain.checkout.CheckoutStatus;

import java.util.List;
import java.util.stream.Collectors;

import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;
import static com.learnjava.util.LoggerUtil.log;
import static java.util.stream.Collectors.summingDouble;

public class CheckoutService {

    PriceValidatorService priceValidatorService=new PriceValidatorService();

    public CheckoutResponse checkout(Cart cart){

//        Predicate<CartItem> expiredCartItem=cartItem -> cartItem.isExpired(); // this can be passed in filter
        startTimer();
        List<CartItem> cartItems=cart.getCartItemList()
                .parallelStream()//First let us do it by stream
                .map(cartItem -> { //Transformation
                    boolean priceValidatorOutput=priceValidatorService.isCartItemInvalid(cartItem);
                    cartItem.setExpired(priceValidatorOutput);
                    return cartItem;
                })
                .filter(CartItem::isExpired)
                .collect(Collectors.toList());

        timeTaken();
        if(cartItems.size()>0){
            return new CheckoutResponse(CheckoutStatus.FAILURE,cartItems);
        }

//        double finalRate=calculateFinalRate(cart);
        double finalRate=calculateFinalRate_reduce(cart);
        log("Checkout Final Rate is : "+finalRate);
        return new CheckoutResponse(CheckoutStatus.SUCCESS,finalRate);
    }

    private double calculateFinalRate(Cart cart) {
        return cart.getCartItemList()
                .parallelStream()
                .map(cartItem -> {
                    log("Rate :: "+cartItem.getRate()+" Quantity :: "+cartItem.getQuantity());
                    return cartItem.getRate() * cartItem.getQuantity();
                }).collect(summingDouble(Double::doubleValue));
//                .mapToDouble(Double::doubleValue)
//                .sum();
    }

    private double calculateFinalRate_reduce(Cart cartItems) {
        return cartItems.getCartItemList()
                .parallelStream()
                .map(cartItem -> {
                    return cartItem.getRate() * cartItem.getQuantity();
                })
                .reduce(0.0, Double::sum);
    }
}
