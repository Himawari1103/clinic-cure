package model.dao;

import model.base.Patient;

import java.util.ArrayList;

public class PatientDao implements Dao<Patient>{
    public static PatientDao getInstance(){
        return new PatientDao();
    }

    @Override
    public int insert(Patient value) {
        return 0;
    }

    @Override
    public int update(Patient value) {
        return 0;
    }

    @Override
    public int delete(Patient value) {
        return 0;
    }

    @Override
    public Patient selectById(String id) {
        return null;
    }

    @Override
    public ArrayList<Patient> selectAll() {
        return null;
    }

    @Override
    public ArrayList<Patient> selectByCondition(String condition) {
        return null;
    }
}
