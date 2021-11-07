package com.romanm.jwtservicedata.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.romanm.jwtservicedata.constants.CommonConstants;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Document(collection = CommonConstants.COMMENT_COLLECTION)
public class Comment implements Serializable {
    @Id
    @NotNull
    private String id;
    @NotNull
    private String userId;
    @NotNull
    private String toUserId;
    @NotBlank
    @Size(max = 255)
    private String comment;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public Comment(String userId, String toUserId, String commment) {
        this.userId = userId;
        this.comment = commment;
        this.toUserId = toUserId;

        this.timestamp = LocalDateTime.now();
    }
}
