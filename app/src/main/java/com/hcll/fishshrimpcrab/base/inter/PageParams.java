package com.hcll.fishshrimpcrab.base.inter;

import java.util.List;

/**
 * Created by hong on 2018/3/26.
 */

public interface PageParams<T> {

    boolean hasMore();

    List<T> getList();
}
