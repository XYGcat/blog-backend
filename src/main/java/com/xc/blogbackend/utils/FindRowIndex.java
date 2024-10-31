package com.xc.blogbackend.utils;

import com.xc.blogbackend.model.domain.BlogTalk;

import java.util.List;
import java.util.Objects;

/**
 * 根据传入条件寻找索引
 *
 * @author 星尘
 */
public class FindRowIndex {
    public static Integer findRowIndex(List<BlogTalk> rows,Long talkId){
        for (int i = 0; i < rows.size(); i++) {
            if (Objects.equals(rows.get(i).getId(), talkId)) {
                return i;
            }
        }
        return -1;
    }
}
