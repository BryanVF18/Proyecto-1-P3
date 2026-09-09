package vista;

import modelo.Administrador;
import modelo.Funcionario;
import modelo.Usuario;

import javax.swing.*;
import java.awt.*;

public class VistaPrincipal extends JFrame {

    private final Usuario usuarioActual;

    private JTabbedPane pestanas;

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

        pestanas = new JTabbedPane();

        cargarPestanas();

        add(pestanas, BorderLayout.CENTER);
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
        JButton btnCerrarSesion = new JButton("Cerrar sesion");

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

    private void cargarPestanas() {
        if (usuarioActual instanceof Administrador) {
            cargarPestanasAdministrador();

        } else if (usuarioActual instanceof Funcionario) {
            cargarPestanasFuncionario();
        }
    }

    private void cargarPestanasAdministrador() {
        pestanas.addTab(
                "Funcionarios",
                new PanelFuncionarios()
        );

        pestanas.addTab(
                "Categorias",
                new PanelCategorias()
        );

        pestanas.addTab(
                "Recursos",
                new PanelRecursos()
        );

        pestanas.addTab(
                "Calendarizacion",
                new PanelCalendarizacion()
        );
    }

    private void cargarPestanasFuncionario() {
        Funcionario funcionario = (Funcionario) usuarioActual;

        pestanas.addTab(
                "Reservas",
                new PanelReservas(funcionario)
        );

        pestanas.addTab(
                "Calendarizacion",
                new PanelCalendarizacion()
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
                "Desea cerrar la sesion?",
                "Cerrar sesion",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            dispose();

            VistaLogin login = new VistaLogin();
            login.setVisible(true);
        }
    }
}