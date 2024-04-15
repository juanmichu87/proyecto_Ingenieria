package controlador;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import entidades.Usuario;

public class RegistroControler {

<<<<<<< Updated upstream
    private static final String USUARIOS_JSON_FILE = "Data/usuarios.json";
=======
    private static final String USUARIOS_JSON_FILE = "usuarios.json";
    private int nextId = 1;  // Inicia el ID desde 1
>>>>>>> Stashed changes
    private List<Usuario> usuarios;

    public RegistroControler() {
        this.usuarios = cargarUsuariosDesdeJson();
    }

    public void registrarUsuario(Usuario usuario) {
        if (!existeNombreUsuario(usuario.getNombre()) && !existeCorreoElectronico(usuario.getEmail())) {
            usuario.setId(nextId++);  // Asigna el ID actual y incrementa nextId
            usuarios.add(usuario);
            guardarUsuariosEnJson(usuarios);
            System.out.println("Usuario registrado con éxito.");
        } else {
            System.out.println("Error: El nombre de usuario o el correo electrónico ya están en uso.");
        }
    }
    
    private List<Usuario> cargarUsuariosDesdeJson() {
        File archivo = new File(USUARIOS_JSON_FILE);
        if (!archivo.exists()) {
            return new ArrayList<>();  // Devuelve una lista vacía si el archivo no existe
        }
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            Gson gson = new Gson();
            Usuario[] arrayUsuarios = gson.fromJson(br, Usuario[].class);
            List<Usuario> usuarios;
            if (arrayUsuarios != null) {
                usuarios = new ArrayList<>(Arrays.asList(arrayUsuarios));
            } else {
                usuarios = new ArrayList<>();
            }
            // Actualiza el nextId basándose en el ID más alto
            usuarios.forEach(usuario -> {
                if (usuario.getId() >= nextId) {
                    nextId = usuario.getId() + 1;
                }
            });
            return usuarios;
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public void guardarUsuariosEnJson(List<Usuario> usuarios) {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(USUARIOS_JSON_FILE))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonUsuarios = gson.toJson(usuarios);
            escritor.write(jsonUsuarios);
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
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
