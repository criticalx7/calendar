package client.ui.editor;

import client.ui.EventAdapter;
import common.model.Event;

/*
 * @author Chatchapol Rasameluangon
 * id: 5810404901
 */
public class EditorViewModel {
    private final EventAdapter eventModel = new EventAdapter();
    private boolean confirm = false;

    void save() {
        eventModel.save();
        confirm = true;
    }

    EventAdapter getEventModel() {
        return eventModel;
    }

    public void setEvent(Event event) {
        eventModel.setEvent(event);
        eventModel.reload();
    }

    public boolean isConfirm() {
        return confirm;
    }
}
