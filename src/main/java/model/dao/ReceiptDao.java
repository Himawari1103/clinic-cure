package model.dao;

import model.Receipt;

import java.util.ArrayList;

public class ReceiptDao implements Dao<Receipt>{
    public static ReceiptDao getInstance(){
        return new ReceiptDao();
    }

    @Override
    public int insert(Receipt value) {
        return 0;
    }

    @Override
    public int update(Receipt value) {
        return 0;
    }

    @Override
    public int delete(Receipt value) {
        return 0;
    }

    @Override
    public Receipt selectById(String id) {
        return null;
    }

    @Override
    public ArrayList<Receipt> selectAll() {
        return null;
    }

    @Override
    public ArrayList<Receipt> selectByCondition(String condition) {
        return null;
    }
}
