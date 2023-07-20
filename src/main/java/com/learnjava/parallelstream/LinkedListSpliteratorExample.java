package com.learnjava.parallelstream;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;

public class LinkedListSpliteratorExample {

    public List<Integer> multiplyValue(LinkedList<Integer> inputList, int multipleValue, boolean isParallel){
        Stream<Integer> integerStream=inputList.stream();
        startTimer();
        if(isParallel)
            integerStream.parallel();

        List<Integer> resultList=integerStream
                .map(integer->integer*multipleValue)
                .collect(Collectors.toList());
        timeTaken();
        return resultList;
    }
}
