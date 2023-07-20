package com.learnjava.parallelstream;

import com.learnjava.util.DataSet;
import org.junit.jupiter.api.RepeatedTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArrayListSpliteratorExampleTest {

    ArrayListSpilteratorExample arrayListSpilteratorExample=new ArrayListSpilteratorExample();

    @RepeatedTest(5)
    void multiplyValueSequential() {
        int size=100000;
        ArrayList<Integer> inputList=DataSet.generateArrayList(size);
        List<Integer> resultList=arrayListSpilteratorExample.multiplyValue(inputList,2,false);
        assertEquals(size,resultList.size());
    }

    @RepeatedTest(5)
    void multiplyValueParallel() {
        int size=100000;
        ArrayList<Integer> inputList=DataSet.generateArrayList(size);
        List<Integer> resultList=arrayListSpilteratorExample.multiplyValue(inputList,2,true);
        assertEquals(size,resultList.size());
    }
}