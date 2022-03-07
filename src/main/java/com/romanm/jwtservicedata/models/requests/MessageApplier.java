package com.romanm.jwtservicedata.models.requests;

import com.romanm.jwtservicedata.models.ChatItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageApplier implements Serializable {
    private List<String> readMessagesIds;
    private List<String> writeMessagesIds;
}
