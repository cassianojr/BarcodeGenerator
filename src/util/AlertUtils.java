package util;

import javafx.scene.control.Alert;

/**
 * Utility class for JFX Alerts
 */
public class AlertUtils {

    /**
     * Create a success alert with the given header
     * @param header
     * @return alert
     */
    public static Alert successAlert(String header){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Gerador de Código de Barras");
        alert.setHeaderText(header);

        return alert;
    }

    /**
     * Create a error alert with the given header
     * @param header
     * @return alert
     */
    public static Alert errorAlert(String header){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Gerador de Código de Barras");
        alert.setHeaderText(header);

        return alert;
    }
}
