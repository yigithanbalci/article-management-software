package com.example.articlemanagement.web;

import com.example.articlemanagement.model.Article;
import com.example.articlemanagement.model.Image;
import com.example.articlemanagement.repository.ArticleRepository;
import com.example.articlemanagement.repository.ImageRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// servlet.multipart.max-file-size config is not testable with MockMvc.
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class ArticleControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ImageRepository imageRepository;

    @Test
    void createArticle() throws Exception {
        mvc.perform((get("/articles")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        mvc.perform(
                        post("/articles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"title\":\"new post\",\"description\":\"new content description\"}")
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(jsonPath("$.title", equalTo("new post")))
                .andExpect(jsonPath("$.description", equalTo("new content description")));

        mvc.perform((get("/articles")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        assertEquals(4, articleRepository.count());
    }

    @Test
    void addImageToArticle() throws Exception {
        mvc.perform((get("/articles")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        mvc.perform(
                        post("/articles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"title\":\"new post\",\"description\":\"new content description\"}")
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(jsonPath("$.title", equalTo("new post")))
                .andExpect(jsonPath("$.description", equalTo("new content description")));

        mvc.perform((get("/articles")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        AtomicReference<Long> id = new AtomicReference<>();

        articleRepository.findAll().forEach(article -> {
            if (article.getImages() == null){
                id.set(article.getId());
            }
        });

        MockMultipartFile file = new MockMultipartFile("image", "filename.jpeg", "text/plain", "some image".getBytes());

        mvc.perform(
                        multipart("/articles/" + id.get())
                                .file(file)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(jsonPath("$.title", equalTo("new post")))
                .andExpect(jsonPath("$.description", equalTo("new content description")));

        mvc.perform((get("/articles")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    void listArticleTitlesWithImages() throws Exception {
        mvc.perform((get("/articles")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].title", equalTo("title1")))
                .andExpect(jsonPath("$[0].numberOfImages", equalTo(1)));

        AtomicReference<Long> id = new AtomicReference<>();

        articleRepository.findAll().forEach(article -> {
            if ("title1".equals(article.getTitle())){
                id.set(article.getId());
            }
        });

        MockMultipartFile file = new MockMultipartFile("image", "filename.jpeg", "text/plain", "some image".getBytes());

        mvc.perform(
                        multipart("/articles/" + id.get())
                                .file(file)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(jsonPath("$.title", equalTo("title1")))
                .andExpect(jsonPath("$.description", equalTo("description1")));

        mvc.perform((get("/articles")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].title", equalTo("title1")))
                .andExpect(jsonPath("$[0].numberOfImages", equalTo(2)));
    }

    @Test
    void descriptionTooLong() throws Exception {
        mvc.perform(
                        post("/articles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"title\":\"title\",\"description\":\" " + new String(new char[(4 * 1024) + 1]).replace('\0', 'A') + " \"}")
                )
                .andExpect(status().isBadRequest());
    }

    @BeforeAll
    void init() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();

        Article article1 = new Article();
        article1.setTitle("title1");
        article1.setDescription("description1");
        article1.setImages(new ArrayList<>());
        article1 = articleRepository.save(article1);

        Image image1 = new Image();
        image1.setArticleId(article1.getId());
        image1.setType("jpeg");
        image1.setImage(new byte[2]);
        image1 = imageRepository.save(image1);

        article1.getImages().add(image1);
        articleRepository.save(article1);


        Article article2 = new Article();
        article2.setTitle("title2");
        article2.setDescription("description2");
        article2.setImages(new ArrayList<>());
        article2 = articleRepository.save(article2);

        Image image2 = new Image();
        image2.setArticleId(article2.getId());
        image2.setType("jpeg");
        image2.setImage(new byte[3]);
        image2 = imageRepository.save(image2);

        article2.getImages().add(image2);
        articleRepository.save(article2);

        Article article3 = new Article();
        article3.setTitle("title3");
        article3.setDescription("description3");
        article3.setImages(new ArrayList<>());
        article3 = articleRepository.save(article3);

        Image image3 = new Image();
        image3.setArticleId(article3.getId());
        image3.setType("jpeg");
        image3.setImage(new byte[4]);
        image3 = imageRepository.save(image3);

        article3.getImages().add(image3);
        articleRepository.save(article3);
    }
}