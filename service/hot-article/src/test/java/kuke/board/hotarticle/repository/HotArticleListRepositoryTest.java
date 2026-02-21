package kuke.board.hotarticle.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HotArticleListRepositoryTest {
    @Autowired
    HotArticleListRepository hotArticleListRepository;

    @Test
    void addTest() throws InterruptedException {
        LocalDateTime time = LocalDateTime.now();
        long limit = 3;
        hotArticleListRepository.add(1L, time, 2L, limit, Duration.ofSeconds(5));
        hotArticleListRepository.add(2L, time, 22L, limit, Duration.ofSeconds(5));
        hotArticleListRepository.add(3L, time, 222L, limit, Duration.ofSeconds(5));
        hotArticleListRepository.add(4L, time, 23L, limit, Duration.ofSeconds(5));
        hotArticleListRepository.add(5L, time, 5L, limit, Duration.ofSeconds(5));

        List<Long> articleIds = hotArticleListRepository.readAll("20260205");

        assertThat(articleIds).hasSize(Long.valueOf(limit).intValue());
        assertThat(articleIds.getFirst()).isEqualTo(3L);
        assertThat(articleIds.get(1)).isEqualTo(4L);
        assertThat(articleIds.get(2)).isEqualTo(2L);

        TimeUnit.SECONDS.sleep(6);

        assertThat(hotArticleListRepository.readAll("20260205")).isEmpty();
    }
}