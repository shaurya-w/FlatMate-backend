package com.example.mpr_backend_jan.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class NoticeRequest {
    private String title;
    private String content;
    private Long societyId;
    private int durationInDays;
}