package com.project.minibacktesting_be.dto.community;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityPortResponseDto {

    CommunityPortDto communityPort;
    Long likesCnt;
    Long commentCnt;
    List<String> likesUsers;

}
