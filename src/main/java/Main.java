import vista.VistaLogin;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VistaLogin login = new VistaLogin();
            login.setVisible(true);
        });
    }
}