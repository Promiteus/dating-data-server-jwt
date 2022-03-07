package com.romanm.jwtservicedata.models.responses;

import com.romanm.jwtservicedata.models.ChatItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageApplierResponse implements Serializable {
    private List<ChatItem> readMessages = new ArrayList<>();
    private List<ChatItem> writeMessages = new ArrayList<>();
}
