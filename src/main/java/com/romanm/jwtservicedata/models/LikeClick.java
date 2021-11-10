package com.romanm.jwtservicedata.models;

import com.romanm.jwtservicedata.constants.CommonConstants;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Document(collection = CommonConstants.LIKES_COLLECTION)
public class LikeClick implements Serializable {
    @Id
    @NotNull
    private String id;
    @Indexed(name = "to_user_id")
    private String toUserId;

    private String fromUserId;

    public LikeClick(String toUserId, String fromUserId) {
        this.toUserId = toUserId;
        this.fromUserId = fromUserId;
    }
}
