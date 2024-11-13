package com.xc.blogbackend.model.domain.reqDto;

import com.xc.blogbackend.model.domain.entity.BlogPhoto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 删除相册图片的请求体
 *
 * @author 星尘
 */
@Data
public class DelPhotoRequest implements Serializable {
    private static final long serialVersionUID = 4141228574115437093L;

    private Integer type;
    private List<BlogPhoto> imgList;
}
