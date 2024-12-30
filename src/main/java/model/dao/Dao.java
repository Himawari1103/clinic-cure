package model.dao;

import java.util.ArrayList;

public interface Dao <T>{
    int insert(T value);
    int update(T value);
    int delete(T value);
    T selectById(String id);
    ArrayList<T> selectAll();
    ArrayList<T> selectByCondition(String condition);
}
