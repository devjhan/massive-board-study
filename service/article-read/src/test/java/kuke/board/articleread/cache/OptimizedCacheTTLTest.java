package kuke.board.articleread.cache;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class OptimizedCacheTTLTest {
    @Test
    void ofTest() {
        long ttlSeconds = 10;
        OptimizedCacheTTL cacheTTL = OptimizedCacheTTL.of(ttlSeconds);

        Assertions.assertThat(cacheTTL.getLogicalTTL()).isEqualTo(cacheTTL.getPhysicalTTL().minusSeconds(OptimizedCacheTTL.PHYSICAL_TTL_DELAY_SECONDS));
    }
}