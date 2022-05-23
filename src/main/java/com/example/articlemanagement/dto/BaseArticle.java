package com.example.articlemanagement.dto;

import com.example.articlemanagement.validate.DescriptionContraint;
import lombok.Data;

@Data
public abstract class BaseArticle {
    private String title;

    @DescriptionContraint
    private String description;
}