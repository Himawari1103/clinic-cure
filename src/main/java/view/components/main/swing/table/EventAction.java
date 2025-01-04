package view.components.main.swing.table;


public interface EventAction <T> {

    public void delete(T obj);

    public void update(T obj);

    public void insert(T obj);
}
