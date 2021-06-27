package com.example.business.usecase;

import com.example.domain.group.model.GroupMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

public class ChangeGroupOwnerCase {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotNull(message = "group_member_id")
        private String memberId;
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        private String groupId;
        private String userId;
        private GroupMember.GroupMemberRole role;

        public static Response from(GroupMember groupMember) {
            return Response.builder()
                    .groupId(groupMember.getGroupId())
                    .userId(groupMember.getUserId())
                    .role(groupMember.getRole())
                    .build();
        }
    }
}