package vista;

import controlador.FuncionarioController;
import logica.FuncionarioException;
import modelo.Funcionario;
import reportes.GeneradorReportePDF;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PanelFuncionarios extends JPanel {

    private JTextField txtBuscarId;
    private JTextField txtBuscarNombre;

    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtTelefono;

    private JTable tablaFuncionarios;
    private DefaultTableModel modeloTabla;

    private final FuncionarioController controlador = new FuncionarioController();

    private boolean modoEdicion = false;

    public PanelFuncionarios() {

        setLayout(new BorderLayout(10, 10));

        JPanel superior = new JPanel();
        superior.setLayout(new BoxLayout(superior, BoxLayout.Y_AXIS));

        superior.add(crearPanelBusqueda());
        superior.add(Box.createVerticalStrut(8));
        superior.add(crearPanelFormulario());

        add(superior, BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);

        actualizarTabla(controlador.listarTodos());
    }

    // Busqueda

    private JPanel crearPanelBusqueda() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        panel.setBorder(new TitledBorder("Busqueda"));

        panel.add(new JLabel("ID:"));
        txtBuscarId = new JTextField(10);
        panel.add(txtBuscarId);

        panel.add(new JLabel("Nombre:"));
        txtBuscarNombre = new JTextField(15);
        panel.add(txtBuscarNombre);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscar());
        panel.add(btnBuscar);

        JButton btnImprimir = new JButton("Imprimir");
        btnImprimir.addActionListener(e -> imprimirReporte());
        panel.add(btnImprimir);

        return panel;
    }

    // Formulario

    private JPanel crearPanelFormulario() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Funcionario"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("ID:"), gbc);

        txtId = new JTextField(15);
        gbc.gridx = 1;
        panel.add(txtId, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Nombre:"), gbc);

        txtNombre = new JTextField(15);
        gbc.gridx = 3;
        panel.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Telefono:"), gbc);

        txtTelefono = new JTextField(15);
        gbc.gridx = 1;
        panel.add(txtTelefono, gbc);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardar());

        JButton btnBorrar = new JButton("Borrar");
        btnBorrar.addActionListener(e -> borrar());

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> limpiar());

        botones.add(btnGuardar);
        botones.add(btnBorrar);
        botones.add(btnLimpiar);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        panel.add(botones, gbc);

        return panel;
    }


    // Tabla

    private JScrollPane crearPanelTabla() {

        String[] columnas = {"Id", "Nombre", "Telefono"};

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaFuncionarios = new JTable(modeloTabla);
        tablaFuncionarios.setRowHeight(26);
        tablaFuncionarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tablaFuncionarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaFuncionarios.getSelectedRow() != -1) {
                cargarSeleccionEnFormulario();
            }
        });

        JScrollPane scroll = new JScrollPane(tablaFuncionarios);
        scroll.setBorder(new TitledBorder("Listado"));

        return scroll;
    }


    // Acciones

    private void buscar() {

        String id = txtBuscarId.getText().trim();
        String nombre = txtBuscarNombre.getText().trim();

        List<Funcionario> resultado = controlador.buscar(id, nombre);
        actualizarTabla(resultado);
    }

    private void guardar() {

        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();

        try {

            if (modoEdicion) {
                controlador.modificar(id, nombre, telefono);
                JOptionPane.showMessageDialog(this, "Funcionario modificado correctamente.");
            } else {
                controlador.agregar(id, nombre, telefono);
                JOptionPane.showMessageDialog(this, "Funcionario agregado correctamente.");
            }

            limpiar();
            actualizarTabla(controlador.listarTodos());

        } catch (FuncionarioException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error de validacion",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void borrar() {

        int filaSeleccionada = tablaFuncionarios.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un funcionario de la lista.",
                    "Sin seleccion",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String id = (String) modeloTabla.getValueAt(filaSeleccionada, 0);

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "Desea eliminar al funcionario " + id + "?",
                "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            controlador.eliminar(id);
            limpiar();
            actualizarTabla(controlador.listarTodos());

        } catch (FuncionarioException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void limpiar() {

        txtId.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");

        txtId.setEditable(true);
        modoEdicion = false;

        tablaFuncionarios.clearSelection();
    }

    private void cargarSeleccionEnFormulario() {

        int fila = tablaFuncionarios.getSelectedRow();

        txtId.setText((String) modeloTabla.getValueAt(fila, 0));
        txtNombre.setText((String) modeloTabla.getValueAt(fila, 1));
        txtTelefono.setText((String) modeloTabla.getValueAt(fila, 2));

        // Mientras se edita no se deja tocar el id, porque es la
        // llave que usan FuncionarioService y Usuario para el login.
        txtId.setEditable(false);
        modoEdicion = true;
    }

    private void actualizarTabla(List<Funcionario> funcionarios) {

        modeloTabla.setRowCount(0);

        for (int i = 0; i < funcionarios.size(); i++) {
            Funcionario f = funcionarios.get(i);

            modeloTabla.addRow(new Object[]{
                    f.getId(),
                    f.getNombre(),
                    f.getTelefono()
            });
        }
    }


    // Reporte PDF

    private void imprimirReporte() {

        JFileChooser selector = new JFileChooser();
        selector.setSelectedFile(new File("funcionarios.pdf"));

        int opcion = selector.showSaveDialog(this);

        if (opcion != JFileChooser.APPROVE_OPTION) {
            return;
        }

        String ruta = selector.getSelectedFile().getAbsolutePath();
        if (!ruta.toLowerCase().endsWith(".pdf")) {
            ruta = ruta + ".pdf";
        }

        List<String[]> filas = new ArrayList<>();

        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            filas.add(new String[]{
                    String.valueOf(modeloTabla.getValueAt(i, 0)),
                    String.valueOf(modeloTabla.getValueAt(i, 1)),
                    String.valueOf(modeloTabla.getValueAt(i, 2))
            });
        }

        try {
            GeneradorReportePDF.generar(
                    ruta,
                    "Listado de Funcionarios",
                    new String[]{"Id", "Nombre", "Telefono"},
                    filas
            );

            JOptionPane.showMessageDialog(this, "Reporte generado en:\n" + ruta);

        } catch (IOException | com.lowagie.text.DocumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo generar el reporte: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
