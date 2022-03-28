package com.project.minibacktesting_be.dto.likes;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class LikesRequestDto {

    private Long portId;
    private boolean likes;
}
