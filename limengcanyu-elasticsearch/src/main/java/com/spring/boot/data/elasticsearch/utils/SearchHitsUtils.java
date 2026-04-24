package com.spring.boot.data.elasticsearch.utils;

import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author rock.jiang
 * Date 2020/05/17 22:27
 */
public class SearchHitsUtils {

    public static <T> List<T> getList(SearchHits<T> searchHits, Class<T> clazz) {
        List<T> tList = new ArrayList<>();

        List<SearchHit<T>> searchHitList = searchHits.getSearchHits();
        if (!CollectionUtils.isEmpty(searchHitList)) {
            for (SearchHit<T> searchHit : searchHitList) {
                tList.add(searchHit.getContent());
            }
        }

        return tList;
    }
}
