package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.stage.DirectoryChooser;
import util.AlertUtils;
import util.Barcodes;
import view.Main;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * This is the main Controller Class, that handle the UI
 */
public class MainController implements Initializable {


    @FXML
    private TextField txtSingleCode;

    @FXML
    private TextField txtSingleFileChooser;

    @FXML
    private TextField txtMultiFileChooser;

    @FXML
    private TextArea txtMultiCode;

    @FXML
    private ComboBox<String> cbSingleBarcodeType;

    @FXML
    private ComboBox<String> cbMultiBarcodeType;

    private String saveDestination = "";

    /**
     * Handle the Button to generate multiple barcodes at once
     */
    @FXML
    void handleGenerateMulti() {
        String codes = txtMultiCode.getText();
        String[] values = codes.split("\n");
        String codeType = getCodeType(cbMultiBarcodeType);


        //Check the empty fields
        if(codes.equals("")){
            AlertUtils.errorAlert("O campo códigos está vazio!").show();
            return;
        }
        if(saveDestination.equals("")){
            AlertUtils.errorAlert("Por favor, selecione onde você deseja salvar o arquivo!").show();
            return;
        }

        //Call the create pdf method and throw the error if something goes wrong
        try {
            Barcodes.createPdf(Barcodes.getRandomFileName(saveDestination), values, codeType);
            AlertUtils.successAlert("Códigos gerados com sucesso").show();
            txtMultiCode.setText("");

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtils.errorAlert("Ocorreu um erro ao gerar os códigos de barras!");
        }
    }

    /**
     * Handle the Button to generate a single barcode file
     */
    @FXML
    void handleGenerateSingle() {
        String code = txtSingleCode.getText();
        String codeType = getCodeType(cbSingleBarcodeType);

        //check empty fields
        if(code.equals("")){
            AlertUtils.errorAlert("O campo código está vazio!").show();
            return;
        }
        if(saveDestination.equals("")){
            AlertUtils.errorAlert("Por favor, selecione onde você deseja salvar o arquivo!").show();
            return;
        }

        //Call the createSingleBarcode method and throw error if something goes wrong
        try {
            Barcodes.createSingleBarcode(code, saveDestination, codeType);
            AlertUtils.successAlert("Código gerado com sucesso").show();
            txtSingleCode.setText("");

        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.errorAlert("Ocorreu um erro ao gerar o código de barras!");
        }
    }

    private String getCodeType(ComboBox<String> cbSingleBarcodeType) {
        return cbSingleBarcodeType.getValue().equals(Barcodes.BARCODE_128) ? Barcodes.BARCODE_128 : Barcodes.BARCODE_INTER_2_5;
    }

    /**
     * Handle de file chooser button, open a directoryChooser window and save the result
     */
    @FXML
    void handleFileChooser() {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(Main.getStage());
        if(selectedDirectory != null){
            saveDestination = selectedDirectory.getAbsolutePath()+"\\";
            txtMultiFileChooser.setText(saveDestination);
            txtSingleFileChooser.setText(saveDestination);
        }
    }

    /**
     * Add the filters to the text fields
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addCharFilter(txtSingleCode);
        addCharFilter(txtMultiCode);

        addComboBoxValues(cbMultiBarcodeType);
        addComboBoxValues(cbSingleBarcodeType);
    }

    /**
     * Add the barcode type for the comboBox
     * @param comboBox
     */
    private void addComboBoxValues(ComboBox<String> comboBox){

        ArrayList<String> cbValues = new ArrayList<>();
        cbValues.add("CODE_128");
        cbValues.add("INTERLEAVED_2_OF_5");

        comboBox.setItems(FXCollections.observableArrayList(cbValues));
    }

    /**
     * Create a simple filter to the textfield, if the typed char is not a number, this will add a breakline character (\n)
     * @param textField
     */
    private void addCharFilter(TextInputControl textField) {
        textField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", "\n"));
            }
        });
    }
}
