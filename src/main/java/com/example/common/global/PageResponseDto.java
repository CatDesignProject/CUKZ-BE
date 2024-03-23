package com.example.common.global;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponseDto<T> {
    private int listSize;
    private int totalPage;
    private long totalElements;
    private boolean isFirst;
    private boolean isLast;
    private List<T> content;

    public static <T> PageResponseDto<T> toResponseDto(Page<T> page) {

        return PageResponseDto.<T>builder()
                .listSize(page.getContent().size())
                .totalPage(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .content(page.getContent())
                .build();
    }
}
