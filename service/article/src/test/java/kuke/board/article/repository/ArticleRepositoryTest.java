package kuke.board.article.repository;

import kuke.board.article.entity.Article;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class ArticleRepositoryTest {
    @Autowired
    ArticleRepository articleRepository;

    @Test
    void findAllTest() {
        List<Article> articles = articleRepository.findAll(1L, 1499970L, 30L);
        log.info("articles.siarticlesze() = {}", articles.size());
        for (Article article : articles) {
            log.info(article.toString());
        }
    }

    @Test
    void countTest() {
        Long count = articleRepository.count(1L, 10000L);
        System.out.println("count = " + count);
    }

    @Test
    void findAllInfiniteScrollTest() {
        List<Article> articles = articleRepository.findAllInfiniteScroll(1L, 30L);

        for (Article article : articles) {
            log.info("articles.getArticleId() = " + article.getArticleId());
        }

        final Long lastArticleId = articles.getLast().getArticleId();
        System.out.println("lastArticleId = " + lastArticleId);
        List<Article> articles_2 = articleRepository.findAllInfiniteScroll(lastArticleId, 30L, lastArticleId);

        for (Article article : articles_2) {
            log.info("articles.getArticleId() = " + article.getArticleId());
        }
    }
}