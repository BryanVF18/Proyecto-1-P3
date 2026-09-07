package vista;

import controlador.CategoriaController;
import logica.CategoriaException;
import modelo.Categoria;
import reportes.GeneradorReportePDF;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PanelCategorias extends JPanel {

    private JTextField txtBuscarDescripcion;

    private JTextField txtId;
    private JTextField txtDescripcion;

    private JTable tablaCategorias;
    private DefaultTableModel modeloTabla;

    private final CategoriaController controlador = new CategoriaController();

    private boolean modoEdicion = false;

    public PanelCategorias() {

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

    private JPanel crearPanelBusqueda() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        panel.setBorder(new TitledBorder("Busqueda"));

        panel.add(new JLabel("Descripcion:"));
        txtBuscarDescripcion = new JTextField(20);
        panel.add(txtBuscarDescripcion);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscar());
        panel.add(btnBuscar);

        JButton btnImprimir = new JButton("Imprimir");
        btnImprimir.addActionListener(e -> imprimirReporte());
        panel.add(btnImprimir);

        return panel;
    }

    private JPanel crearPanelFormulario() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Categoria"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("ID:"), gbc);

        txtId = new JTextField(12);
        txtId.setEditable(false);
        txtId.setBackground(new Color(235, 235, 235));
        gbc.gridx = 1;
        panel.add(txtId, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Descripcion:"), gbc);

        txtDescripcion = new JTextField(20);
        gbc.gridx = 3;
        panel.add(txtDescripcion, gbc);

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
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        panel.add(botones, gbc);

        return panel;
    }

    private JScrollPane crearPanelTabla() {

        String[] columnas = {"Id", "Descripcion"};

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaCategorias = new JTable(modeloTabla);
        tablaCategorias.setRowHeight(26);
        tablaCategorias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tablaCategorias.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaCategorias.getSelectedRow() != -1) {
                cargarSeleccionEnFormulario();
            }
        });

        JScrollPane scroll = new JScrollPane(tablaCategorias);
        scroll.setBorder(new TitledBorder("Listado"));

        return scroll;
    }

    private void buscar() {

        String descripcion = txtBuscarDescripcion.getText().trim();
        List<Categoria> resultado = controlador.buscar(descripcion);
        actualizarTabla(resultado);
    }

    private void guardar() {

        String descripcion = txtDescripcion.getText().trim();

        try {

            if (modoEdicion) {
                controlador.modificar(txtId.getText(), descripcion);
                JOptionPane.showMessageDialog(this, "Categoria modificada correctamente.");
            } else {
                controlador.agregar(descripcion);
                JOptionPane.showMessageDialog(this, "Categoria agregada correctamente.");
            }

            limpiar();
            actualizarTabla(controlador.listarTodos());

        } catch (CategoriaException ex) {
            JOptionPane.showMessageDialog(
                    this, ex.getMessage(), "Error de validacion", JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void borrar() {

        int filaSeleccionada = tablaCategorias.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                    this, "Seleccione una categoria de la lista.", "Sin seleccion", JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String id = (String) modeloTabla.getValueAt(filaSeleccionada, 0);

        int confirmacion = JOptionPane.showConfirmDialog(
                this, "Desea eliminar la categoria " + id + "?", "Confirmar eliminacion", JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            controlador.eliminar(id);
            limpiar();
            actualizarTabla(controlador.listarTodos());

        } catch (CategoriaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiar() {

        txtId.setText("");
        txtDescripcion.setText("");

        modoEdicion = false;

        tablaCategorias.clearSelection();
    }

    private void cargarSeleccionEnFormulario() {

        int fila = tablaCategorias.getSelectedRow();

        txtId.setText((String) modeloTabla.getValueAt(fila, 0));
        txtDescripcion.setText((String) modeloTabla.getValueAt(fila, 1));

        modoEdicion = true;
    }

    private void actualizarTabla(List<Categoria> categorias) {

        modeloTabla.setRowCount(0);

        for (int i = 0; i < categorias.size(); i++) {
            Categoria c = categorias.get(i);

            modeloTabla.addRow(new Object[]{c.getId(), c.getDescripcion()});
        }
    }

    private void imprimirReporte() {

        JFileChooser selector = new JFileChooser();
        selector.setSelectedFile(new File("categorias.pdf"));

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
                    String.valueOf(modeloTabla.getValueAt(i, 1))
            });
        }

        try {
            GeneradorReportePDF.generar(
                    ruta, "Listado de Categorias", new String[]{"Id", "Descripcion"}, filas
            );
            JOptionPane.showMessageDialog(this, "Reporte generado en:\n" + ruta);

        } catch (IOException | com.lowagie.text.DocumentException ex) {
            JOptionPane.showMessageDialog(
                    this, "No se pudo generar el reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
