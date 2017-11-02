package view;

class ComponentFactory {

    static DateCell createDateCell() {
        return new DateCell();
    }

    static EventBox createEventBox() {
        return new EventBox();
    }
}
