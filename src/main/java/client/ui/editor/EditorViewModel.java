package client.ui.editor;

/*
 * @author Chatchapol Rasameluangon
 * id: 5810404901
 */

import client.controls.EventAdapter;


public class EditorViewModel {
    private final EventAdapter eventModel;
    private boolean confirm = false;

    public EditorViewModel(EventAdapter eventModel) {
        this.eventModel = eventModel;
    }

    void save() {
        eventModel.save();
        confirm = true;
    }

    EventAdapter getEventModel() {
        return eventModel;
    }

    public boolean isConfirm() {
        return confirm;
    }
}
