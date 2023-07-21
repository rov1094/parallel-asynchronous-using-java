package com.learnjava.concurrent.completablefuture;

import com.learnjava.service.HelloWorldService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class CompletableFutureHelloWorldServiceTest {

    HelloWorldService hws =new HelloWorldService();
    CompletableFutureHelloWorldService cfhws=new CompletableFutureHelloWorldService(hws);

    @Test
    void helloWorld() {
        CompletableFuture<String> result= cfhws.helloWorld();
        result.thenAccept((s)->{
            assertEquals("HELLO WORLD",s);
        })
        .join(); // Very Important to Join to wait for the assertion to get completed
    }

    @Test
    void helloWorld_withSize() {
        CompletableFuture<String> result= cfhws.helloWorld_withSize();
        result.thenAccept((s)->{
                    assertEquals("11 - HELLO WORLD",s);
                })
                .join(); // Very Important to Join to wait for the assertion to get completed
    }

    @Test
    void helloworld_multiple_async_calls(){
        CompletableFuture<String> result= cfhws.helloworld_multiple_async_calls();
        result.thenAccept((s)->{
                    assertEquals("HELLO WORLD!",s);
                })
                .join(); // Very Important to Join to wait for the assertion to get completed
    }

    @Test
    void helloworld_3_async_calls(){
        CompletableFuture<String> result= cfhws.helloworld_3_async_calls();
        result.thenAccept((s)->{
                    assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!",s);
                })
                .join(); // Very Important to Join to wait for the assertion to get completed
    }

    @Test
    void helloworld_4_async_calls(){
        String result= cfhws.helloWorld_4_async_calls();
        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE! BYE!",result);
    }

    @Test
    void helloWorld_thenCompose() {
        CompletableFuture<String> result= cfhws.helloWorld_thenCompose();
        result.thenAccept((s)->{
                    assertEquals("HELLO WORLD!",s);
                })
                .join(); // Very Important to Join to wait for the assertion to get completed
    }
}