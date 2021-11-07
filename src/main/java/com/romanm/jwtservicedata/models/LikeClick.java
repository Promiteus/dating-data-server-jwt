package com.romanm.jwtservicedata.models;

import com.romanm.jwtservicedata.constants.CommonConstants;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Document(collection = CommonConstants.LIKE_COLLECTION)
public class LikeClick implements Serializable {
    @Id
    @NotNull
    private String id;
    @NotNull
    @NotBlank
    private String toUserId;
    @NotNull
    @NotBlank
    private String fromUserId;

    public LikeClick(String toUserId, String fromUserId) {
        this.toUserId = toUserId;
        this.fromUserId = fromUserId;
    }
}
