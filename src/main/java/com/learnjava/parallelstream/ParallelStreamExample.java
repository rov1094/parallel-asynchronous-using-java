package com.learnjava.parallelstream;

import com.learnjava.util.DataSet;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.learnjava.util.CommonUtil.*;
import static com.learnjava.util.LoggerUtil.log;

public class ParallelStreamExample {

    public static void main(String[] args) {
        List<String> names=DataSet.namesList();
        ParallelStreamExample parallelStreamExample=new ParallelStreamExample();
        startTimer();
        List<String> resultWithParallelStream=parallelStreamExample.stringTransformWithParallelStream(names);
        timeTaken();

        log("Result with Parallel Stream : "+resultWithParallelStream);
        startTimer();
        resultWithParallelStream=parallelStreamExample.string_toLowerCase(names);
        timeTaken();
        log("Result with Parallel Stream : "+resultWithParallelStream);
    }
    /**
     * Takes close to 600ms with Stream it was taking close to 2000ms
     */
    public List<String> stringTransformWithParallelStream(List<String> namesList) {
        return namesList.parallelStream().map(this::addNameLengthTransform).collect(Collectors.toList());
    }

    /**
     * Example using parallel
     */
    public List<String> stringTransformWithParallelStream_1(List<String> namesList,boolean isParallel) {
        Stream<String> namesStream=namesList.stream();

        if(isParallel){
            namesStream.parallel();
        }
        return namesStream.map(this::addNameLengthTransform).collect(Collectors.toList());
    }

    private String addNameLengthTransform(String name) {
        delay(500);
        return name.length()+" - "+name ;
    }

    public List<String> string_toLowerCase(List<String> names){
        return names.parallelStream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }
}
