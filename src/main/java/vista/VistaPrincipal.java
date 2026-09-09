package vista;

import modelo.Administrador;
import modelo.Funcionario;
import modelo.Usuario;

import javax.swing.*;
import java.awt.*;

public class VistaPrincipal extends JFrame {

    private final Usuario usuarioActual;

    private JTabbedPane pestañas;

    public VistaPrincipal(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;

        setTitle("Sistema de Reserva de Recursos");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        crearInterfaz();
    }

    private void crearInterfaz() {
        setLayout(new BorderLayout());

        add(crearPanelSuperior(), BorderLayout.NORTH);

        pestañas = new JTabbedPane();

        cargarPestañas();

        add(pestañas, BorderLayout.CENTER);
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());

        panel.setBorder(
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        );

        JLabel lblUsuario = new JLabel(
                "Usuario: " + usuarioActual.getId()
                        + " | Rol: " + usuarioActual.getRol()
        );

        panel.add(lblUsuario, BorderLayout.WEST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnCambiarClave = new JButton("Cambiar clave");
        JButton btnCerrarSesion = new JButton("Cerrar sesión");

        btnCambiarClave.addActionListener(
                e -> abrirCambioClave()
        );

        btnCerrarSesion.addActionListener(
                e -> cerrarSesion()
        );

        panelBotones.add(btnCambiarClave);
        panelBotones.add(btnCerrarSesion);

        panel.add(panelBotones, BorderLayout.EAST);

        return panel;
    }

    private void cargarPestañas() {
        if (usuarioActual instanceof Administrador) {
            cargarPestañasAdministrador();
        } else if (usuarioActual instanceof Funcionario) {
            cargarPestañasFuncionario();
        }
    }

    private void cargarPestañasAdministrador() {
        pestañas.addTab(
                "Funcionarios",
                new PanelFuncionarios()
        );

        pestañas.addTab(
                "Categorías",
                new PanelCategorias()
        );

        pestañas.addTab(
                "Recursos",
                new PanelRecursos()
        );
    }

    private void cargarPestañasFuncionario() {
        Funcionario funcionario = (Funcionario) usuarioActual;

        pestañas.addTab(
                "Reservas",
                new PanelReservas(funcionario)
        );
    }

    private void abrirCambioClave() {
        VistaCambiarClaves ventana = new VistaCambiarClaves(
                this,
                usuarioActual
        );

        ventana.setVisible(true);
    }

    private void cerrarSesion() {
        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Desea cerrar la sesión?",
                "Cerrar sesión",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            dispose();

            VistaLogin login = new VistaLogin();
            login.setVisible(true);
        }
    }
}