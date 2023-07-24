package com.learnjava.concurrent.completablefuture;

import com.learnjava.service.HelloWorldService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.learnjava.util.CommonUtil.*;
import static com.learnjava.util.LoggerUtil.log;

public class CompletableFutureHelloWorldService {

    HelloWorldService hws;

    public CompletableFutureHelloWorldService(HelloWorldService hws) {
        this.hws = hws;
    }


    public CompletableFuture<String> helloWorld(){
        return CompletableFuture
                .supplyAsync(hws::helloWorld)
                .thenApply(String::toUpperCase);
    }

    public CompletableFuture<String> helloWorld_thenCompose(){
        return CompletableFuture
                .supplyAsync(hws::hello)
                .thenCompose(input->hws.worldFuture(input))// This waits for the dependent task to finish and then return the value
                .thenApply(String::toUpperCase);
    }

    public CompletableFuture<String> helloWorld_withSize(){
        return CompletableFuture
                .supplyAsync(hws::helloWorld)
                .thenApply(String::toUpperCase)
                .thenApply(s->s.length()+" - "+s);
    }

    public String standardWay(){
        String hello=hws.hello();
        String world=hws.world();
        return hello+world;
    }

    public CompletableFuture<String> helloworld_multiple_async_calls(){
        CompletableFuture<String> helloCF=CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> worldCF=CompletableFuture.supplyAsync(hws::world);

        return helloCF
                .thenCombine(worldCF,(h,w)->h+w) // Here we are combining helloCF with WorldCF and (h->output of hello,w->output of w)
                .thenApply(String::toUpperCase);
    }

