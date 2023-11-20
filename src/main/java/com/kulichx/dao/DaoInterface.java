package com.kulichx.dao;

import java.util.List;
import java.util.Optional;

public interface DaoInterface<T> {
    void save(T entity);

}