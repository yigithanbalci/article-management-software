package com.example.articlemanagement.service.impl;

import com.example.articlemanagement.dto.ArticleDto;
import com.example.articlemanagement.dto.ArticleSummaryResponse;
import com.example.articlemanagement.dto.CreateArticleRequest;
import com.example.articlemanagement.exception.ArticleNotFoundException;
import com.example.articlemanagement.exception.BadRequestException;
import com.example.articlemanagement.mapper.ArticleMapper;
import com.example.articlemanagement.model.Article;
import com.example.articlemanagement.model.Image;
import com.example.articlemanagement.repository.ArticleRepository;
import com.example.articlemanagement.repository.ImageRepository;
import com.example.articlemanagement.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ImageRepository imageRepository;

    @Override
    public List<ArticleSummaryResponse> getArticleSummaries() {
        List<ArticleSummaryResponse> articleSummaryResponses = new ArrayList<>();
        imageRepository.findAll().forEach(image -> {
            ArticleSummaryResponse articleSummaryResponse = new ArticleSummaryResponse();
            Optional<Article> articleOptional = articleRepository.findById(image.getArticleId());
            articleOptional.ifPresent(article -> {
                articleSummaryResponse.setTitle(article.getTitle());
                articleSummaryResponse.setNumberOfImages(article.getImages().size());
                articleSummaryResponses.add(articleSummaryResponse);
            });
        });
        return articleSummaryResponses;
    }

    @Override
    public ArticleDto createArticle(CreateArticleRequest createArticleRequest) {
        Article article = new Article();
        article.setTitle(createArticleRequest.getTitle());
        article.setDescription(createArticleRequest.getDescription());
        return ArticleMapper.INSTANCE.articleToArticleDto(articleRepository.save(article));
    }

    @Override
    public ArticleDto addImageToArticle(Long id, MultipartFile multipartFile) throws IOException {
        Article article = articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException("Article not found with id: " + id));
        if (article.getImages() == null){
            article.setImages(new ArrayList<>());
        }
        if (article.getImages().size() < 3){
            Image image = new Image();
            image.setType(multipartFile.getContentType());
            image.setImage(multipartFile.getBytes());
            image.setArticleId(article.getId());
            image = imageRepository.save(image);
            article.getImages().add(image);
            return ArticleMapper.INSTANCE.articleToArticleDto(articleRepository.save(article));
        }
        throw new BadRequestException("Articles can have less than 3 images, current images of the article: " + article.getImages().size());
    }
}
