package viewmodel.component;

public class ComponentFactory {

    public static DateCell createDateCell() {
        return new DateCell();
    }

    public static EventBox createEventBox() {
        return new EventBox();
    }
}
