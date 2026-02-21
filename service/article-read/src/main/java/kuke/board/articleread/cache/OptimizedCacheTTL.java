package kuke.board.articleread.cache;

import lombok.Getter;

import java.time.Duration;

@Getter
public class OptimizedCacheTTL {
    private Duration logicalTTL;
    private Duration physicalTTL;

    public static final long PHYSICAL_TTL_DELAY_SECONDS = 5L;

    public static OptimizedCacheTTL of(long logicalTTL) {
        OptimizedCacheTTL optimizedCacheTTL = new OptimizedCacheTTL();
        optimizedCacheTTL.logicalTTL = Duration.ofSeconds(logicalTTL);
        optimizedCacheTTL.physicalTTL = optimizedCacheTTL.logicalTTL.ofSeconds(logicalTTL).plusSeconds(PHYSICAL_TTL_DELAY_SECONDS);
        return optimizedCacheTTL;
    }
}
