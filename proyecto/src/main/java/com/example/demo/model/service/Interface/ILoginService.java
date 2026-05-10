package com.example.demo.model.service.Interface;

import com.example.demo.model.entidad.Usuario;

public interface ILoginService {

    Usuario buscarPorUsernameOEmail(String usernameOEmail);

    Usuario registrarInicioSesion(String usernameOEmail);
}
