package vista;

import controlador.EstadisticaController;
import reportes.GeneradorReportePDF;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PanelEstadisticas extends JPanel {

    private JSpinner spnFechaInicio;
    private JSpinner spnFechaFin;

    private JTable tablaRecursos;
    private JTable tablaActividades;

    private DefaultTableModel modeloRecursos;
    private DefaultTableModel modeloActividades;

    private GraficoBarras graficoRecursos;
    private GraficoBarras graficoActividades;

    private final EstadisticaController estadisticaController;

    public PanelEstadisticas() {
        estadisticaController = new EstadisticaController();

        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearContenido(), BorderLayout.CENTER);
    }

    private JPanel crearEncabezado() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Estadisticas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(30, 70, 120));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDescripcion = new JLabel(
                "Consulta informacion sobre los recursos y actividades del sistema"
        );
        lblDescripcion.setFont(new Font("Arial", Font.PLAIN, 14));
        lblDescripcion.setForeground(Color.DARK_GRAY);
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblDescripcion);
        panel.add(Box.createVerticalStrut(10));
        panel.add(crearPanelFechas());

        return panel;
    }

    private JPanel crearPanelFechas() {
        JPanel panel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 10)
        );

        panel.setBackground(Color.WHITE);

        TitledBorder borde = new TitledBorder(
                "Rango de fechas"
        );

        borde.setTitleColor(new Color(30, 70, 120));
        panel.setBorder(borde);

        panel.add(new JLabel("Desde:"));

        spnFechaInicio = new JSpinner(
                new SpinnerDateModel()
        );

        spnFechaInicio.setEditor(
                new JSpinner.DateEditor(
                        spnFechaInicio,
                        "dd/MM/yyyy"
                )
        );

        spnFechaInicio.setPreferredSize(
                new Dimension(130, 30)
        );

        panel.add(spnFechaInicio);

        panel.add(new JLabel("Hasta:"));

        spnFechaFin = new JSpinner(
                new SpinnerDateModel()
        );

        spnFechaFin.setEditor(
                new JSpinner.DateEditor(
                        spnFechaFin,
                        "dd/MM/yyyy"
                )
        );

        spnFechaFin.setPreferredSize(
                new Dimension(130, 30)
        );

        panel.add(spnFechaFin);

        JButton btnConsultar = new JButton("Consultar");
        btnConsultar.setFocusPainted(false);
        btnConsultar.setBackground(
                new Color(70, 130, 180)
        );
        btnConsultar.setForeground(Color.WHITE);

        btnConsultar.addActionListener(
                e -> consultarEstadisticas()
        );

        panel.add(btnConsultar);

        JButton btnImprimir = new JButton(
                "Imprimir reporte"
        );

        btnImprimir.setFocusPainted(false);
        btnImprimir.setBackground(
                new Color(90, 110, 130)
        );
        btnImprimir.setForeground(Color.WHITE);

        btnImprimir.addActionListener(
                e -> imprimirReporte()
        );

        panel.add(btnImprimir);

        return panel;
    }

    private JPanel crearContenido() {
        JPanel panel = new JPanel(
                new GridLayout(2, 1, 10, 10)
        );

        panel.setBackground(Color.WHITE);

        panel.add(crearPanelRecursos());
        panel.add(crearPanelActividades());

        return panel;
    }

    private JPanel crearPanelRecursos() {
        JPanel panel = new JPanel(
                new BorderLayout(10, 10)
        );

        panel.setBackground(Color.WHITE);

        TitledBorder borde = new TitledBorder(
                "Recursos reservados por categoria"
        );

        borde.setTitleColor(
                new Color(30, 70, 120)
        );

        panel.setBorder(borde);

        modeloRecursos = new DefaultTableModel(
                new Object[]{"Categoria", "Cantidad"},
                0
        ) {
            @Override
            public boolean isCellEditable(
                    int fila,
                    int columna
            ) {
                return false;
            }
        };

        tablaRecursos = new JTable(modeloRecursos);
        configurarTabla(tablaRecursos);

        JScrollPane scroll = new JScrollPane(
                tablaRecursos
        );

        scroll.setPreferredSize(
                new Dimension(300, 200)
        );

        graficoRecursos = new GraficoBarras();

        JPanel contenido = new JPanel(
                new GridLayout(1, 2, 10, 10)
        );

        contenido.setBackground(Color.WHITE);

        contenido.add(scroll);
        contenido.add(graficoRecursos);

        panel.add(contenido, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelActividades() {
        JPanel panel = new JPanel(
                new BorderLayout(10, 10)
        );

        panel.setBackground(Color.WHITE);

        TitledBorder borde = new TitledBorder(
                "Actividades por semana"
        );

        borde.setTitleColor(
                new Color(30, 70, 120)
        );

        panel.setBorder(borde);

        modeloActividades = new DefaultTableModel(
                new Object[]{"Semana", "Cantidad"},
                0
        ) {
            @Override
            public boolean isCellEditable(
                    int fila,
                    int columna
            ) {
                return false;
            }
        };

        tablaActividades = new JTable(
                modeloActividades
        );

        configurarTabla(tablaActividades);

        JScrollPane scroll = new JScrollPane(
                tablaActividades
        );

        scroll.setPreferredSize(
                new Dimension(300, 200)
        );

        graficoActividades = new GraficoBarras();

        JPanel contenido = new JPanel(
                new GridLayout(1, 2, 10, 10)
        );

        contenido.setBackground(Color.WHITE);

        contenido.add(scroll);
        contenido.add(graficoActividades);

        panel.add(contenido, BorderLayout.CENTER);

        return panel;
    }

    private void configurarTabla(JTable tabla) {
        tabla.setRowHeight(25);
        tabla.setBackground(Color.WHITE);
        tabla.setGridColor(
                new Color(220, 220, 220)
        );

        tabla.getTableHeader().setBackground(
                new Color(45, 85, 130)
        );

        tabla.getTableHeader().setForeground(
                Color.WHITE
        );

        tabla.getTableHeader().setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );
    }

    private void consultarEstadisticas() {
        LocalDate fechaInicio =
                obtenerFecha(spnFechaInicio);

        LocalDate fechaFin =
                obtenerFecha(spnFechaFin);

        if (fechaInicio.isAfter(fechaFin)) {
            JOptionPane.showMessageDialog(
                    this,
                    "La fecha inicial no puede ser mayor que la fecha final.",
                    "Fechas incorrectas",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        cargarRecursos(
                fechaInicio,
                fechaFin
        );

        cargarActividades(
                fechaInicio,
                fechaFin
        );
    }

    private void cargarRecursos(
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {
        modeloRecursos.setRowCount(0);

        List<String> categorias =
                estadisticaController
                        .obtenerCategoriasReservadas(
                                fechaInicio,
                                fechaFin
                        );

        List<String> nombres = new ArrayList<>();
        List<Integer> cantidades = new ArrayList<>();

        for (int i = 0; i < categorias.size(); i++) {
            String idCategoria = categorias.get(i);

            int cantidad =
                    estadisticaController
                            .contarRecursosPorCategoria(
                                    idCategoria,
                                    fechaInicio,
                                    fechaFin
                            );

            String descripcion =
                    estadisticaController
                            .obtenerDescripcionCategoria(
                                    idCategoria
                            );

            modeloRecursos.addRow(
                    new Object[]{
                            descripcion,
                            cantidad
                    }
            );

            nombres.add(descripcion);
            cantidades.add(cantidad);
        }

        graficoRecursos.setDatos(
                nombres,
                cantidades
        );
    }

    private void cargarActividades(
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {
        modeloActividades.setRowCount(0);

        List<String> semanas =
                estadisticaController.obtenerSemanas(
                        fechaInicio,
                        fechaFin
                );

        List<String> nombres = new ArrayList<>();
        List<Integer> cantidades = new ArrayList<>();

        for (int i = 0; i < semanas.size(); i++) {
            String semana = semanas.get(i);

            int cantidad =
                    estadisticaController
                            .contarActividadesPorSemana(
                                    semana,
                                    fechaInicio,
                                    fechaFin
                            );

            modeloActividades.addRow(
                    new Object[]{
                            semana,
                            cantidad
                    }
            );

            nombres.add(semana);
            cantidades.add(cantidad);
        }

        graficoActividades.setDatos(
                nombres,
                cantidades
        );
    }

    private LocalDate obtenerFecha(
            JSpinner spinner
    ) {
        Date fecha = (Date) spinner.getValue();

        return fecha.toInstant()
                .atZone(
                        ZoneId.systemDefault()
                )
                .toLocalDate();
    }

    private void imprimirReporte() {
        if (modeloRecursos.getRowCount() == 0
                && modeloActividades.getRowCount() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Primero debe consultar las estadisticas.",
                    "Sin datos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        JFileChooser selector = new JFileChooser();

        selector.setSelectedFile(
                new File("estadisticas.pdf")
        );

        int opcion = selector.showSaveDialog(this);

        if (opcion != JFileChooser.APPROVE_OPTION) {
            return;
        }

        String ruta =
                selector.getSelectedFile()
                        .getAbsolutePath();

        if (!ruta.toLowerCase().endsWith(".pdf")) {
            ruta = ruta + ".pdf";
        }

        String[] columnas = {
                "Tipo",
                "Descripcion",
                "Cantidad"
        };

        List<String[]> filas = new ArrayList<>();

        for (int i = 0;
             i < modeloRecursos.getRowCount();
             i++) {

            String[] fila = {
                    "Recurso",
                    String.valueOf(
                            modeloRecursos.getValueAt(
                                    i,
                                    0
                            )
                    ),
                    String.valueOf(
                            modeloRecursos.getValueAt(
                                    i,
                                    1
                            )
                    )
            };

            filas.add(fila);
        }

        for (int i = 0;
             i < modeloActividades.getRowCount();
             i++) {

            String[] fila = {
                    "Actividad",
                    String.valueOf(
                            modeloActividades.getValueAt(
                                    i,
                                    0
                            )
                    ),
                    String.valueOf(
                            modeloActividades.getValueAt(
                                    i,
                                    1
                            )
                    )
            };

            filas.add(fila);
        }

        try {
            GeneradorReportePDF.generar(
                    ruta,
                    "Estadisticas del Sistema",
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

    private class GraficoBarras extends JPanel {

        private List<String> nombres;
        private List<Integer> cantidades;

        public GraficoBarras() {
            nombres = new ArrayList<>();
            cantidades = new ArrayList<>();

            setBackground(Color.WHITE);

            setBorder(
                    BorderFactory.createLineBorder(
                            new Color(220, 220, 220)
                    )
            );
        }

        public void setDatos(
                List<String> nombres,
                List<Integer> cantidades
        ) {
            this.nombres = nombres;
            this.cantidades = cantidades;

            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (cantidades.isEmpty()) {
                g.setColor(Color.GRAY);

                g.drawString(
                        "No hay datos para mostrar",
                        20,
                        30
                );

                return;
            }

            int cantidadMayor = obtenerCantidadMayor();

            if (cantidadMayor == 0) {
                return;
            }

            int anchoDisponible =
                    getWidth() - 160;

            int alturaBarra = 22;
            int espacio = 10;
            int posicionY = 30;

            for (int i = 0;
                 i < cantidades.size();
                 i++) {

                int cantidad = cantidades.get(i);

                int anchoBarra =
                        cantidad * anchoDisponible
                                / cantidadMayor;

                g.setColor(
                        new Color(90, 150, 210)
                );

                g.fillRect(
                        120,
                        posicionY,
                        anchoBarra,
                        alturaBarra
                );

                g.setColor(Color.DARK_GRAY);

                g.drawString(
                        nombres.get(i),
                        10,
                        posicionY + 16
                );

                g.drawString(
                        String.valueOf(cantidad),
                        125 + anchoBarra,
                        posicionY + 16
                );

                posicionY +=
                        alturaBarra + espacio;
            }
        }

        private int obtenerCantidadMayor() {
            int mayor = 0;

            for (int i = 0;
                 i < cantidades.size();
                 i++) {

                if (cantidades.get(i) > mayor) {
                    mayor = cantidades.get(i);
                }
            }

            return mayor;
        }
    }
}