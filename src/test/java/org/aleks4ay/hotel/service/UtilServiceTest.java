package org.aleks4ay.hotel.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UtilServiceTest {
    private UtilService<Integer> utilService = new UtilService<Integer>();
    private List<Integer> integers = new ArrayList<>();

    @BeforeEach
    public void init() {
        integers = Stream.iterate(1, n -> n + 1).limit(18).collect(Collectors.toList());
    }

    @Test
    public void testDoPagination() throws Exception {
        List<Integer> actual = utilService.doPagination(5, 1, integers);
        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5);
        assertEquals(expected, actual);
    }

    @Test
    public void testDoPaginationIfEndPage() throws Exception {
        List<Integer> actual = utilService.doPagination(5, 4, integers);
        List<Integer> expected = Arrays.asList(16, 17, 18);
        assertEquals(expected, actual);
    }

    @Test
    public void testDoPaginationAfterEndPage() throws Exception {
        List<Integer> actual = utilService.doPagination(5, 5, integers);
        List expected = Collections.EMPTY_LIST;
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDate() throws Exception {
        LocalDate result = UtilService.getDate("2021-09-08");
        assertEquals(LocalDate.of(2021, Month.SEPTEMBER, 8), result);
    }
}

