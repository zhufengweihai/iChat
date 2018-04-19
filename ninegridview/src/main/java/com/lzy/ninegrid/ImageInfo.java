package com.lzy.ninegrid;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * ================================================
 * 作    者：廖子尧
 * 版    本：1.0
 * 创建日期：2016/3/21
 * 描    述：
 * 修订历史：
 * ================================================
 */
@Getter
@Setter
public class ImageInfo implements Serializable {
    private String thumbnailUrl;
    private String bigImageUrl;
    private int imageViewHeight;
    private int imageViewWidth;
    private int imageViewX;
    private int imageViewY;
}
