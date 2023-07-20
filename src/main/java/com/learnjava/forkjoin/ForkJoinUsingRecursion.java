package com.learnjava.forkjoin;

import com.learnjava.util.DataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

import static com.learnjava.util.CommonUtil.delay;
import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.LoggerUtil.log;

public class ForkJoinUsingRecursion extends RecursiveTask<List<String>> {

    private List<String> inputList;

    ForkJoinUsingRecursion(List<String> inputList){
        this.inputList=inputList;
    }

    public static void main(String[] args) {

        stopWatch.start();
        List<String> resultList = new ArrayList<>();
        List<String> names = DataSet.namesList();
        log("names : "+ names);
        ForkJoinPool forkJoinPool=new ForkJoinPool();
        //Constructing recursive task
        ForkJoinUsingRecursion forkJoinUsingRecursionTask=new ForkJoinUsingRecursion(names);
        resultList=forkJoinPool.invoke(forkJoinUsingRecursionTask);
        stopWatch.stop();
        log("Final Result : "+ resultList);
        log("Total Time Taken : "+ stopWatch.getTime());
    }


    private static String addNameLengthTransform(String name) {
        delay(500);
        return name.length()+" - "+name ;
    }

    //This is where our computation will happen
    @Override
    protected List<String> compute() {
        int midPoint=inputList.size()/2;
        //For Breaking the recursion
        if(inputList.size()<=1){
            List<String> resultList=new ArrayList<>();
            inputList.forEach(name->resultList.add(addNameLengthTransform(name)));
            return resultList;
        }
        //Submitted to ForkJoinTask for left input value
        //Forking the sublist
        ForkJoinTask<List<String>> leftInputListTask=new ForkJoinUsingRecursion(inputList.subList(0,midPoint)).fork();
        inputList=inputList.subList(midPoint,inputList.size());
        List<String> rightResult=compute(); //Recursion Happens
        List<String> leftResult=leftInputListTask.join();
        leftResult.addAll(rightResult);
        return leftResult;
    }
}
