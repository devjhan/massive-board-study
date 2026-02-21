package kuke.board.articleread.cache;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kuke.board.common.event.DataSerializer;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@ToString
public class OptimizedCache {
    private String data;
    private LocalDateTime expireAt;

    public static OptimizedCache of(Object data, Duration ttl) {
        OptimizedCache cache = new OptimizedCache();
        cache.data = DataSerializer.serialize(data);
        cache.expireAt = LocalDateTime.now().plus(ttl);
        return cache;
    }

    @JsonIgnore
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireAt);
    }

    public <T> T parseData(Class<T> dataType) {
        return DataSerializer.deserialize(data, dataType);
    }
}
