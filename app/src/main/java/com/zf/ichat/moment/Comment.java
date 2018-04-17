package com.zf.ichat.moment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Comment {
    private String from = null;
    private String to = null;
    private String content = null;
}
