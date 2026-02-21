package kuke.board.article.api;

import kuke.board.article.entity.Article;
import kuke.board.article.service.response.ArticlePageResponse;
import kuke.board.article.service.response.ArticleResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class  ArticleApiTest {
    RestClient restClient = RestClient.create("http://localhost:9000");

    @Test
    void createTest() {
        ArticleResponse response = create(new ArticleCreateRequest(
                "hi", "my name is janghan1", 1L, 1L
        ));
        System.out.println("response = " + response);
    }

    @Test
    void readTest() {
        ArticleResponse response = read(270809074745810944L);
        System.out.println("response = " + response);
    }

    @Test
    void updateTest() {
        ArticleResponse response = update(270809074745810944L);
        System.out.println("response = " + response);
    }

    @Test
    void deleteTest() {
        delete(270809074745810944L);
    }

    @Test
    void readAllTest() {
        ArticlePageResponse response = restClient.get().uri("/v1/articles?boardId=1&page=50&pageSize=50000")
                .retrieve().body(ArticlePageResponse.class);

        System.out.println("response.getArticleCount() = " + response.getArticleCount());
        for (ArticleResponse article : response.getArticles()) {
            System.out.println("article.getArticleId() = " + article.getArticleId());
        }
    }

    @Test
    void readAllInfiniteScrollTest() {
        List<ArticleResponse> responses = restClient.get().uri("/v1/articles/infinite-scroll?boardId=1&pageSize=50&lastArticleId=270874586461192600").
                retrieve().body(new ParameterizedTypeReference<List<ArticleResponse>>() {});

        System.out.println("responses.size() = " + responses.size());

        for (ArticleResponse article : responses) {
            System.out.println("article.getArticleId() = " + article.getArticleId());
        }
    }

    @Test
    void countTest() {
        ArticleResponse response = create(new ArticleCreateRequest("hi", "content1", 1L, 2L));

        Long count1 = restClient.get()
                .uri("/v1/articles/{boardId}/count", 2L)
                .retrieve()
                .body(Long.class);

        System.out.println("count1 = " + count1);

        restClient.delete()
                .uri("/v1/articles/{articleId}", response.getArticleId())
                .retrieve();

        Long count2 = restClient.get()
                .uri("/v1/articles/{boardId}/count", 2L)
                .retrieve()
                .body(Long.class);

        System.out.println("count2 = " + count2);
    }

    ArticleResponse create(ArticleCreateRequest request) {
        return restClient.post()
                .uri("/v1/articles")
                .body(request)
                .retrieve()
                .body(ArticleResponse.class);
    }

    ArticleResponse read(Long articleId) {
        return restClient.get()
                .uri("/v1/articles/{articleId}", articleId)
                .retrieve()
                .body(ArticleResponse.class);
    }

    ArticleResponse update(Long articleId) {
        return restClient.put()
                .uri("/v1/articles/{articleId}", articleId)
                .body(new ArticleUpdateRequest("hi 2", "my name is janghan2"))
                .retrieve()
                .body(ArticleResponse.class);
    }

    void delete(Long articleId) {
        restClient.delete()
                .uri("/v1/articles/{articleId}", articleId)
                .retrieve();
    }

    @Getter
    @AllArgsConstructor
    static class ArticleCreateRequest {
        private String title;
        private String content;
        private Long writerId;
        private Long boardId;
    }

    @Getter
    @AllArgsConstructor
    static class ArticleUpdateRequest {
        private String title;
        private String content;
    }
}
