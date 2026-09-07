package vista;

import controlador.CategoriaController;
import controlador.RecursoController;
import logica.RecursoException;
import modelo.Categoria;
import modelo.Recurso;
import reportes.GeneradorReportePDF;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PanelRecursos extends JPanel {

    private static final Categoria TODAS_LAS_CATEGORIAS = new Categoria(null, "Todas");

    private JComboBox<Categoria> cmbFiltroCategoria;
    private JTextField txtBuscarDescripcion;

    private JTextField txtId;
    private JComboBox<Categoria> cmbCategoriaFormulario;
    private JTextField txtDescripcion;

    private JTable tablaRecursos;
    private DefaultTableModel modeloTabla;

    private final RecursoController controlador = new RecursoController();
    private final CategoriaController categoriaController = new CategoriaController();

    private List<Categoria> categoriasDisponibles = new ArrayList<>();
    private List<Recurso> recursosMostrados = new ArrayList<>();

    private boolean modoEdicion = false;

    public PanelRecursos() {

        setLayout(new BorderLayout(10, 10));

        cargarCategoriasDisponibles();

        JPanel superior = new JPanel();
        superior.setLayout(new BoxLayout(superior, BoxLayout.Y_AXIS));

        superior.add(crearPanelFiltro());
        superior.add(Box.createVerticalStrut(8));
        superior.add(crearPanelFormulario());

        add(superior, BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);

        actualizarTabla(controlador.listarTodos());
    }

    private void cargarCategoriasDisponibles() {
        categoriasDisponibles = categoriaController.listarTodos();
    }

    private DefaultListCellRenderer crearRendererDeCategoria() {

        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> lista, Object valor, int indice,
                    boolean seleccionado, boolean tieneFoco) {

                JLabel etiqueta = (JLabel) super.getListCellRendererComponent(
                        lista, valor, indice, seleccionado, tieneFoco
                );

                if (valor instanceof Categoria) {
                    etiqueta.setText(((Categoria) valor).getDescripcion());
                }

                return etiqueta;
            }
        };
    }

    private JPanel crearPanelFiltro() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        panel.setBorder(new TitledBorder("Filtro"));

        panel.add(new JLabel("Categoria:"));

        cmbFiltroCategoria = new JComboBox<>();
        cmbFiltroCategoria.setRenderer(crearRendererDeCategoria());
        cmbFiltroCategoria.addItem(TODAS_LAS_CATEGORIAS);
        for (int i = 0; i < categoriasDisponibles.size(); i++) {
            cmbFiltroCategoria.addItem(categoriasDisponibles.get(i));
        }
        panel.add(cmbFiltroCategoria);

        panel.add(new JLabel("Descripcion:"));
        txtBuscarDescripcion = new JTextField(15);
        panel.add(txtBuscarDescripcion);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscar());
        panel.add(btnBuscar);

        JButton btnImprimir = new JButton("Imprimir");
        btnImprimir.addActionListener(e -> imprimirReporte());
        panel.add(btnImprimir);

        JButton btnActualizarCategorias = new JButton("Actualizar categorias");
        btnActualizarCategorias.addActionListener(e -> actualizarCombosDeCategoria());
        panel.add(btnActualizarCategorias);

        return panel;
    }

    private void actualizarCombosDeCategoria() {

        cargarCategoriasDisponibles();

        cmbFiltroCategoria.removeAllItems();
        cmbFiltroCategoria.addItem(TODAS_LAS_CATEGORIAS);
        for (int i = 0; i < categoriasDisponibles.size(); i++) {
            cmbFiltroCategoria.addItem(categoriasDisponibles.get(i));
        }

        cmbCategoriaFormulario.removeAllItems();
        for (int i = 0; i < categoriasDisponibles.size(); i++) {
            cmbCategoriaFormulario.addItem(categoriasDisponibles.get(i));
        }
    }

    private JPanel crearPanelFormulario() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Recurso"));

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
        panel.add(new JLabel("Categoria:"), gbc);

        cmbCategoriaFormulario = new JComboBox<>();
        cmbCategoriaFormulario.setRenderer(crearRendererDeCategoria());
        for (int i = 0; i < categoriasDisponibles.size(); i++) {
            cmbCategoriaFormulario.addItem(categoriasDisponibles.get(i));
        }
        gbc.gridx = 3;
        panel.add(cmbCategoriaFormulario, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Descripcion:"), gbc);

        txtDescripcion = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        panel.add(txtDescripcion, gbc);
        gbc.gridwidth = 1;

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

    private JScrollPane crearPanelTabla() {

        String[] columnas = {"Id", "Categoria", "Descripcion"};

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaRecursos = new JTable(modeloTabla);
        tablaRecursos.setRowHeight(26);
        tablaRecursos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tablaRecursos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaRecursos.getSelectedRow() != -1) {
                cargarSeleccionEnFormulario();
            }
        });

        JScrollPane scroll = new JScrollPane(tablaRecursos);
        scroll.setBorder(new TitledBorder("Listado"));

        return scroll;
    }

    private void buscar() {

        Categoria categoriaSeleccionada = (Categoria) cmbFiltroCategoria.getSelectedItem();
        String categoriaId = (categoriaSeleccionada == TODAS_LAS_CATEGORIAS)
                ? null
                : categoriaSeleccionada.getId();

        String descripcion = txtBuscarDescripcion.getText().trim();

        List<Recurso> resultado = controlador.buscar(categoriaId, descripcion);
        actualizarTabla(resultado);
    }

    private void guardar() {

        String id = txtId.getText().trim();
        String descripcion = txtDescripcion.getText().trim();

        Categoria categoriaSeleccionada = (Categoria) cmbCategoriaFormulario.getSelectedItem();

        if (categoriaSeleccionada == null) {
            JOptionPane.showMessageDialog(
                    this, "Debe existir al menos una categoria antes de crear un recurso.",
                    "Sin categorias", JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String categoriaId = categoriaSeleccionada.getId();

        try {

            if (modoEdicion) {
                controlador.modificar(id, categoriaId, descripcion);
                JOptionPane.showMessageDialog(this, "Recurso modificado correctamente.");
            } else {
                controlador.agregar(id, categoriaId, descripcion);
                JOptionPane.showMessageDialog(this, "Recurso agregado correctamente.");
            }

            limpiar();
            actualizarTabla(controlador.listarTodos());

        } catch (RecursoException ex) {
            JOptionPane.showMessageDialog(
                    this, ex.getMessage(), "Error de validacion", JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void borrar() {

        int filaSeleccionada = tablaRecursos.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                    this, "Seleccione un recurso de la lista.", "Sin seleccion", JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String id = (String) modeloTabla.getValueAt(filaSeleccionada, 0);

        int confirmacion = JOptionPane.showConfirmDialog(
                this, "Desea eliminar el recurso " + id + "?", "Confirmar eliminacion", JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            controlador.eliminar(id);
            limpiar();
            actualizarTabla(controlador.listarTodos());

        } catch (RecursoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiar() {

        txtId.setText("");
        txtDescripcion.setText("");

        if (cmbCategoriaFormulario.getItemCount() > 0) {
            cmbCategoriaFormulario.setSelectedIndex(0);
        }

        txtId.setEditable(true);
        modoEdicion = false;

        tablaRecursos.clearSelection();
    }

    private void cargarSeleccionEnFormulario() {

        int fila = tablaRecursos.getSelectedRow();

        Recurso recurso = recursosMostrados.get(fila);

        txtId.setText(recurso.getId());
        txtDescripcion.setText(recurso.getDescripcion());

        seleccionarCategoriaPorId(recurso.getCategoria());

        txtId.setEditable(false);
        modoEdicion = true;
    }

    private void seleccionarCategoriaPorId(String categoriaId) {

        for (int i = 0; i < cmbCategoriaFormulario.getItemCount(); i++) {
            Categoria c = cmbCategoriaFormulario.getItemAt(i);

            if (c.getId() != null && c.getId().equals(categoriaId)) {
                cmbCategoriaFormulario.setSelectedIndex(i);
                return;
            }
        }
    }

    private void actualizarTabla(List<Recurso> recursos) {

        recursosMostrados = recursos;
        modeloTabla.setRowCount(0);

        for (int i = 0; i < recursos.size(); i++) {
            Recurso r = recursos.get(i);

            modeloTabla.addRow(new Object[]{
                    r.getId(), obtenerDescripcionCategoria(r.getCategoria()), r.getDescripcion()
            });
        }
    }

    private String obtenerDescripcionCategoria(String categoriaId) {

        for (int i = 0; i < categoriasDisponibles.size(); i++) {
            Categoria c = categoriasDisponibles.get(i);

            if (c.getId() != null && c.getId().equals(categoriaId)) {
                return c.getDescripcion();
            }
        }

        return categoriaId;
    }

    private void imprimirReporte() {

        JFileChooser selector = new JFileChooser();
        selector.setSelectedFile(new File("recursos.pdf"));

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
                    ruta, "Listado de Recursos", new String[]{"Id", "Categoria", "Descripcion"}, filas
            );
            JOptionPane.showMessageDialog(this, "Reporte generado en:\n" + ruta);

        } catch (IOException | com.lowagie.text.DocumentException ex) {
            JOptionPane.showMessageDialog(
                    this, "No se pudo generar el reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
