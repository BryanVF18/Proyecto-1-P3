package vista;

import controlador.LoginController;
import modelo.Usuario;

import javax.swing.*;
import java.awt.*;

public class VistaLogin extends JFrame {

    private JTextField txtId;
    private JPasswordField txtClave;

    private final LoginController loginController;

    public VistaLogin() {
        loginController = new LoginController();

        setTitle("Sistema de Reserva de Recursos");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        crearInterfaz();
    }

    private void crearInterfaz() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        );

        JLabel lblTitulo = new JLabel(
                "Sistema de Reserva de Recursos",
                SwingConstants.CENTER
        );

        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));

        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelFormulario.add(new JLabel("ID:"), gbc);

        txtId = new JTextField(18);

        gbc.gridx = 1;
        panelFormulario.add(txtId, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelFormulario.add(new JLabel("Clave:"), gbc);

        txtClave = new JPasswordField(18);

        gbc.gridx = 1;
        panelFormulario.add(txtClave, gbc);

        JButton btnIngresar = new JButton("Ingresar");
        btnIngresar.addActionListener(e -> ingresar());

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panelFormulario.add(btnIngresar, gbc);

        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);

        add(panelPrincipal);

        getRootPane().setDefaultButton(btnIngresar);
    }

    private void ingresar() {
        String id = txtId.getText().trim();
        String clave = new String(txtClave.getPassword());

        if (id.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar el ID y la clave",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Usuario usuario = loginController.autenticar(id, clave);

        if (usuario == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "ID o clave incorrectos",
                    "Ingreso incorrecto",
                    JOptionPane.ERROR_MESSAGE
            );
            txtClave.setText("");
            return;
        }

        VistaPrincipal vistaPrincipal = new VistaPrincipal(usuario);
        vistaPrincipal.setVisible(true);

        dispose();
    }
}