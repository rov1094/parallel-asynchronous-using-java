package com.learnjava.service;

import com.learnjava.domain.Product;
import com.learnjava.domain.ProductInfo;
import com.learnjava.domain.Review;

import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.LoggerUtil.log;

public class ProductServiceusingThread {
    private ProductInfoService productInfoService;
    private ReviewService reviewService;

    public ProductServiceusingThread(ProductInfoService productInfoService, ReviewService reviewService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
    }

    public Product retrieveProductDetails(String productId) throws InterruptedException {
        stopWatch.start();
        ProductInfoRunnable productInfoRunnable=new ProductInfoRunnable(productId);
        Thread productInfoThread=new Thread(productInfoRunnable);

        ReviewRunnable reviewRunnable=new ReviewRunnable(productId);
        Thread reviewThread=new Thread(reviewRunnable);

        productInfoThread.start();
        reviewThread.start();

        productInfoThread.join();
        reviewThread.join();

        ProductInfo productInfo=productInfoRunnable.getProductInfo();
        Review review=reviewRunnable.getReview();

        stopWatch.stop();
        log("Total Time Taken : "+ stopWatch.getTime());
        return new Product(productId, productInfo, review);
    }

    public static void main(String[] args) throws InterruptedException {

        ProductInfoService productInfoService = new ProductInfoService();
        ReviewService reviewService = new ReviewService();
        ProductServiceusingThread productService = new ProductServiceusingThread(productInfoService, reviewService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is " + product);

    }

    private class ProductInfoRunnable implements Runnable {

        private ProductInfo productInfo;

        private String productId;
        public ProductInfoRunnable(String productId) {
            this.productId=productId;
        }

        public ProductInfo getProductInfo() {
            return productInfo;
        }

        @Override
        public void run() {
            productInfo=productInfoService.retrieveProductInfo(productId);
        }
    }

    private class ReviewRunnable implements Runnable {

        private Review review;
        private String productId;
        public ReviewRunnable(String productId) {
            this.productId=productId;
        }

        public Review getReview() {
            return review;
        }

        @Override
        public void run() {
            review=reviewService.retrieveReviews(productId);
        }
    }
}
