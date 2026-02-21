package kuke.board.articleread.client.api;

import kuke.board.articleread.service.response.ArticleReadPageResponse;
import kuke.board.articleread.service.response.ArticleReadResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class ArticleReadApiTest {
    RestClient articleReadRestClient = RestClient.create("http://localhost:9001");
    RestClient articleRestClient = RestClient.create("http://localhost:9000");

    @Test
    void readTest() {
        ArticleReadResponse response = articleReadRestClient.get()
                .uri("v1/articles/{articleId}", 270870567844003845L)
                .retrieve()
                .body(ArticleReadResponse.class);

        System.out.println("response: " + response);
    }

    @Test
    void readAllTest() {
        ArticleReadPageResponse response1 = articleReadRestClient.get()
                .uri("/v1/articles?boardId=%s&page=%s&pageSize=%s".formatted(1L, 1L, 5))
                .retrieve()
                .body(ArticleReadPageResponse.class);

        System.out.println("response1.getArticleCount(): " + response1.getArticleCount());

        for (ArticleReadResponse response : response1.getArticles()) {
            System.out.println("response.getArticleId(): " + response.getArticleId());
        }

        ArticleReadPageResponse response2 = articleRestClient.get()
                .uri("/v1/articles?boardId=%s&page=%s&pageSize=%s".formatted(1L, 1L, 5))
                .retrieve()
                .body(ArticleReadPageResponse.class);

        System.out.println("response2.getArticleCount(): " + response2.getArticleCount());

        for (ArticleReadResponse response : response2.getArticles()) {
            System.out.println("response.getArticleId(): " + response.getArticleId());
        }
    }

    @Test
    void readAllInfiniteScrollTest() {
        List<ArticleReadResponse> responses1 = articleReadRestClient.get()
                .uri("/v1/articles/infinite-scroll?boardId=%s&lastArticleId=%s&pageSize=%s".formatted(1L, 282819495239135232L, 5L))
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleReadResponse>>() {
                });

        System.out.println("responses1.size() = " + responses1.size());

        for (ArticleReadResponse response : responses1) {
            System.out.println("response.getArticleId(): " + response.getArticleId());
        }

        List<ArticleReadResponse> responses2 = articleRestClient.get()
                .uri("/v1/articles/infinite-scroll?boardId=%s&lastArticleId=%s&pageSize=%s".formatted(1L, 282819495239135232L, 5L))
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleReadResponse>>() {
                });

        System.out.println("responses2.size() = " + responses2.size());

        for (ArticleReadResponse response : responses2) {
            System.out.println("response.getArticleId(): " + response.getArticleId());
        }
    }
}
