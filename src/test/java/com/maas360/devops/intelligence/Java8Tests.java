/* created by nikita */
package com.maas360.devops.intelligence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Java8Tests {
    @Test
    public void listOfListOfDouble()
    {
        List<List<Double>> outerList = Arrays.asList(Arrays.asList(1D,100D),Arrays.asList(2D,200D));

        //outerList.stream().filter(x->x.get(0););
    }




}
