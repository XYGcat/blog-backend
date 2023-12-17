package com.xc.blogbackend.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 找出两个图片链接数组之间不同的图片链接元素
 *
 * @author 星尘
 */
public class ImageLinkComparator {

    // oldMdImgList：数据库中的数据；  newMdImgList：文章更新编辑后传入的数据
    // 寻找oldMdImgList中有，newMdImgList中没有的元素
    public static List<String> findMissingElements(List<String> newMdImgList, List<String> oldMdImgList) {
        List<String> missingInOldList = new ArrayList<>(oldMdImgList);
        missingInOldList.removeAll(newMdImgList);
        return missingInOldList;
    }

    // 寻找newMdImgList中有，oldMdImgList中没有的元素
    public static List<String> findMissingElementsReverse(List<String> newMdImgList, List<String> oldMdImgList) {
        List<String> missingInNewList = new ArrayList<>(newMdImgList);
        missingInNewList.removeAll(oldMdImgList);
        return missingInNewList;
    }

    // newMdImgList中和oldMdImgList中共有的元素
    public static List<String> findCommonElements(List<String> newMdImgList, List<String> oldMdImgList) {
        List<String> commonElements = new ArrayList<>();

        for (String element : newMdImgList) {
            if (oldMdImgList.contains(element)) {
                commonElements.add(element);
            }
        }

        return commonElements;
    }
}
