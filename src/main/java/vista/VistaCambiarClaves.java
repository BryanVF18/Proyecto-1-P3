package vista;

import controlador.LoginController;
import modelo.Usuario;

import javax.swing.*;
import java.awt.*;

public class VistaCambiarClaves extends JDialog {

    private JPasswordField txtClaveActual;
    private JPasswordField txtNuevaClave;
    private JPasswordField txtConfirmarClave;

    private final Usuario usuario;
    private final LoginController loginController;

    public VistaCambiarClaves(JFrame ventanaPadre, Usuario usuario) {
        super(ventanaPadre, "Cambiar clave", true);

        this.usuario = usuario;
        this.loginController = new LoginController();

        setSize(400, 280);
        setLocationRelativeTo(ventanaPadre);
        setResizable(false);

        crearInterfaz();
    }

    private void crearInterfaz() {
        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Clave actual:"), gbc);

        txtClaveActual = new JPasswordField(15);

        gbc.gridx = 1;
        panel.add(txtClaveActual, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Nueva clave:"), gbc);

        txtNuevaClave = new JPasswordField(15);

        gbc.gridx = 1;
        panel.add(txtNuevaClave, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Confirmar clave:"), gbc);

        txtConfirmarClave = new JPasswordField(15);

        gbc.gridx = 1;
        panel.add(txtConfirmarClave, gbc);

        JButton btnCambiar = new JButton("Cambiar clave");
        btnCambiar.addActionListener(e -> cambiarClave());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel();

        panelBotones.add(btnCambiar);
        panelBotones.add(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;

        panel.add(panelBotones, gbc);

        add(panel);
    }

    private void cambiarClave() {
        String claveActual = new String(txtClaveActual.getPassword());
        String nuevaClave = new String(txtNuevaClave.getPassword());
        String confirmarClave = new String(txtConfirmarClave.getPassword());

        if (claveActual.isEmpty()
                || nuevaClave.isEmpty()
                || confirmarClave.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe completar todos los campos",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!nuevaClave.equals(confirmarClave)) {
            JOptionPane.showMessageDialog(
                    this,
                    "La nueva clave y su confirmación no coinciden",
                    "Claves diferentes",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        boolean cambioExitoso = loginController.cambiarClave(
                usuario.getId(),
                claveActual,
                nuevaClave
        );

        if (!cambioExitoso) {
            JOptionPane.showMessageDialog(
                    this,
                    "La clave actual es incorrecta",
                    "No se pudo cambiar la clave",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        usuario.setClave(nuevaClave);

        JOptionPane.showMessageDialog(
                this,
                "Clave modificada correctamente"
        );

        dispose();
    }
}