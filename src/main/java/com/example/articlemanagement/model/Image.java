package com.example.articlemanagement.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "image")
@Table(name = "image")
public class Image {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "type")
    private String type;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "article_id")
    private Long articleId;
}