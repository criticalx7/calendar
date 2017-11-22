package client.ui.editor;

// TODO change validation into strategy

/*
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */

import client.config.Setting;
import client.utility.ColorUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class EditorView {
    @FXML
    private AnchorPane headerPane;
    @FXML
    private Label dayLabel, monthYearLabel, nameErrorText;
    @FXML
    private TextField nameField;
    @FXML
    private DatePicker startPicker;
    @FXML
    private HBox repeatFrame;
    @FXML
    private CheckBox repeatBox, allDayBox;
    @FXML
    public ToggleGroup repeatGroup;
    @FXML
    private RadioButton yearly, monthly, daily;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private TextArea noteArea;
    @FXML
    private Button saveButton;

    private EditorViewModel viewModel;

    public void initialize() {
        formatDatePicker(startPicker);
        repeatFrame.visibleProperty().bind(repeatBox.selectedProperty());
    }

    private void formatDatePicker(DatePicker picker) {
        DateTimeFormatter dt = Setting.getDatePattern();
        picker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? dt.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return string != null && !string.isEmpty() ? LocalDate.parse(string, dt) : null;
            }
        });
    }

    @FXML
    void onRepeat() {
        repeatGroup.selectToggle(repeatBox.isSelected() ? monthly : null);
    }

    @FXML
    public void onAdd() {
        if (nameField.getText().isEmpty()) {
            nameErrorText.setVisible(true);
            nameField.requestFocus();
        } else {
            viewModel.save();
            saveButton.getScene().getWindow().hide();
        }
    }

    @FXML
    public void onDateSelect() {
        String day = String.valueOf(startPicker.getValue().getDayOfMonth());
        String month = startPicker.getValue().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        String year = String.valueOf(startPicker.getValue().getYear());
        dayLabel.setText(day);
        monthYearLabel.setText(String.format("%s  %s", month, year));
    }

    public void setViewModel(EditorViewModel viewModel) {
        this.viewModel = viewModel;
        bindModel();
        onDateSelect();
    }

    private void bindModel() {
        if (viewModel.getEventModel() != null) {
            nameField.textProperty().bindBidirectional(viewModel.getEventModel().nameProperty());
            noteArea.textProperty().bindBidirectional(viewModel.getEventModel().noteProperty());
            startPicker.valueProperty().bindBidirectional(viewModel.getEventModel().startProperty());
            startPicker.valueProperty().bindBidirectional(viewModel.getEventModel().endProperty());
            colorPicker.valueProperty().bindBidirectional(viewModel.getEventModel().colorProperty());
            repeatBox.selectedProperty().bindBidirectional(viewModel.getEventModel().recurredProperty());
            yearly.selectedProperty().bindBidirectional(viewModel.getEventModel().yearlyProperty());
            monthly.selectedProperty().bindBidirectional(viewModel.getEventModel().monthlyProperty());
            colorPicker.valueProperty().addListener(
                    (obs, oldColor, newColor) -> headerPane.setStyle(ColorUtil.getBorderColorFX(newColor)));
            viewModel.getEventModel().reload();
        }
    }


}
