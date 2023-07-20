package com.learnjava.parallelstream;

import com.learnjava.util.DataSet;
import org.junit.jupiter.api.RepeatedTest;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LinkedListSpliteratorExampleTest {

    LinkedListSpliteratorExample linkedListSpilteratorExample =new LinkedListSpliteratorExample();

    @RepeatedTest(5)
    void multiplyValueSequential() {
        int size=100000;
        LinkedList<Integer> inputList=DataSet.generateIntegerLinkedList((size));
        List<Integer> resultList= linkedListSpilteratorExample.multiplyValue(inputList,2,false);
        assertEquals(size,resultList.size());
    }

    @RepeatedTest(5)
    void multiplyValueParallel() {
        int size=100000;
        LinkedList<Integer> inputList=DataSet.generateIntegerLinkedList((size));
        List<Integer> resultList= linkedListSpilteratorExample.multiplyValue(inputList,2,true);
        assertEquals(size,resultList.size());
    }
}