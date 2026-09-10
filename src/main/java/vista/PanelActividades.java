package vista;

import controlador.ActividadController;
import modelo.Reserva;
import reportes.GeneradorReportePDF;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PanelActividades extends JPanel {

    private JSpinner spnFecha;
    private JTable tablaActividades;
    private DefaultTableModel modeloTabla;

    private final ActividadController actividadController;

    private final Color[] coloresActividades = {new Color(204, 229, 255), new Color(204, 255, 229),
            new Color(255, 230, 204), new Color(230, 204, 255), new Color(255, 204, 204),
            new Color(204, 255, 255), new Color(255, 255, 204), new Color(230, 230, 250)
    };

    public PanelActividades() {
        actividadController = new ActividadController();

        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(crearEncabezado(), BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new BorderLayout(10, 10));
        panelCentro.setBackground(Color.WHITE);

        panelCentro.add(crearPanelFiltros(), BorderLayout.NORTH);
        panelCentro.add(crearPanelTabla(), BorderLayout.CENTER);

        add(panelCentro, BorderLayout.CENTER);

        actualizarTabla();
    }

    private JPanel crearEncabezado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Programacion de Actividades");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(30, 70, 120));

        JLabel lblDescripcion = new JLabel(
                "Consulta las actividades programadas para la semana seleccionada"
        );
        lblDescripcion.setFont(new Font("Arial", Font.PLAIN, 14));
        lblDescripcion.setForeground(Color.DARK_GRAY);

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setBackground(Color.WHITE);

        textos.add(lblTitulo);
        textos.add(Box.createVerticalStrut(5));
        textos.add(lblDescripcion);

        panel.add(textos, BorderLayout.WEST);

        return panel;
    }

    private JPanel crearPanelFiltros() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(Color.WHITE);

        TitledBorder borde = new TitledBorder("Seleccion de semana");
        borde.setTitleColor(new Color(30, 70, 120));
        panel.setBorder(borde);

        JLabel lblFecha = new JLabel("Fecha de referencia:");
        lblFecha.setFont(new Font("Arial", Font.BOLD, 13));

        panel.add(lblFecha);

        spnFecha = new JSpinner(new SpinnerDateModel());
        spnFecha.setEditor(new JSpinner.DateEditor(spnFecha, "dd/MM/yyyy"));
        spnFecha.setPreferredSize(new Dimension(130, 30));

        panel.add(spnFecha);

        JButton btnConsultar = new JButton("Consultar");
        btnConsultar.setFocusPainted(false);
        btnConsultar.setBackground(new Color(70, 130, 180));
        btnConsultar.setForeground(Color.WHITE);
        btnConsultar.setPreferredSize(new Dimension(110, 32));
        btnConsultar.addActionListener(e -> actualizarTabla());

        panel.add(btnConsultar);

        JButton btnImprimir = new JButton("Imprimir reporte");
        btnImprimir.setFocusPainted(false);
        btnImprimir.setBackground(new Color(90, 110, 130));
        btnImprimir.setForeground(Color.WHITE);
        btnImprimir.setPreferredSize(new Dimension(150, 32));
        btnImprimir.addActionListener(e -> imprimirReporte());

        panel.add(btnImprimir);

        return panel;
    }

    private JScrollPane crearPanelTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaActividades = new JTable(modeloTabla);

        tablaActividades.setRowHeight(45);
        tablaActividades.setBackground(Color.WHITE);
        tablaActividades.setGridColor(new Color(210, 210, 210));
        tablaActividades.setShowGrid(true);
        tablaActividades.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaActividades.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tablaActividades.getTableHeader().setBackground(
                new Color(45, 85, 130)
        );

        tablaActividades.getTableHeader().setForeground(Color.WHITE);

        tablaActividades.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        tablaActividades.setDefaultRenderer(
                Object.class,
                new RenderizadorActividades()
        );

        JScrollPane scroll = new JScrollPane(tablaActividades);

        scroll.setBackground(Color.WHITE);
        scroll.getViewport().setBackground(Color.WHITE);

        TitledBorder borde = new TitledBorder("Actividades de la semana");
        borde.setTitleColor(new Color(30, 70, 120));

        scroll.setBorder(borde);

        return scroll;
    }

    private void actualizarTabla() {
        LocalDate fechaReferencia = obtenerFechaSeleccionada();
        LocalDate lunes = obtenerLunes(fechaReferencia);

        crearColumnas(lunes);
        cargarHoras(lunes);
    }

    private LocalDate obtenerLunes(LocalDate fecha) {
        while (fecha.getDayOfWeek() != DayOfWeek.MONDAY) {
            fecha = fecha.minusDays(1);
        }

        return fecha;
    }

    private void crearColumnas(LocalDate lunes) {
        modeloTabla.setColumnCount(0);
        modeloTabla.setRowCount(0);

        modeloTabla.addColumn("Hora");

        String[] nombresDias = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};

        for (int i = 0; i < 7; i++) {
            LocalDate fecha = lunes.plusDays(i);

            String fechaTexto = String.format("%02d/%02d", fecha.getDayOfMonth(), fecha.getMonthValue());

            modeloTabla.addColumn(nombresDias[i] + " " + fechaTexto);
        }
    }

    private void cargarHoras(LocalDate lunes) {
        for (int hora = 6; hora <= 22; hora++) {
            LocalTime horaActual = LocalTime.of(hora, 0);

            Object[] fila = new Object[8];

            fila[0] = String.format("%02d:00", hora);

            for (int dia = 0; dia < 7; dia++) {
                LocalDate fechaActual = lunes.plusDays(dia);

                Reserva reserva = actividadController.buscarActividad(
                        fechaActual,
                        horaActual
                );

                if (reserva == null) {
                    fila[dia + 1] = "";
                } else {
                    fila[dia + 1] = obtenerTextoActividad(reserva);
                }
            }

            modeloTabla.addRow(fila);
        }

        ajustarColumnas();
    }

    private String obtenerTextoActividad(Reserva reserva) {
        String funcionario = "";

        if (reserva.getFuncionario() != null) {
            funcionario = reserva.getFuncionario().getNombre();
        }

        return reserva.getActividad() + " - " + funcionario;
    }

    private LocalDate obtenerFechaSeleccionada() {
        Date fecha = (Date) spnFecha.getValue();

        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();}

    private void ajustarColumnas() {
        if (tablaActividades.getColumnCount() == 0) {
            return;
        }

        tablaActividades.getColumnModel().getColumn(0).setPreferredWidth(80);

        for (int i = 1; i < tablaActividades.getColumnCount(); i++) {
            tablaActividades.getColumnModel().getColumn(i).setPreferredWidth(180);
        }
    }

    private Color obtenerColorActividad(String texto) {
        int numero = Math.abs(texto.hashCode());
        int posicion = numero % coloresActividades.length;

        return coloresActividades[posicion];
    }

    private class RenderizadorActividades extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {

            Component componente = super.getTableCellRendererComponent(table, value, isSelected,
                    hasFocus, row, column);

            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(new Font("Arial", Font.PLAIN, 12));

            if (column == 0) {
                componente.setBackground(new Color(240, 243, 247));
                componente.setForeground(new Color(50, 50, 50));
                setFont(new Font("Arial", Font.BOLD, 12));

                return componente;
            }

            String texto = "";

            if (value != null) {
                texto = value.toString();
            }

            if (texto.isEmpty()) {
                componente.setBackground(Color.WHITE);
                componente.setForeground(Color.DARK_GRAY);
            } else {
                componente.setBackground(
                        obtenerColorActividad(texto)
                );

                componente.setForeground(
                        new Color(40, 40, 40)
                );

                setFont(
                        new Font("Arial", Font.BOLD, 12)
                );
            }

            if (isSelected) {
                componente.setBackground(
                        new Color(180, 210, 240)
                );
            }

            return componente;
        }
    }

    private void imprimirReporte() {
        if (modeloTabla.getColumnCount() == 0
                || modeloTabla.getRowCount() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "No hay datos para generar el reporte.",
                    "Sin datos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        JFileChooser selector = new JFileChooser();

        selector.setSelectedFile(
                new File("actividades.pdf")
        );

        int opcion = selector.showSaveDialog(this);

        if (opcion != JFileChooser.APPROVE_OPTION) {
            return;
        }

        String ruta = selector
                .getSelectedFile()
                .getAbsolutePath();

        if (!ruta.toLowerCase().endsWith(".pdf")) {
            ruta = ruta + ".pdf";
        }

        String[] columnas =
                new String[modeloTabla.getColumnCount()];

        for (int i = 0; i < modeloTabla.getColumnCount(); i++) {
            columnas[i] =
                    modeloTabla.getColumnName(i);
        }

        List<String[]> filas = new ArrayList<>();

        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            String[] fila =
                    new String[modeloTabla.getColumnCount()];

            for (int j = 0; j < modeloTabla.getColumnCount(); j++) {
                fila[j] = String.valueOf(
                        modeloTabla.getValueAt(i, j)
                );
            }

            filas.add(fila);
        }

        try {
            GeneradorReportePDF.generar(
                    ruta,
                    "Programacion de Actividades",
                    columnas,
                    filas
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Reporte generado correctamente."
            );

        } catch (
                IOException |
                com.lowagie.text.DocumentException e
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo generar el reporte: "
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}