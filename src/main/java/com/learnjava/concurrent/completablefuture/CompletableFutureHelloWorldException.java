package com.learnjava.concurrent.completablefuture;

import com.learnjava.service.HelloWorldService;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.CommonUtil.delay;
import static com.learnjava.util.LoggerUtil.log;

public class CompletableFutureHelloWorldException {
    HelloWorldService hws;

    public CompletableFutureHelloWorldException(HelloWorldService hws) {
        this.hws = hws;
    }


    public CompletableFuture<String> helloworld_3_async_calls_hanlde(){
        CompletableFuture<String> helloCF=CompletableFuture.supplyAsync(hws::hello).handle((result,e)->{
            log("Exception for value "+result+" is : "+e.getMessage()); // Only invoked when exception occurred
            return "";// Recoverable value
        });
        CompletableFuture<String> worldCF=CompletableFuture.supplyAsync(hws::world);
        CompletableFuture<String> hiCompletableFuture=CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " Hi CompletableFuture!";
        });

        return helloCF
                .thenCombine(worldCF,(h,w)->h+w)
                .thenCombine(hiCompletableFuture,(previous,current)->previous+current)
                .thenApply(String::toUpperCase);
    }

    public CompletableFuture<String> helloworld_3_async_calls_handle_approach2(){
        CompletableFuture<String> helloCF=CompletableFuture.supplyAsync(hws::hello).handle((result,e)->{
            if(e!=null){
                log("Exception for value in hello "+result+" is : "+e.getMessage()); // Only invoked when exception occurred
                return "";// Recoverable value
            }
            return result;
        });
        CompletableFuture<String> worldCF=CompletableFuture.supplyAsync(hws::world)
                .handle((result,e)->{ // Handle Method is Always called
                    if(e!=null){
                        log("Exception for value in world "+result+" is : "+e.getMessage()); // Only invoked when exception occurred
                        return "";// Recoverable value
                    }
                    return result;
                });
        CompletableFuture<String> hiCompletableFuture=CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " Hi CompletableFuture!";
        });

        return helloCF
                .thenCombine(worldCF,(h,w)->h+w)
                .thenCombine(hiCompletableFuture,(previous,current)->previous+current)
                .thenApply(String::toUpperCase);
    }

    public CompletableFuture<String> helloworld_3_async_calls_expectionally(){
        CompletableFuture<String> helloCF=CompletableFuture.supplyAsync(hws::hello)
                .exceptionally((e)->{
                log("Exception in hello is : "+e.getMessage()); // Only invoked when exception occurred
                return "";// Recoverable value
        });
        CompletableFuture<String> worldCF=CompletableFuture.supplyAsync(hws::world)
                .exceptionally((e)->{
                    log("Exception in world is : "+e.getMessage()); // Only invoked when exception occurred
                    return "";// Recoverable value
                });
        CompletableFuture<String> hiCompletableFuture=CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " Hi CompletableFuture!";
        });

        return helloCF
                .thenCombine(worldCF,(h,w)->h+w)
                .thenCombine(hiCompletableFuture,(previous,current)->previous+current)
                .thenApply(String::toUpperCase);
    }

    public CompletableFuture<String> helloworld_3_async_calls_whenComplete(){
        CompletableFuture<String> helloCF=CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> worldCF=CompletableFuture.supplyAsync(hws::world);
        CompletableFuture<String> hiCompletableFuture=CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " Hi CompletableFuture!";
        });

        return helloCF
                .whenComplete((res,e)->{
                    log("Result is :"+e);
                    log(e.getMessage());
                })
                .thenCombine(worldCF,(h,w)->h+w)
                .whenComplete((res,e)->{
                    log("Result is :"+e);
                    log(e.getMessage());
                }).exceptionally((e)->{
                    log("Exceptionally ::"+e.getMessage());
                    return "";
                })
                .thenCombine(hiCompletableFuture,(previous,current)->previous+current)
                .thenApply(String::toUpperCase);
    }
}
