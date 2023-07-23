package com.learnjava.concurrent.completablefuture;

import com.learnjava.service.HelloWorldService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class CompletableFutureHelloWorldExceptionTest {
    @InjectMocks
    CompletableFutureHelloWorldException completableFutureHelloWorldException;

    @Mock
    HelloWorldService hws=mock(HelloWorldService.class);

    @Test
    void helloworld_3_async_calls(){
        when(hws.hello()).thenThrow(new RuntimeException("Exception Thrown"));
        when(hws.world()).thenCallRealMethod();
        String result=completableFutureHelloWorldException.helloworld_3_async_calls_hanlde().join();
        assertEquals(" WORLD! HI COMPLETABLEFUTURE!",result);
    }

    @Test
    void helloworld_3_async_calls_approach2(){
        when(hws.hello()).thenThrow(new RuntimeException("Exception Thrown"));
        when(hws.world()).thenThrow(new RuntimeException("Exception Thrown"));
        String result=completableFutureHelloWorldException.helloworld_3_async_calls_handle_approach2().join();
        assertEquals(" HI COMPLETABLEFUTURE!",result);
    }

    @Test
    void helloworld_3_async_calls_approach3(){
        when(hws.hello()).thenCallRealMethod();
        when(hws.world()).thenCallRealMethod();
        String result=completableFutureHelloWorldException.helloworld_3_async_calls_handle_approach2().join();
        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!",result);
    }


    @Test
    void helloworld_3_async_calls_exceptionally(){
        when(hws.hello()).thenThrow(new RuntimeException("Exception Thrown"));
        when(hws.world()).thenThrow(new RuntimeException("Exception Thrown"));
        String result=completableFutureHelloWorldException.helloworld_3_async_calls_expectionally().join();
        assertEquals(" HI COMPLETABLEFUTURE!",result);
    }


    @Test
    void helloworld_3_async_calls_whenComplete(){
        when(hws.hello()).thenThrow(new RuntimeException("Exception Thrown"));
        when(hws.world()).thenThrow(new RuntimeException("Exception Thrown"));
        String result=completableFutureHelloWorldException.helloworld_3_async_calls_whenComplete().join();
        assertEquals(" HI COMPLETABLEFUTURE!",result);
    }

}