package controlador;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import entidades.Usuario;

public class LoginControler {

	private static final String USUARIOS_JSON_FILE = "usuarios.json";

	public boolean iniciarSesion(String email, String contraseña) {
		List<Usuario> usuarios = cargarUsuariosDesdeJson();
		for (Usuario usuario : usuarios) {
			if (usuario.getEmail().equals(email) && usuario.getPassword().equals(contraseña)) {
				return true; // Inicio de sesión exitoso
			}
		}
		return false; // Inicio de sesión fallido
	}

	private List<Usuario> cargarUsuariosDesdeJson() {
		try (BufferedReader br = new BufferedReader(new FileReader(USUARIOS_JSON_FILE))) {
			Gson gson = new Gson();
			Usuario[] usuariosArray = gson.fromJson(br, Usuario[].class);
			return usuariosArray != null ? List.of(usuariosArray) : new ArrayList<>();
		} catch (IOException e) {
			return new ArrayList<>();
		}
	}
}
