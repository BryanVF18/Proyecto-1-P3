package vista;

import controlador.CalendarizacionController;
import modelo.Categoria;
import modelo.Recurso;
import modelo.Reserva;

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

public class PanelCalendarizacion extends JPanel {

    private JSpinner spnFecha;
    private JComboBox<String> cmbCategoria;
    private JTable tablaCalendarizacion;
    private DefaultTableModel modeloTabla;

    private final CalendarizacionController calendarizacionController;

    private List<Categoria> categoriasDisponibles;

    public PanelCalendarizacion() {

        this.calendarizacionController = new CalendarizacionController();
        this.categoriasDisponibles = new ArrayList<>();

        setLayout(new BorderLayout(10, 10));

        add(crearPanelFiltros(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);

        cargarCategorias();
        actualizarTabla();
    }

    private JPanel crearPanelFiltros() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(new TitledBorder("Calendarizacion de recursos"));

        panel.add(new JLabel("Fecha:"));

        spnFecha = new JSpinner(new SpinnerDateModel());
        spnFecha.setEditor(new JSpinner.DateEditor(spnFecha, "dd/MM/yyyy"));

        panel.add(spnFecha);

        panel.add(new JLabel("Categoria:"));

        cmbCategoria = new JComboBox<>();

        panel.add(cmbCategoria);

        JButton btnConsultar = new JButton("Consultar");

        btnConsultar.addActionListener(e -> actualizarTabla());

        panel.add(btnConsultar);

        return panel;
    }

    private JScrollPane crearPanelTabla() {

        modeloTabla = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaCalendarizacion = new JTable(modeloTabla);
        tablaCalendarizacion.setRowHeight(40);
        tablaCalendarizacion.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane scroll = new JScrollPane(tablaCalendarizacion);
        scroll.setBorder(new TitledBorder("Disponibilidad de recursos"));

        return scroll;
    }

    private void cargarCategorias() {

        categoriasDisponibles = calendarizacionController.listarCategorias();

        cmbCategoria.removeAllItems();

        for (int i = 0; i < categoriasDisponibles.size(); i++) {

            Categoria categoria = categoriasDisponibles.get(i);

            cmbCategoria.addItem(categoria.getDescripcion());
        }
    }

    private void actualizarTabla() {

        if (categoriasDisponibles.isEmpty()
                || cmbCategoria.getSelectedIndex() == -1) {

            modeloTabla.setRowCount(0);
            modeloTabla.setColumnCount(0);

            return;
        }

        int indiceCategoria = cmbCategoria.getSelectedIndex();

        Categoria categoria = categoriasDisponibles.get(indiceCategoria);

        List<Recurso> recursos = calendarizacionController.listarRecursosPorCategoria(
                categoria.getId()
        );

        crearColumnas(recursos);
        cargarHoras(recursos);
    }

    private void crearColumnas(List<Recurso> recursos) {

        modeloTabla.setColumnCount(0);
        modeloTabla.setRowCount(0);

        modeloTabla.addColumn("Hora");

        for (int i = 0; i < recursos.size(); i++) {

            Recurso recurso = recursos.get(i);

            modeloTabla.addColumn(recurso.getId());
        }
    }

    private void cargarHoras(List<Recurso> recursos) {

        LocalDate fecha = obtenerFechaSeleccionada();

        for (int hora = 6; hora <= 22; hora++) {

            LocalTime horaActual = LocalTime.of(hora, 0);

            Object[] fila = new Object[recursos.size() + 1];

            fila[0] = String.format("%02d:00", hora);

            for (int i = 0; i < recursos.size(); i++) {

                Recurso recurso = recursos.get(i);

                Reserva reserva = calendarizacionController.buscarReserva(
                        recurso,
                        fecha,
                        horaActual
                );

                if (reserva == null) {

                    fila[i + 1] = "Libre";

                } else {

                    fila[i + 1] = obtenerTextoReserva(reserva);
                }
            }

            modeloTabla.addRow(fila);
        }

        ajustarColumnas();
    }

    private String obtenerTextoReserva(Reserva reserva) {

        String funcionario = "";

        if (reserva.getFuncionario() != null) {
            funcionario = reserva.getFuncionario().getId();
        }

        return reserva.getActividad() + " - " + funcionario;
    }

    private LocalDate obtenerFechaSeleccionada() {

        Date fecha = (Date) spnFecha.getValue();

        return fecha.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private void ajustarColumnas() {

        if (tablaCalendarizacion.getColumnCount() == 0) {
            return;
        }

        tablaCalendarizacion.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(70);

        for (int i = 1; i < tablaCalendarizacion.getColumnCount(); i++) {

            tablaCalendarizacion.getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(160);
        }
    }
}