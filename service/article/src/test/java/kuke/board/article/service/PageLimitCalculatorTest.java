package kuke.board.article.service;

import kuke.board.common.pagelimitcalculator.PageLimitCalculator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PageLimitCalculatorTest {

    @Test
    void calculatePageLimitTest() {
        calculatePageLimitTest(1L, 30L, 10L, 301L);
        calculatePageLimitTest(2L, 30L, 10L, 301L);
        calculatePageLimitTest(3L, 30L, 10L, 301L);
        calculatePageLimitTest(11L, 30L, 10L, 601L);
        calculatePageLimitTest(1L, 30L, 15L, 451L);
        calculatePageLimitTest(1L, 50L, 10L, 501L);
        calculatePageLimitTest(1L, 50L, 15L, 751L);
    }


    void calculatePageLimitTest(Long currentPage, Long pageSize, Long maxPages, Long expected) {
         Long result = PageLimitCalculator.calculatePageLimit(currentPage, pageSize, maxPages);
         Assertions.assertThat(result).isEqualTo(expected);
    }
}