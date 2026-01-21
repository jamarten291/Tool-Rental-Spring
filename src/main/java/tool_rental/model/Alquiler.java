package tool_rental.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "alquileres")
public class Alquiler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private LocalDate fechaIni;

    @NotNull
    @Column(nullable = false)
    private LocalDate fechaFin;

    @PositiveOrZero
    @Column(nullable = false, scale = 2)
    private BigDecimal fianza;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAlquiler estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "herramienta_id", nullable = false)
    private Herramienta herramienta;

    // Nuevo: multa aplicada al finalizar (por retraso)
    @Column(nullable = true, scale = 2)
    private BigDecimal multa;

    // Nuevo: monto retenido en caso de cancelación
    @Column(nullable = true, scale = 2)
    private BigDecimal montoRetenido;

    // Nuevo: monto devuelto al cliente (fianza - retenido - multa, según caso)
    @Column(nullable = true, scale = 2)
    private BigDecimal montoDevuelto;

    public Alquiler() {}

    public Alquiler(LocalDate fechaIni, LocalDate fechaFin, BigDecimal fianza, EstadoAlquiler estado, Cliente cliente, Herramienta herramienta) {
        this.fechaIni = fechaIni;
        this.fechaFin = fechaFin;
        this.fianza = fianza;
        this.estado = estado;
        this.cliente = cliente;
        this.herramienta = herramienta;
    }

    // getters y setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getFechaIni() { return fechaIni; }
    public void setFechaIni(LocalDate fechaIni) { this.fechaIni = fechaIni; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public BigDecimal getFianza() { return fianza; }
    public void setFianza(BigDecimal fianza) { this.fianza = fianza; }

    public EstadoAlquiler getEstado() { return estado; }
    public void setEstado(EstadoAlquiler estado) { this.estado = estado; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Herramienta getHerramienta() { return herramienta; }
    public void setHerramienta(Herramienta herramienta) { this.herramienta = herramienta; }

    public BigDecimal getMulta() { return multa; }
    public void setMulta(BigDecimal multa) { this.multa = multa; }

    public BigDecimal getMontoRetenido() { return montoRetenido; }
    public void setMontoRetenido(BigDecimal montoRetenido) { this.montoRetenido = montoRetenido; }

    public BigDecimal getMontoDevuelto() { return montoDevuelto; }
    public void setMontoDevuelto(BigDecimal montoDevuelto) { this.montoDevuelto = montoDevuelto; }
}