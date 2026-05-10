package com.example.demo.controller;

import com.example.demo.model.service.Interface.IInformacionEnvioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/envios")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class InformacionEnvioController {

    private final IInformacionEnvioService service;

    @GetMapping("/usuario/{idUsuario}/activas")
    public ResponseEntity<List<EnvioResponse>> obtenerActivasPorUsuario(@PathVariable Long idUsuario) {
        var envios = service.obtenerActivasPorUsuario(idUsuario);
        List<EnvioResponse> response = envios.stream()
                .map(e -> new EnvioResponse(
                        e.getIdInfoEnvio(),
                        e.getNombreDestinatario(),
                        e.getDireccion(),
                        e.getCiudad(),
                        e.getDistrito(),
                        e.getTelefonoContacto(),
                        e.getEsPrincipal() != null ? e.getEsPrincipal() : false
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    public record EnvioResponse(
            Long idInfoEnvio,
            String nombreDestinatario,
            String direccion,
            String ciudad,
            String distrito,
            String telefono,
            Boolean esPrincipal
    ) {}
}