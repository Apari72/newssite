package com.newssite.service;

import com.newssite.model.Article;
import com.newssite.model.Comment;
import com.newssite.repository.ArticleRepository;
import com.newssite.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepo;
    private final ArticleRepository articleRepo;

    public CommentService(CommentRepository commentRepo, ArticleRepository articleRepo) {
        this.commentRepo = commentRepo;
        this.articleRepo = articleRepo;
    }

    public Comment addComment(Long articleId, String authorName, String content) {
        Article article = articleRepo.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        Comment comment = new Comment();
        comment.setAuthorName(authorName);
        comment.setContent(content);
        comment.setArticle(article);

        return commentRepo.save(comment);
    }

    public List<Comment> getComments(Long articleId) {
        Article article = articleRepo.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        return commentRepo.findByArticleOrderByCreatedAtDesc(article);
    }
}
