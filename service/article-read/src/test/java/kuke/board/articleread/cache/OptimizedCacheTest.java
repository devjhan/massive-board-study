package kuke.board.articleread.cache;

import lombok.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class OptimizedCacheTest {
    @Test
    void parseDataTest() {
        parseCacheTest("data", 12L);
        parseCacheTest(LocalDateTime.now(), 10L);
        parseCacheTest(192, 10L);
        parseCacheTest(new TestClass("sex"), 10L);
    }

    void parseCacheTest(Object data, long ttlSeconds) {
        OptimizedCache cache = OptimizedCache.of(data, Duration.ofSeconds(ttlSeconds));
        System.out.println("cache: " + cache);

        Object resolvedData = cache.parseData(data.getClass());
        System.out.println("resolvedData: " + resolvedData);
        assertThat(resolvedData).isEqualTo(data);
    }

    @Test
    void isExpiredTest() {
     assertThat(OptimizedCache.of("data", Duration.ofDays(-10)).isExpired()).isTrue();
     assertThat(OptimizedCache.of("data", Duration.ofDays(30)).isExpired()).isFalse();
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    static class TestClass {
        String testData;
    }
}