package vista;

import controlador.CategoriaController;
import controlador.ReservaController;
import logica.ReservaException;
import modelo.Categoria;
import modelo.Funcionario;
import modelo.Recurso;
import modelo.Reserva;
import modelo.SolicitudReserva;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PanelReserva extends JPanel {

    private JTextField txtActividad;
    private JSpinner spnFecha;
    private JComboBox<String> cmbHoraInicio;
    private JComboBox<String> cmbHoraFin;
    private JList<String> listaCategorias;
    private DefaultListModel<String> modeloCategorias;
    private JTable tablaReservas;
    private DefaultTableModel modeloTabla;

    private final ReservaController reservaController;
    private final CategoriaController categoriaController;
    private final Funcionario funcionarioActual;

    private List<Categoria> categoriasDisponibles;

    public PanelReserva(Funcionario funcionarioActual) {
        this.funcionarioActual = funcionarioActual;
        this.reservaController = new ReservaController();
        this.categoriaController = new CategoriaController();
        this.categoriasDisponibles = new ArrayList<>();

        setLayout(new BorderLayout(10, 10));

        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);

        cargarCategorias();
        actualizarTabla();
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Nueva reserva"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Actividad:"), gbc);

        txtActividad = new JTextField(25);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        panel.add(txtActividad, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Fecha:"), gbc);

        spnFecha = new JSpinner(new SpinnerDateModel());
        spnFecha.setEditor(new JSpinner.DateEditor(spnFecha, "dd/MM/yyyy"));

        gbc.gridx = 1;
        panel.add(spnFecha, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Hora inicio:"), gbc);

        cmbHoraInicio = new JComboBox<>(crearHoras());
        cmbHoraInicio.setSelectedItem("08:00");

        gbc.gridx = 3;
        panel.add(cmbHoraInicio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Categorías:"), gbc);

        modeloCategorias = new DefaultListModel<>();
        listaCategorias = new JList<>(modeloCategorias);
        listaCategorias.setSelectionMode(
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
        );
        listaCategorias.setVisibleRowCount(4);

        JScrollPane scrollCategorias = new JScrollPane(listaCategorias);
        scrollCategorias.setPreferredSize(new Dimension(250, 90));

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridheight = 2;
        panel.add(scrollCategorias, gbc);

        gbc.gridheight = 1;

        gbc.gridx = 2;
        gbc.gridy = 2;
        panel.add(new JLabel("Hora fin:"), gbc);

        cmbHoraFin = new JComboBox<>(crearHoras());
        cmbHoraFin.setSelectedItem("09:00");

        gbc.gridx = 3;
        panel.add(cmbHoraFin, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnReservar = new JButton("Reservar");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnActualizar = new JButton("Actualizar lista");

        btnReservar.addActionListener(e -> reservar());
        btnLimpiar.addActionListener(e -> limpiar());
        btnActualizar.addActionListener(e -> actualizarTabla());

        panelBotones.add(btnReservar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnActualizar);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 4;
        panel.add(panelBotones, gbc);

        return panel;
    }

    private JScrollPane crearPanelTabla() {
        String[] columnas = {"Id", "Actividad", "Fecha", "Horario", "Recursos", "Estado"};

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaReservas = new JTable(modeloTabla);
        tablaReservas.setRowHeight(25);
        tablaReservas.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scroll = new JScrollPane(tablaReservas);
        scroll.setBorder(new TitledBorder("Mis reservas"));

        return scroll;
    }

    private void cargarCategorias() {
        categoriasDisponibles = categoriaController.listarTodos();
        modeloCategorias.clear();

        for (int i = 0; i < categoriasDisponibles.size(); i++) {
            Categoria categoria = categoriasDisponibles.get(i);
            modeloCategorias.addElement(categoria.getDescripcion());
        }
    }

    private void reservar() {
        SolicitudReserva solicitud = crearSolicitudDesdeFormulario();

        try {
            Reserva reserva = reservaController.crearReserva(
                    solicitud,
                    funcionarioActual
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Reserva " + reserva.getId() + " creada correctamente."
            );

            limpiar();
            actualizarTabla();

        } catch (ReservaException e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "No fue posible realizar la reserva",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private SolicitudReserva crearSolicitudDesdeFormulario() {
        String actividad = txtActividad.getText();

        LocalDate fecha = obtenerFechaSeleccionada();

        LocalTime horaInicio = LocalTime.parse(
                (String) cmbHoraInicio.getSelectedItem()
        );

        LocalTime horaFin = LocalTime.parse(
                (String) cmbHoraFin.getSelectedItem()
        );

        List<String> idsCategorias = obtenerCategoriasSeleccionadas();

        return new SolicitudReserva(actividad, fecha, horaInicio, horaFin, idsCategorias);
    }

    private LocalDate obtenerFechaSeleccionada() {
        Date fecha = (Date) spnFecha.getValue();

        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private List<String> obtenerCategoriasSeleccionadas() {
        List<String> idsCategorias = new ArrayList<>();

        int[] indices = listaCategorias.getSelectedIndices();

        for (int i = 0; i < indices.length; i++) {
            Categoria categoria = categoriasDisponibles.get(indices[i]);
            idsCategorias.add(categoria.getId());
        }

        return idsCategorias;
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);

        if (funcionarioActual == null || funcionarioActual.getId() == null) {
            return;
        }

        List<Reserva> reservas = reservaController.listarPorFuncionario(
                funcionarioActual.getId()
        );

        for (int i = 0; i < reservas.size(); i++) {
            Reserva reserva = reservas.get(i);

            modeloTabla.addRow(new Object[]{
                    reserva.getId(),
                    reserva.getActividad(),
                    reserva.getFecha(),
                    reserva.getHoraInicio() + " - " + reserva.getHoraFin(),
                    obtenerTextoRecursos(reserva),
                    reserva.getEstado()
            });
        }
    }

    private String obtenerTextoRecursos(Reserva reserva) {
        List<Recurso> recursos = reserva.getRecursosAsignados();

        if (recursos == null || recursos.isEmpty()) {
            return "";
        }

        String texto = "";

        for (int i = 0; i < recursos.size(); i++) {
            texto += recursos.get(i).getId();

            if (i < recursos.size() - 1) {
                texto += ", ";
            }
        }

        return texto;
    }

    private String[] crearHoras() {
        String[] horas = new String[17];
        int posicion = 0;

        for (int hora = 6; hora <= 22; hora++) {
            horas[posicion] = String.format("%02d:00", hora);
            posicion++;
        }

        return horas;
    }

    private void limpiar() {
        txtActividad.setText("");
        spnFecha.setValue(new Date());
        cmbHoraInicio.setSelectedItem("08:00");
        cmbHoraFin.setSelectedItem("09:00");
        listaCategorias.clearSelection();
    }
}