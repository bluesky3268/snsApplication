package com.hyunbenny.snsApplication.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCreateRequest {
    private String title;
    private String content;

}