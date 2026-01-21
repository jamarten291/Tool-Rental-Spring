package tool_rental.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class HerramientaDTO {
    private Long id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String categoria;

    @Positive
    private BigDecimal precioDia;

    @Min(0)
    private Integer stock;

    public HerramientaDTO() {}

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
}