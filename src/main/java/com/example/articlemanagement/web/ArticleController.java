package com.example.articlemanagement.web;

import com.example.articlemanagement.dto.ArticleDto;
import com.example.articlemanagement.dto.ArticleSummaryResponse;
import com.example.articlemanagement.dto.CreateArticleRequest;
import com.example.articlemanagement.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/articles", produces = MediaType.APPLICATION_JSON_VALUE)
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping
    List<ArticleSummaryResponse> getArticles() {
        return articleService.getArticleSummaries();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ArticleDto createArticle(@Valid @RequestBody CreateArticleRequest createArticleRequest) {
        return articleService.createArticle(createArticleRequest);
    }

    @PostMapping("{id}")
    ArticleDto addImageToArticle(@PathVariable Long id, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        return articleService.addImageToArticle(id, multipartFile);
    }
}