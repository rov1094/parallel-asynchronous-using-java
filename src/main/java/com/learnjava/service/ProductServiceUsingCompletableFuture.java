package com.learnjava.service;

import com.learnjava.domain.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.LoggerUtil.log;

public class ProductServiceUsingCompletableFuture {
    private ProductInfoService productInfoService;
    private ReviewService reviewService;

    private InventoryService inventoryService;

    public ProductServiceUsingCompletableFuture(ProductInfoService productInfoService, ReviewService reviewService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
    }

    public ProductServiceUsingCompletableFuture(ProductInfoService productInfoService, ReviewService reviewService,InventoryService inventoryService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
        this.inventoryService=inventoryService;
    }


    public Product retrieveProductDetails(String productId) {
        stopWatch.start();
        CompletableFuture<ProductInfo> productInfoCompletableFuture=CompletableFuture.supplyAsync(()->productInfoService.retrieveProductInfo(productId));
        CompletableFuture<Review> reviewCompletableFuture=CompletableFuture.supplyAsync(()->reviewService.retrieveReviews(productId));
        Product product=productInfoCompletableFuture
                .thenCombine(reviewCompletableFuture,(productInfo,review)->new Product(productId, productInfo, review))
                .join(); // block the thread
        stopWatch.stop();
        log("Total Time Taken : "+ stopWatch.getTime());
        return product;
    }

    public Product retrieveProductDetailsWithInventory(String productId) {
        stopWatch.start();
        CompletableFuture<ProductInfo> productInfoCompletableFuture=CompletableFuture
                .supplyAsync(()->productInfoService.retrieveProductInfo(productId))
                .thenApply(productInfo -> {
                    productInfo.setProductOptions(updateInventory(productInfo.getProductOptions()));
                    return productInfo;
                });

        CompletableFuture<Review> reviewCompletableFuture=CompletableFuture.supplyAsync(()->reviewService.retrieveReviews(productId));
        Product product=productInfoCompletableFuture
                .thenCombine(reviewCompletableFuture,(productInfo,review)->new Product(productId, productInfo, review))
                .join(); // block the thread
        stopWatch.stop();
        log("Total Time Taken : "+ stopWatch.getTime());
        return product;
    }

    public Product retrieveProductDetailsWithInventory_approach2(String productId) {
        stopWatch.start();
        CompletableFuture<ProductInfo> productInfoCompletableFuture=CompletableFuture
                .supplyAsync(()->productInfoService.retrieveProductInfo(productId))
                .thenApply(productInfo -> {
                    productInfo.setProductOptions(updateInventory_approach2(productInfo.getProductOptions()));
                    return productInfo;
                });

        CompletableFuture<Review> reviewCompletableFuture=CompletableFuture.supplyAsync(()->reviewService.retrieveReviews(productId));
        Product product=productInfoCompletableFuture
                .thenCombine(reviewCompletableFuture,(productInfo,review)->new Product(productId, productInfo, review))
                .join(); // block the thread
        stopWatch.stop();
        log("Total Time Taken : "+ stopWatch.getTime());
        return product;
    }

    private List<ProductOption> updateInventory(List<ProductOption> productOptions) {

       List<ProductOption> productOptionList= productOptions.stream()
                .map(productOption -> {
                    Inventory inventory=inventoryService.addInventory(productOption);// this is causing delay of 500ms for each addition of inventory
                    productOption.setInventory(inventory);
                    return productOption;
                }).collect(Collectors.toList());
       return productOptionList;
    }

    private List<ProductOption> updateInventory_approach2(List<ProductOption> productOptions) {

        List<CompletableFuture<ProductOption>> productOptionList= productOptions.stream()
                .map(productOption -> {
                    CompletableFuture<ProductOption> productOptionCF=CompletableFuture
                            .supplyAsync(()->inventoryService.addInventory(productOption))
                            .thenApply(inventory -> {
                                productOption.setInventory(inventory);
                                return productOption;
                            });
                    return productOptionCF;
                }).collect(Collectors.toList());
        return productOptionList.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }


    public CompletableFuture<Product> retrieveProductDetails_approach2(String productId) {
        CompletableFuture<ProductInfo> productInfoCompletableFuture=CompletableFuture.supplyAsync(()->productInfoService.retrieveProductInfo(productId));
        CompletableFuture<Review> reviewCompletableFuture=CompletableFuture.supplyAsync(()->reviewService.retrieveReviews(productId));
        return productInfoCompletableFuture
                .thenCombine(reviewCompletableFuture,(productInfo,review)->new Product(productId, productInfo, review));
    }

    public static void main(String[] args) {

        ProductInfoService productInfoService = new ProductInfoService();
        ReviewService reviewService = new ReviewService();
        ProductServiceUsingCompletableFuture productService = new ProductServiceUsingCompletableFuture(productInfoService, reviewService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is " + product);

    }


}
