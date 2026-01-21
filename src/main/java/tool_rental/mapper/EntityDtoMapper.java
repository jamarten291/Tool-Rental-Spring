package tool_rental.mapper;

import tool_rental.dto.*;
import tool_rental.model.*;

import java.util.List;
import java.util.stream.Collectors;

public class EntityDtoMapper {

    // Cliente
    public static ClienteDTO toDto(Cliente c) {
        if (c == null) return null;
        ClienteDTO dto = new ClienteDTO();
        dto.setId(c.getId());
        dto.setNombre(c.getNombre());
        dto.setEmail(c.getEmail());
        dto.setTelefono(c.getTelefono());
        return dto;
    }

    public static Cliente toEntity(ClienteDTO dto) {
        if (dto == null) return null;
        Cliente c = new Cliente();
        c.setId(dto.getId());
        c.setNombre(dto.getNombre());
        c.setEmail(dto.getEmail());
        c.setTelefono(dto.getTelefono());
        return c;
    }

    // Herramienta
    public static HerramientaDTO toDto(Herramienta h) {
        if (h == null) return null;
        HerramientaDTO dto = new HerramientaDTO();
        dto.setId(h.getId());
        dto.setNombre(h.getNombre());
        dto.setCategoria(h.getCategoria());
        dto.setPrecioDia(h.getPrecioDia());
        dto.setStock(h.getStock());
        return dto;
    }

    public static Herramienta toEntity(HerramientaDTO dto) {
        if (dto == null) return null;
        Herramienta h = new Herramienta();
        h.setId(dto.getId());
        h.setNombre(dto.getNombre());
        h.setCategoria(dto.getCategoria());
        h.setPrecioDia(dto.getPrecioDia());
        h.setStock(dto.getStock());
        return h;
    }

    // Alquiler -> response
    public static Alquiler toEntityFromCreateDto(AlquilerCreateDTO dto) {
        if (dto == null) return null;
        Alquiler a = new Alquiler();

        // Crear referencias mínimas con id (los servicios validan/recargan)
        Cliente c = new Cliente();
        c.setId(dto.getClienteId());
        a.setCliente(c);

        Herramienta h = new Herramienta();
        h.setId(dto.getHerramientaId());
        a.setHerramienta(h);

        a.setFechaIni(dto.getFechaInicio());
        a.setFechaFin(dto.getFechaFin());
        return a;
    }

    public static AlquilerResponseDTO toDto(Alquiler a) {
        if (a == null) return null;
        AlquilerResponseDTO dto = new AlquilerResponseDTO();
        dto.setId(a.getId());
        if (a.getCliente() != null) dto.setClienteId(a.getCliente().getId());
        if (a.getHerramienta() != null) dto.setHerramientaId(a.getHerramienta().getId());
        dto.setEstado(a.getEstado() != null ? a.getEstado().name() : null);
        dto.setFechaInicio(a.getFechaIni());
        dto.setFechaFin(a.getFechaFin());
        dto.setMonto(a.getMontoDevuelto());
        dto.setMontoDevuelto(a.getMontoDevuelto());
        return dto;
    }

    public static List<ClienteDTO> toClienteDtoList(List<Cliente> list) {
        return list.stream().map(EntityDtoMapper::toDto).collect(Collectors.toList());
    }

    public static List<HerramientaDTO> toHerramientaDtoList(List<Herramienta> list) {
        return list.stream().map(EntityDtoMapper::toDto).collect(Collectors.toList());
    }

    public static List<AlquilerResponseDTO> toAlquilerResponseList(List<Alquiler> list) {
        return list.stream().map(EntityDtoMapper::toDto).collect(Collectors.toList());
    }
}