    public CompletableFuture<String> helloworld_3_async_calls(){
        CompletableFuture<String> helloCF=CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> worldCF=CompletableFuture.supplyAsync(hws::world);
        CompletableFuture<String> hiCompletableFuture=CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " Hi CompletableFuture!";
        });

        return helloCF
                .thenCombine(worldCF,(h,w)->h+w) // Here we are combining helloCF with WorldCF and (h->output of hello,w->output of w)
                .thenCombine(hiCompletableFuture,(previous,current)->previous+current) //previous : Hello World , current : Hi CompletableFuture
                .thenApply(String::toUpperCase);
    }

    public CompletableFuture<String> helloworld_3_async_calls_log(){
        CompletableFuture<String> helloCF=CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> worldCF=CompletableFuture.supplyAsync(hws::world);
        CompletableFuture<String> hiCompletableFuture=CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " Hi CompletableFuture!";
        });

        return helloCF
                .thenCombine(worldCF,(h,w)->{
                    log("Then Combine h/w");
                    return h+w;
                }) // Here we are combining helloCF with WorldCF and (h->output of hello,w->output of w)
                .thenCombine(hiCompletableFuture,(previous,current)->{
                    log("Then Combine prev/current");
                    return previous+current;
                }) //previous : Hello World , current : Hi CompletableFuture
                .thenApply(s->{
                    log("Then Apply");
                    return s.toUpperCase();
                });
    }

    public CompletableFuture<String> helloworld_3_async_calls_async(){
        CompletableFuture<String> helloCF=CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> worldCF=CompletableFuture.supplyAsync(hws::world);
        CompletableFuture<String> hiCompletableFuture=CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " Hi CompletableFuture!";
        });

        return helloCF
                .thenCombineAsync(worldCF,(h,w)->{
                    log("Then Combine h/w");
                    return h+w;
                }) // Here we are combining helloCF with WorldCF and (h->output of hello,w->output of w)
                .thenCombineAsync(hiCompletableFuture,(previous,current)->{
                    log("Then Combine prev/current");
                    return previous+current;
                }) //previous : Hello World , current : Hi CompletableFuture
                .thenApplyAsync(s->{
                    log("Then Apply");
                    return s.toUpperCase();
                });
    }

    public CompletableFuture<String> helloworld_3_async_calls_customThreadPool(){

        ExecutorService executorService= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        CompletableFuture<String> helloCF=CompletableFuture.supplyAsync(hws::hello,executorService);
        CompletableFuture<String> worldCF=CompletableFuture.supplyAsync(hws::world,executorService);
        CompletableFuture<String> hiCompletableFuture=CompletableFuture.supplyAsync(()->{
            delay(1000);
            log("in Hi CompletableFuture");
            return " Hi CompletableFuture!";
        },executorService);

        return helloCF
                .thenCombine(worldCF,(h,w)->{
                    log("Then Combine h/w");
                    return h+w;
                }) // Here we are combining helloCF with WorldCF and (h->output of hello,w->output of w)
                .thenCombine(hiCompletableFuture,(previous,current)->{
                    log("Then Combine prev/current");
                    return previous+current;
                }) //previous : Hello World , current : Hi CompletableFuture
                .thenApply(s->{
                    log("Then Apply");
                    return s.toUpperCase();
                });
    }

    public CompletableFuture<String> helloworld_3_async_calls_customThreadPool_async(){

        ExecutorService executorService= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        CompletableFuture<String> helloCF=CompletableFuture.supplyAsync(hws::hello,executorService);
        CompletableFuture<String> worldCF=CompletableFuture.supplyAsync(hws::world,executorService);
        CompletableFuture<String> hiCompletableFuture=CompletableFuture.supplyAsync(()->{
            delay(1000);
            log("in Hi CompletableFuture");
            return " Hi CompletableFuture!";
        },executorService);

        return helloCF
                .thenCombineAsync(worldCF,(h,w)->{
                    log("Then Combine h/w");
                    return h+w;
                },executorService) // Here we are combining helloCF with WorldCF and (h->output of hello,w->output of w)
                .thenCombineAsync(hiCompletableFuture,(previous,current)->{
                    log("Then Combine prev/current");
                    return previous+current;
                },executorService) //previous : Hello World , current : Hi CompletableFuture
                .thenApplyAsync(s->{
                    log("Then Apply");
                    return s.toUpperCase();
                },executorService);
    }

    /**
     * Send response from any of the competableFuture and discard the other
     * @return
     */
    public String anyOf(){
        //Db
        CompletableFuture<String> db=CompletableFuture.supplyAsync(()->{
            delay(4000);
           log("Response from DB");
           return "Hello World!";
        });

        //Rest Call
        CompletableFuture<String> restCall=CompletableFuture.supplyAsync(()->{
            delay(2000);
            log("Response from restCall");
            return "Hello World!";
        });

        //SOAP Call
        CompletableFuture<String> soapCall=CompletableFuture.supplyAsync(()->{
            delay(3000);
            log("Response from soapCall");
            return "Hello World!";
        });
        var cfList=List.of(db,restCall,soapCall);
        CompletableFuture<Object> resultCf=CompletableFuture.anyOf(cfList.toArray(new CompletableFuture[cfList.size()]));

        String result=(String) resultCf.thenApply(v->{
            if(v instanceof String){
                return v;
            }
            return null;
        }).join();
        return result;
    }

    public static void main(String[] args) {
        HelloWorldService hws=new HelloWorldService();
        CompletableFuture
                .supplyAsync(hws::helloWorld)//()->hws.helloWorld()) // Accepts Consumer Functional Interface
                .thenApply(String::toUpperCase) // Accepts Function Functional Interface
                .thenAccept((result)-> {
                   log("Result is :"+result);
                }) // Accepts Consumer Functional Interface
                .join();// Join Method to wait for the computation to be finished
        log("Done!!");
        delay(2000); // Added a delay to get the result as main thread is freed up and gets completed and terminated
        //But can't we get the result without delay? : we can use Join for this but this will lead blocking of main thread
    }

    public String helloWorld_4_async_calls() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> this.hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> this.hws.world());
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " HI CompletableFuture!";
        });
        // Add the 4th CompletableFuture that returns a String "  Bye!"
        CompletableFuture<String> byeCompletableFuture=CompletableFuture.supplyAsync(()-> {
            delay(1000);
            return " Bye!";
        });

        String hw = hello
                .thenCombine(world, (h, w) -> h + w) // (first,second)
                .thenCombine(hiCompletableFuture, (previous, current) -> previous + current)
                .thenCombine(byeCompletableFuture,(previous,current)->previous+current)
                // Combine the fourth CompletableFuture
                .thenApply(String::toUpperCase)
                .join();

        timeTaken();

        return hw;
    }
}
