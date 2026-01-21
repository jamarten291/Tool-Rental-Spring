package tool_rental.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "herramientas")
public class Herramienta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nombre;

    @NotBlank
    @Column(nullable = false)
    private String categoria;

    @Positive
    @Column(nullable = false, scale = 2)
    private BigDecimal precioDia;

    @Min(0)
    @Column(nullable = false)
    private Integer stock;

    @OneToMany(mappedBy = "herramienta", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Alquiler> alquileres = new HashSet<>();

    public Herramienta() {}

    public Herramienta(String nombre, String categoria, BigDecimal precioDia, Integer stock) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.precioDia = precioDia;
        this.stock = stock;
    }

    // getters y setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public BigDecimal getPrecioDia() { return precioDia; }
    public void setPrecioDia(BigDecimal precioDia) { this.precioDia = precioDia; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Set<Alquiler> getAlquileres() { return alquileres; }
    public void setAlquileres(Set<Alquiler> alquileres) { this.alquileres = alquileres; }

    public boolean disponible(Integer cantidad) {
        return this.stock != null && this.stock >= (cantidad == null ? 1 : cantidad);
    }
}