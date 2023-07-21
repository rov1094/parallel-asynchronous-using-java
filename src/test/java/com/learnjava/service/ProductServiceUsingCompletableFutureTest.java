package com.learnjava.service;

import com.learnjava.domain.Product;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceUsingCompletableFutureTest {

    private ProductInfoService productInfoService=new ProductInfoService();
    private ReviewService reviewService=new ReviewService();

    ProductServiceUsingCompletableFuture productService=new ProductServiceUsingCompletableFuture(productInfoService,reviewService);

    ProductServiceUsingCompletableFuture productServiceInventory=new ProductServiceUsingCompletableFuture(productInfoService,reviewService,new InventoryService());
    @Test
    void retrieveProductDetails() {
        String productId="ABC123";
        Product product=productService.retrieveProductDetails(productId);
        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size()>0);
        assertNotNull(product.getReview());
    }

    @Test
    void retrieveProductDetailsWithInventory() {
        String productId="ABC123";
        Product product=productServiceInventory.retrieveProductDetailsWithInventory(productId);
        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size()>0);
        product.getProductInfo().getProductOptions().forEach(productOption -> assertNotNull(productOption.getInventory()));
        assertNotNull(product.getReview());
    }

    @Test
    void retrieveProductDetailsWithInventory_approach2() {
        String productId="ABC123";
        Product product=productServiceInventory.retrieveProductDetailsWithInventory_approach2(productId);
        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size()>0);
        product.getProductInfo().getProductOptions().forEach(productOption -> assertNotNull(productOption.getInventory()));
        assertNotNull(product.getReview());
    }

    @Test
    void retrieveProductDetails_approach2() throws ExecutionException, InterruptedException {
        String productId="ABC123";
        Product product=productService.retrieveProductDetails_approach2(productId).get();//.join();
        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size()>0);
        assertNotNull(product.getReview());
    }
}