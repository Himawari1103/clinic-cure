package model.dao;

import model.Appointment;

import java.util.ArrayList;

public class AppointmentDao implements Dao<Appointment>{
    public static AppointmentDao getInstance(){
        return new AppointmentDao();
    }

    @Override
    public int insert(Appointment value) {
        return 0;
    }

    @Override
    public int update(Appointment value) {
        return 0;
    }

    @Override
    public int delete(Appointment value) {
        return 0;
    }

    @Override
    public Appointment selectById(String id) {
        return null;
    }

    @Override
    public ArrayList<Appointment> selectAll() {
        return null;
    }

    @Override
    public ArrayList<Appointment> selectByCondition(String condition) {
        return null;
    }
}
