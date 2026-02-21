package kuke.board.common.pagelimitcalculator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class PageLimitCalculator {

    public static Long calculatePageLimit(Long currentPage, Long pageSize, Long maxPages) {
        return (((currentPage - 1) / maxPages) + 1) * pageSize * maxPages + 1;
    }
}
