package com.newssite.service;

import com.newssite.dto.CommentDto;
import com.newssite.model.*;
import com.newssite.repository.ArticleRepository;
import com.newssite.repository.CommentLikeRepository;
import com.newssite.repository.CommentRepository;
import com.newssite.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepo;
    private final CommentLikeRepository likeRepo;
    private final ArticleRepository articleRepo;
    private final UserRepository userRepo;

    public CommentService(
            CommentRepository commentRepo,
            CommentLikeRepository likeRepo,
            ArticleRepository articleRepo,
            UserRepository userRepo
    ) {
        this.commentRepo = commentRepo;
        this.likeRepo = likeRepo;
        this.articleRepo = articleRepo;
        this.userRepo = userRepo;
    }

    // ---------------- ADD ----------------
    public CommentDto addComment(Long articleId, String email, String content) {

        User user = userRepo.findByEmail(email).orElseThrow();
        Article article = articleRepo.findById(articleId).orElseThrow();

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setArticle(article);
        comment.setContent(content);

        Comment saved = commentRepo.save(comment);

        return toDto(saved, user);
    }

    // ---------------- LIST ----------------
    public List<CommentDto> getComments(Long articleId, String email) {

        Article article = articleRepo.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        User currentUser = email == null
                ? null
                : userRepo.findByEmail(email).orElse(null);

        return commentRepo.findByArticleOrderByCreatedAtDesc(article)
                .stream()
                .map(c -> {

                    boolean isOwner = currentUser != null &&
                            c.getUser().getId().equals(currentUser.getId());

                    boolean isAdmin = currentUser != null &&
                            currentUser.getRole() == Role.ADMIN;

                    int likeCount = likeRepo.countByComment(c);

                    boolean liked = currentUser != null &&
                            likeRepo.findByCommentAndUser(c, currentUser).isPresent();

                    return new CommentDto(
                            c.getId(),
                            c.getContent(),
                            c.getUser().getName(),
                            c.getCreatedAt(),
                            isOwner,               // canEdit
                            isOwner || isAdmin,    // canDelete
                            likeCount,
                            liked
                    );


                })
                .toList();
    }


    // ---------------- DELETE (OWNER OR ADMIN) ----------------
    @Transactional
    public void deleteComment(Long commentId, String email) {

        User user = userRepo.findByEmail(email).orElseThrow();
        Comment comment = commentRepo.findById(commentId).orElseThrow();

        boolean isOwner = comment.getUser().getId().equals(user.getId());
        boolean isAdmin = user.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("Forbidden");
        }

        commentRepo.delete(comment);
    }

    // ---------------- EDIT (OWNER ONLY) ----------------
    @Transactional
    public CommentDto editComment(Long commentId, String email, String newContent) {

        User user = userRepo.findByEmail(email).orElseThrow();
        Comment comment = commentRepo.findById(commentId).orElseThrow();

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Forbidden");
        }

        comment.setContent(newContent);
        return toDto(comment, user);
    }

    // ---------------- LIKE / UNLIKE ----------------
    @Transactional
    public CommentDto toggleLike(Long commentId, String email) {

        User user = userRepo.findByEmail(email).orElseThrow();
        Comment comment = commentRepo.findById(commentId).orElseThrow();

        likeRepo.findByCommentAndUser(comment, user)
                .ifPresentOrElse(
                        likeRepo::delete,
                        () -> {
                            CommentLike like = new CommentLike();
                            like.setComment(comment);
                            like.setUser(user);
                            likeRepo.save(like);
                        }
                );

        return toDto(comment, user);
    }

    // ---------------- DTO MAPPER ----------------
    private CommentDto toDto(Comment c, User currentUser) {

        boolean isOwner = currentUser != null &&
                c.getUser().getId().equals(currentUser.getId());

        boolean isAdmin = currentUser != null &&
                currentUser.getRole() == Role.ADMIN;

        boolean likedByCurrentUser = currentUser != null &&
                likeRepo.findByCommentAndUser(c, currentUser).isPresent();

        int likeCount = likeRepo.countByComment(c);

        return new CommentDto(
                c.getId(),
                c.getContent(),
                c.getUser().getName(),
                c.getCreatedAt(),
                isOwner,                // canEdit
                isOwner || isAdmin,     // canDelete
                likeCount,
                likedByCurrentUser      // likedByCurrentUser
        );
    }

}


