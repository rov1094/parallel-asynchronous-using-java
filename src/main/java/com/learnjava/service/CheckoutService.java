package com.learnjava.service;

import com.learnjava.domain.checkout.Cart;
import com.learnjava.domain.checkout.CartItem;
import com.learnjava.domain.checkout.CheckoutResponse;
import com.learnjava.domain.checkout.CheckoutStatus;

import java.util.List;
import java.util.stream.Collectors;

import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;

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

        return new CheckoutResponse(CheckoutStatus.SUCCESS);
    }
}
