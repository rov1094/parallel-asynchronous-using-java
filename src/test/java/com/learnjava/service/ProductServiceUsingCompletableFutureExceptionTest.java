package com.learnjava.service;

import com.learnjava.domain.Product;
import com.learnjava.domain.ProductInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceUsingCompletableFutureExceptionTest {

    @Mock
    private ProductInfoService pisService;

    @Mock
    private ReviewService reviewService;

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private ProductServiceUsingCompletableFuture pscf;

    @Test
    void retrieveProductDetailsWithInventory_approach2() {
        String productId="ABC123";
        when(pisService.retrieveProductInfo(any())).thenCallRealMethod();
        when(reviewService.retrieveReviews(any())).thenThrow(new RuntimeException("Error Occured"));
        when(inventoryService.addInventory(any())).thenCallRealMethod();
        Product product=pscf.retrieveProductDetailsWithInventory_approach2(productId);
        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size()>0);
        product.getProductInfo().getProductOptions().forEach(productOption -> assertNotNull(productOption.getInventory()));
        assertNotNull(product.getReview());
        assertEquals(0,product.getReview().getNoOfReviews());
    }

    @Test
    void retrieveProductDetailsWithInventory_productTest() {
        //given
        String productId="ABC123";
        //When
        when(pisService.retrieveProductInfo(any())).thenThrow(new RuntimeException("Error Occured"));
        when(reviewService.retrieveReviews(any())).thenCallRealMethod();
//        when(inventoryService.addInventory(any())).thenCallRealMethod(); //No Need as we will not reach here
        //Then
        Assertions.assertThrows(RuntimeException.class,()->pscf.retrieveProductDetailsWithInventory_approach2(productId));
    }

    @Test
    void retrieveProductDetailsWithInventory_inventoryTest() {
        //given
        String productId="ABC123";
        //When
        when(pisService.retrieveProductInfo(any())).thenCallRealMethod();
        when(reviewService.retrieveReviews(any())).thenCallRealMethod();
        when(inventoryService.addInventory(any())).thenThrow(new RuntimeException("Exception Occured in Inventory"));
        //Then
        Product product=pscf.retrieveProductDetailsWithInventory_approach2(productId);
        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size()>0);
        product.getProductInfo().getProductOptions().forEach(productOption -> assertNotNull(productOption.getInventory()));
        product.getProductInfo().getProductOptions().forEach(productOption -> {
            assertNotNull(productOption.getInventory());
            assertEquals(1,productOption.getInventory().getCount());
        });
        assertNotNull(product.getReview());
    }
}