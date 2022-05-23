package com.example.articlemanagement.service;

import com.example.articlemanagement.dto.ArticleDto;
import com.example.articlemanagement.dto.ArticleSummaryResponse;
import com.example.articlemanagement.dto.CreateArticleRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

public interface ArticleService {
    List<ArticleSummaryResponse> getArticleSummaries();
    ArticleDto createArticle(@Valid CreateArticleRequest createArticleRequest);
    ArticleDto addImageToArticle(Long id, MultipartFile multipartFile) throws IOException;
}