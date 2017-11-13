package client.ui.editor;

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

// TODO - change validation into strategy

/*
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */
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
        nameErrorText.setVisible(false);
        if (nameField.getText().isEmpty()) {
            nameErrorText.setVisible(true);
            nameField.requestFocus();
        } else {
            viewModel.save();
            saveButton.getScene().getWindow().hide();
        }
    }

    private void bindModel() {
        viewModel.getEventModel().nameProperty().bindBidirectional(nameField.textProperty());
        viewModel.getEventModel().noteProperty().bindBidirectional(noteArea.textProperty());
        viewModel.getEventModel().startProperty().bindBidirectional(startPicker.valueProperty());
        viewModel.getEventModel().endProperty().bindBidirectional(startPicker.valueProperty());
        viewModel.getEventModel().colorProperty().bindBidirectional(colorPicker.valueProperty());
        viewModel.getEventModel().recurredProperty().bindBidirectional(repeatBox.selectedProperty());
        viewModel.getEventModel().yearlyProperty().bindBidirectional(yearly.selectedProperty());
        viewModel.getEventModel().monthlyProperty().bindBidirectional(monthly.selectedProperty());
        viewModel.getEventModel().colorProperty().addListener(
                (obs, oldColor, newColor) -> headerPane.setStyle(ColorUtil.getBorderColorFX(newColor)));
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
    }


}
