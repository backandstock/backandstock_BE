package com.project.minibacktesting_be.dto;

import lombok.Data;

@Data
public class MsgResponseDto {
    private String msg;

    public MsgResponseDto(String msg){
        this.msg = msg;
    }
}
