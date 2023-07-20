package com.learnjava.parallelstream;

import com.learnjava.util.DataSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.learnjava.util.CommonUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class ParallelStreamExampleTest {

    ParallelStreamExample parallelStreamExample=new ParallelStreamExample();

    @Test
    void stringTransformWithParallelStream() {
        List<String> names=DataSet.namesList();
        startTimer();
        List<String> resultList=parallelStreamExample.stringTransformWithParallelStream(names);
        timeTaken();
        //Validate Size of the result
        assertEquals(4,resultList.size());
        Consumer<String> validateDash=s-> assertTrue(s.contains("-"));
        resultList.forEach(validateDash);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false,true})
    void stringTransformWithParallelStream_1(boolean isParallel) {
        List<String> names=DataSet.namesList();
        startTimer();
        List<String> resultList=parallelStreamExample.stringTransformWithParallelStream_1(names,isParallel);
        timeTaken();
        //Validate Size of the result
        assertEquals(4,resultList.size());
        Consumer<String> validateDash=s-> assertTrue(s.contains("-"));
        resultList.forEach(validateDash);
    }

    @Test
    void string_toLowerCaseTest(){
        List<String> names= DataSet.namesList();
        startTimer();
        List<String> result=parallelStreamExample.string_toLowerCase(names);
        timeTaken();
        assertEquals(names.size(),result.size());
    }

}