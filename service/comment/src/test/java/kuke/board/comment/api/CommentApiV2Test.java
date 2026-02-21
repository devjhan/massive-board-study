package kuke.board.comment.api;

import kuke.board.comment.service.response.CommentPageResponse;
import kuke.board.comment.service.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class CommentApiV2Test {
    RestClient restClient = RestClient.create("http://localhost:9002");

    @Test
    void create() {
        CommentResponse response1 = create(new CommentCreateRequestV2("content1", 1L, null, 1L));
        CommentResponse response2 = create(new CommentCreateRequestV2("content2", 1L, response1.getPath(), 1L));
        CommentResponse response3 = create(new CommentCreateRequestV2("content3", 1L, response2.getPath(), 1L));

        System.out.println("response1.getPath() = " + response1.getPath());
        System.out.println("response1.getCommentId() = " + response1.getCommentId());
        System.out.println("\tresponse2.getPath() = " + response2.getPath());
        System.out.println("\t\tresponse3.getPath() = " + response3.getPath());

        /*
        response1.getPath() = 00009
            response2.getPath() = 0000900000
		        response3.getPath() = 000090000000000
         */
    }

    @Test
    void read() {
        CommentResponse response = restClient.get()
                .uri("/v2/comments/{commentsId}", 275459143570849792L)
                .retrieve()
                .body(CommentResponse.class);

        System.out.println("response = " + response);
    }

    @Test
    void delete() {
        restClient.delete()
                .uri("/v2/comments/{commentId}", 275459143570849792L)
                .retrieve();
    }

    @Test
    void readAll() {
        CommentPageResponse response = restClient.get()
                .uri("/v2/comments?articleId=1&page=1&pageSize=50")
                .retrieve()
                .body(CommentPageResponse.class);

        System.out.println("response.getCommentCount() = " + response.getCommentCount());
        for (CommentResponse comment : response.getComments()) {
            System.out.println("commentgetCommentId() = " + comment.getCommentId());
        }
        /*
        response.getCommentCount() = 501
commentgetCommentId() = 275461767328292865
commentgetCommentId() = 275461767361847330
commentgetCommentId() = 275461767366041600
commentgetCommentId() = 275461767366041609
commentgetCommentId() = 275461767366041630
commentgetCommentId() = 275461767366041641
commentgetCommentId() = 275461767370235904
commentgetCommentId() = 275461767370235916
commentgetCommentId() = 275461767370235925
commentgetCommentId() = 275461767370235933
commentgetCommentId() = 275461767370235947
commentgetCommentId() = 275461767374430216
commentgetCommentId() = 275461767374430228
commentgetCommentId() = 275461767374430241
commentgetCommentId() = 275461767374430256
commentgetCommentId() = 275461767378624513
commentgetCommentId() = 275461767378624528
commentgetCommentId() = 275461767378624543
commentgetCommentId() = 275461767378624554
commentgetCommentId() = 275461767378624565
commentgetCommentId() = 275461767382818818
commentgetCommentId() = 275461767382818830
commentgetCommentId() = 275461767382818840
commentgetCommentId() = 275461767382818854
commentgetCommentId() = 275461767382818866
commentgetCommentId() = 275461767387013123
commentgetCommentId() = 275461767387013130
commentgetCommentId() = 275461767387013138
commentgetCommentId() = 275461767387013148
commentgetCommentId() = 275461767387013156
commentgetCommentId() = 275461767391207424
commentgetCommentId() = 275461767391207430
commentgetCommentId() = 275461767391207440
commentgetCommentId() = 275461767391207447
commentgetCommentId() = 275461767391207455
commentgetCommentId() = 275461767391207463
commentgetCommentId() = 275461767395401734
commentgetCommentId() = 275461767395401742
commentgetCommentId() = 275461767395401750
commentgetCommentId() = 275461767395401758
commentgetCommentId() = 275461767395401766
commentgetCommentId() = 275461767399596035
commentgetCommentId() = 275461767399596037
commentgetCommentId() = 275461767399596040
commentgetCommentId() = 275461767399596043
commentgetCommentId() = 275461767407984644
commentgetCommentId() = 275461767420567588
commentgetCommentId() = 275461767420567591
commentgetCommentId() = 275461767420567596
commentgetCommentId() = 275461767420567602
         */
    }

    @Test
    void readAllInfiniteScroll() {
        List<CommentResponse> responses1 = restClient.get()
                .uri("/v2/comments/infinite-scroll?articleId=1&pageSize=5")
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {
                });

        System.out.println("first page");
        for (CommentResponse response : responses1) {
            System.out.println("response.getCommentId() = " + response.getCommentId());
        }

        String lastPath = responses1.getLast().getPath();

        List<CommentResponse> responses2 = restClient.get()
                .uri("/v2/comments/infinite-scroll?articleId=1&pageSize=10&lastPath={lastPath}", lastPath)
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {
                });

        System.out.println("second page");
        for (CommentResponse response : responses2) {
            System.out.println("response.getCommentId() = " + response.getCommentId());
        }
    }

    @Test
    void countTest() {
        CommentResponse response = create(new CommentCreateRequestV2("content", 1L, null, 1L));
        Long count1 = restClient.get()
                .uri("v2/comments/articles/{articleId}/count", 1L)
                .retrieve()
                .body(Long.class);

        System.out.println("count1 = " + count1);

        restClient.delete()
                .uri("/v2/comments/{commentId}", response.getCommentId())
                .retrieve();

        Long count2 = restClient.get()
                .uri("v2/comments/articles/{articleId}/count", 1L)
                .retrieve()
                .body(Long.class);

        System.out.println("count2 = " + count2);
    }

    CommentResponse create(CommentCreateRequestV2 request) {
        return restClient.post()
                .uri("/v2/comments")
                .body(request)
                .retrieve()
                .body(CommentResponse.class);
    }

    @Getter
    @AllArgsConstructor
    public static class CommentCreateRequestV2 {
        private String content;
        private Long articleId;
        private String parentPath;
        private Long writerId;
    }


}
