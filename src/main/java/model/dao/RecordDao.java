package model.dao;

import model.base.Record;

import java.util.ArrayList;

public class RecordDao implements Dao<Record>{
    public static RecordDao getInstance(){
        return new RecordDao();
    }

    @Override
    public int insert(Record value) {
        return 0;
    }

    @Override
    public int update(Record value) {
        return 0;
    }

    @Override
    public int delete(Record value) {
        return 0;
    }

    @Override
    public Record selectById(String id) {
        return null;
    }

    @Override
    public ArrayList<Record> selectAll() {
        return null;
    }

    @Override
    public ArrayList<Record> selectByCondition(String condition) {
        return null;
    }
}
