package entidades;

import com.google.gson.JsonArray; // Importa la clase JsonArray para trabajar con arreglos JSON.

/**
 * Clase Usuario que representa a un usuario con sus detalles y funcionalidades.
 */
public class Usuario {
	private int id; // ID del usuario.
	private String nombre; // Nombre del usuario.
	private String email; // Email del usuario.
	private String password; // Contraseña del usuario.
	private String tipo; // Tipo de usuario (e.g., administrador, profesor, usuario).
	private JsonArray historialAsistencia; // Historial de asistencia del usuario.

	/**
	 * Constructor de la clase Usuario.
	 *
	 * @param id                  ID del usuario.
	 * @param nombre              Nombre del usuario.
	 * @param email               Email del usuario.
	 * @param password            Contraseña del usuario.
	 * @param tipo                Tipo de usuario.
	 * @param historialAsistencia Historial de asistencia del usuario.
	 */
	public Usuario(int id, String nombre, String email, String password, String tipo, JsonArray historialAsistencia) {
		this.id = id; // Asigna el ID del usuario.
		this.nombre = nombre; // Asigna el nombre del usuario.
		this.email = email; // Asigna el email del usuario.
		this.password = password; // Asigna la contraseña del usuario.
		this.tipo = tipo; // Asigna el tipo de usuario.
		this.historialAsistencia = historialAsistencia; // Asigna el historial de asistencia del usuario.
	}

	// Getters

	/**
	 * Obtiene el ID del usuario.
	 *
	 * @return El ID del usuario.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Obtiene el nombre del usuario.
	 *
	 * @return El nombre del usuario.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Obtiene el email del usuario.
	 *
	 * @return El email del usuario.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Obtiene la contraseña del usuario.
	 *
	 * @return La contraseña del usuario.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Obtiene el tipo de usuario.
	 *
	 * @return El tipo de usuario.
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * Obtiene el historial de asistencia del usuario.
	 *
	 * @return El historial de asistencia del usuario.
	 */
	public JsonArray getHistorialAsistencia() {
		return historialAsistencia;
	}

	// Setters

	/**
	 * Establece el ID del usuario.
	 *
	 * @param id El nuevo ID del usuario.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Establece el nombre del usuario.
	 *
	 * @param nombre El nuevo nombre del usuario.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Establece el email del usuario.
	 *
	 * @param email El nuevo email del usuario.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Establece la contraseña del usuario.
	 *
	 * @param password La nueva contraseña del usuario.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Establece el tipo de usuario.
	 *
	 * @param tipo El nuevo tipo de usuario.
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * Obtiene el historial de asistencia del usuario en formato de array de
	 * enteros.
	 *
	 * @return Un array de enteros que representa el historial de asistencia.
	 */
	public int[] obtenerHistorialAsistencia() {
		int[] historial = new int[historialAsistencia.size()];
		for (int i = 0; i < historialAsistencia.size(); i++) { // Itera sobre el historial de asistencia.
			historial[i] = historialAsistencia.get(i).getAsInt(); // Asigna el ID de la sala al historial.
		}
		return historial; // Devuelve el historial de asistencia.
	}
}
