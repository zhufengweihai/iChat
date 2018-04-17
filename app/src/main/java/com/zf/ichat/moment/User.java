package com.zf.ichat.moment;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String name = null;
    private String avatar = null;
    private String background = null;
    private List<Moment> moments = null;
}
