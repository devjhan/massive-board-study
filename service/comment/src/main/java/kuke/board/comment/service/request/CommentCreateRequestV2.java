package kuke.board.comment.service.request;

import lombok.Getter;

@Getter
public class CommentCreateRequestV2 {
    private String content;
    private Long articleId;
    private String parentPath;
    private Long writerId;
}
