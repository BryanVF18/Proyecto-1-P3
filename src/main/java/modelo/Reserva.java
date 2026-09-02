package modelo;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "reserva")
@XmlAccessorType(XmlAccessType.FIELD)

public class Reserva {

    private String id;
    private String actividad;
    private Funcionario funcionario;

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate fecha;

    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime horaInicio;

    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime horaFin;

    @XmlElement(name = "recurso")
    private List<Recurso> recursosAsignados = new ArrayList<>();

    private String estado; // Activa o Cancelada

    public Reserva() {
        // vacio
    }

    public Reserva(String id, String actividad, Funcionario funcionario, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        this.id = id;
        this.actividad = actividad;
        this.funcionario = funcionario;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = "ACTIVA";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public List<Recurso> getRecursosAsignados() {
        return recursosAsignados;
    }

    public void setRecursosAsignados(List<Recurso> recursosAsignados) {
        this.recursosAsignados = recursosAsignados;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean seSuperponeCon(LocalDate otraFecha, LocalTime otraHoraInicio, LocalTime otraHoraFin) {
        if (!this.fecha.equals(otraFecha) || !"ACTIVA".equals(this.estado)) {
            return false;
        }
        return this.horaInicio.isBefore(otraHoraFin) && otraHoraInicio.isBefore(this.horaFin);
    }
}
