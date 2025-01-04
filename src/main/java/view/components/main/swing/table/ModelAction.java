package view.components.main.swing.table;

public class ModelAction<T>{

    public T getStudent() {
        return student;
    }

    public void setStudent(T student) {
        this.student = student;
    }

    public EventAction getEvent() {
        return event;
    }

    public void setEvent(EventAction event) {
        this.event = event;
    }

    public ModelAction(T student, EventAction event) {
        this.student = student;
        this.event = event;
    }

    public ModelAction() {
    }

    private T student;
    private EventAction event;
}
