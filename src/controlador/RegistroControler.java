package controlador;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import entidades.Usuario;

public class RegistroControler {

	private static final String USUARIOS_JSON_FILE = "usuarios.json";
	private List<Usuario> usuarios;

	public RegistroControler() {
		this.usuarios = cargarUsuariosDesdeJson();
	}

	public void registrarUsuario(Usuario usuario) {
		if (!existeNombreUsuario(usuario.getNombre()) && !existeCorreoElectronico(usuario.getEmail())) {
			usuarios.add(usuario);
			guardarUsuariosEnJson(usuarios);
			System.out.println("Usuario registrado con éxito.");
		} else {
			System.out.println("Error: El nombre de usuario o el correo electrónico ya están en uso.");
		}
	}

	private List<Usuario> cargarUsuariosDesdeJson() {
		try (BufferedReader br = new BufferedReader(new FileReader(USUARIOS_JSON_FILE))) {
			Gson gson = new Gson();
			Usuario[] usuariosArray = gson.fromJson(br, Usuario[].class);
			return usuariosArray != null ? new ArrayList<>(List.of(usuariosArray)) : new ArrayList<>();
		} catch (IOException e) {
			return new ArrayList<>();
		}
	}

	public void guardarUsuariosEnJson(List<Usuario> usuarios) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(USUARIOS_JSON_FILE))) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String usuariosJson = gson.toJson(usuarios);
			writer.write(usuariosJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean existeNombreUsuario(String nombreUsuario) {
		for (Usuario usuario : usuarios) {
			if (usuario.getNombre().equals(nombreUsuario)) {
				return true; // El nombre de usuario ya existe en la base de datos
			}
		}
		return false; // El nombre de usuario no existe en la base de datos
	}

	public boolean existeCorreoElectronico(String correoElectronico) {
		for (Usuario usuario : usuarios) {
			if (usuario.getEmail().equals(correoElectronico)) {
				return true; // El correo electrónico ya existe en la base de datos
			}
		}
		return false; // El correo electrónico no existe en la base de datos
	}
}
