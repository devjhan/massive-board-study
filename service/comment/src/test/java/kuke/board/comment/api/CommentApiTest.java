package kuke.board.comment.api;

import kuke.board.comment.service.response.CommentPageResponse;
import kuke.board.comment.service.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class CommentApiTest {
    RestClient restClient = RestClient.create("http://localhost:9002");

    @Test
    void create() {
        CommentResponse response1 = createComment(new CommentCreateRequest("1", 1L, null, 4L));
        CommentResponse response2 = createComment(new CommentCreateRequest("2", 1L, response1.getCommentId(), 4L));
        CommentResponse response3 = createComment(new CommentCreateRequest("3", 1L, response1.getCommentId(), 4L));

        System.out.println("response1.getCommentId() = " + response1.getCommentId());
        System.out.println("\tresponse2.getCommentId() = " + response2.getCommentId());
        System.out.println("\tresponse3.getCommentId() = " + response3.getCommentId());

        // response1.getCommentId() = 273328851800416256
        //	 response2.getCommentId() = 273328852404396032 x
        //	 response3.getCommentId() = 273328852454727680 x
    }

    @Test
    void read() {
        CommentResponse response = restClient.get()
                .uri("v1/comments/{comments_id}",273328851800416256L)
                .retrieve()
                .body(CommentResponse.class);

        System.out.println("response = " + response);
    }

    @Test
    void delete() {
        CommentResponse response = restClient.delete()
                .uri("v1/comments/{comments_id}", 273328851800416256L)
                .retrieve()
                .body(CommentResponse.class);

        System.out.println("response = " + response);
    }

    @Test
    void readAll() {
        CommentPageResponse response = restClient.get()
                .uri("v1/comments?articleId=1&page=1&pageSize=50")
                .retrieve()
                .body(CommentPageResponse.class);

        System.out.println("response.getCommentCount() = " + response.getCommentCount());

        for (CommentResponse comment : response.getComments()) {
            if (comment.getCommentId().equals(comment.getParentCommentId())) {
                System.out.print("\t");
            } else {
                System.out.println("comment.getCommentId() = " + comment.getCommentId());
            }
        }
    }

    @Test
    void readAllInfiniteScroll() {
        List<CommentResponse> responses1 = restClient.get()
                .uri("v1/comments/infinite-scroll?articleId=1&pageSize=10")
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {
                });

        System.out.println("first page");
        for (CommentResponse response: responses1) {
            if (response.getCommentId().equals(response.getParentCommentId())) {
                System.out.print("\t");
            }
            System.out.println("response.getCommentId = " + response.getCommentId());
        }

        Long lastParentCommentId = responses1.getLast().getParentCommentId();
        Long lastCommentId = responses1.getLast().getCommentId();

        List<CommentResponse> responses2 = restClient.get()
                .uri("v1/comments/infinite-scroll?articleId=1&pageSize=10&lastCommentId={lastCommentId}&lastParentCommentId={lastParentCommentId}", lastCommentId, lastParentCommentId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {
                });

        System.out.println("second page");
        for (CommentResponse response: responses2) {
            if (response.getCommentId().equals(response.getParentCommentId())) {
                System.out.print("\t");
            }
            System.out.println("response.getCommentId = " + response.getCommentId());
        }
    }

    CommentResponse createComment(CommentCreateRequest request) {
        return restClient.post()
                .uri("/v1/comments")
                .body(request)
                .retrieve()
                .body(CommentResponse.class);
    }


    @Getter
    @AllArgsConstructor
    static class CommentCreateRequest {
        private String content;
        private Long articleId;
        private Long parentCommentId;
        private Long writerId;
    }
}
