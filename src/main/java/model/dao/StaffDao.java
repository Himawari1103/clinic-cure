package model.dao;

import model.base.Staff;

import java.util.ArrayList;

public class StaffDao implements Dao<Staff>{
    public static StaffDao getInstance(){
        return new StaffDao();
    }

    @Override
    public int insert(Staff value) {
        return 0;
    }

    @Override
    public int update(Staff value) {
        return 0;
    }

    @Override
    public int delete(Staff value) {
        return 0;
    }

    @Override
    public Staff selectById(String id) {
        return null;
    }

    @Override
    public ArrayList<Staff> selectAll() {
        return null;
    }

    @Override
    public ArrayList<Staff> selectByCondition(String condition) {
        return null;
    }
}
