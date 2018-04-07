package com.hcll.fishshrimpcrab.base;

import java.util.List;

/**
 * Created by hong on 2018/3/25.
 */

public interface BasePageAdapter<T> {
    void clearAll();

    void addAll(List<T> list);
}
