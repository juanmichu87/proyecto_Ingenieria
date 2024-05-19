package entidades;

import com.google.gson.JsonArray; // Importa la clase JsonArray para trabajar con arreglos JSON.
import com.google.gson.JsonObject; // Importa la clase JsonObject para trabajar con objetos JSON.

/**
 * Clase Sala que representa una sala con sus detalles y funcionalidades.
 */
public class Sala {
	private int id; // ID de la sala.
	private boolean reservada; // Indica si la sala está reservada.
	private int idProfesorAsociado; // ID del profesor asociado a la sala.
	private String turno; // Turno de la sala (mañana, tarde, etc.).
	private JsonArray historial; // Lista de registros de historial.
	private JsonArray usuariosEnSala; // Lista de usuarios presentes en la sala.

	/**
	 * Constructor de la clase Sala.
	 *
	 * @param id                 ID de la sala.
	 * @param reservada          Estado de reserva de la sala.
	 * @param idProfesorAsociado ID del profesor asociado a la sala.
	 * @param turno              Turno de la sala.
	 * @param historial          Historial de reservas de la sala.
	 */
	public Sala(int id, boolean reservada, int idProfesorAsociado, String turno, JsonArray historial) {
		this.id = id; // Asigna el ID de la sala.
		this.reservada = reservada; // Asigna el estado de reserva de la sala.
		this.idProfesorAsociado = idProfesorAsociado; // Asigna el ID del profesor asociado.
		this.turno = turno; // Asigna el turno de la sala.
		this.historial = historial; // Asigna el historial de reservas de la sala.
		this.usuariosEnSala = new JsonArray(); // Inicializa la lista de usuarios en la sala.
	}

	/**
	 * Obtiene la lista de usuarios en la sala.
	 *
	 * @return La lista de usuarios en la sala.
	 */
	public JsonArray getUsuariosEnSala() {
		return usuariosEnSala;
	}

	/**
	 * Establece la lista de usuarios en la sala.
	 *
	 * @param usuariosEnSala La nueva lista de usuarios en la sala.
	 */
	public void setUsuariosEnSala(JsonArray usuariosEnSala) {
		this.usuariosEnSala = usuariosEnSala;
	}

	/**
	 * Obtiene el ID de la sala.
	 *
	 * @return El ID de la sala.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Establece el ID de la sala.
	 *
	 * @param id El nuevo ID de la sala.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Verifica si la sala está reservada.
	 *
	 * @return true si la sala está reservada, false en caso contrario.
	 */
	public boolean isReservada() {
		return reservada;
	}

	/**
	 * Establece el estado de reserva de la sala.
	 *
	 * @param reservada El nuevo estado de reserva de la sala.
	 */
	public void setReservada(boolean reservada) {
		this.reservada = reservada;
	}

	/**
	 * Obtiene el ID del profesor asociado a la sala.
	 *
	 * @return El ID del profesor asociado a la sala.
	 */
	public int getIdProfesorAsociado() {
		return idProfesorAsociado;
	}

	/**
	 * Establece el ID del profesor asociado a la sala.
	 *
	 * @param idProfesorAsociado El nuevo ID del profesor asociado a la sala.
	 */
	public void setIdProfesorAsociado(int idProfesorAsociado) {
		this.idProfesorAsociado = idProfesorAsociado;
	}

	/**
	 * Obtiene el turno de la sala.
	 *
	 * @return El turno de la sala.
	 */
	public String getTurno() {
		return turno;
	}

	/**
	 * Establece el turno de la sala.
	 *
	 * @param turno El nuevo turno de la sala.
	 */
	public void setTurno(String turno) {
		this.turno = turno;
	}

	/**
	 * Obtiene el historial de reservas de la sala.
	 *
	 * @return El historial de reservas de la sala.
	 */
	public JsonArray getHistorial() {
		return historial;
	}

	/**
	 * Establece el historial de reservas de la sala.
	 *
	 * @param historial El nuevo historial de reservas de la sala.
	 */
	public void setHistorial(JsonArray historial) {
		this.historial = historial;
	}

	/**
	 * Agrega un usuario a la sala.
	 *
	 * @param usuario El usuario a agregar.
	 */
	public void agregarUsuarioEnSala(JsonObject usuario) {
		this.usuariosEnSala.add(usuario);
	}

	/**
	 * Quita un usuario de la sala.
	 *
	 * @param usuario El usuario a quitar.
	 */
	public void quitarUsuarioEnSala(JsonObject usuario) {
		this.usuariosEnSala.remove(usuario);
	}

	/**
	 * Convierte el objeto Sala a un objeto JSON.
	 *
	 * @return El objeto JSON que representa la sala.
	 */
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json.addProperty("id", id); // Agrega el ID de la sala al JSON.
		json.addProperty("reservada", reservada); // Agrega el estado de reserva al JSON.
		json.addProperty("idProfesorAsociado", idProfesorAsociado); // Agrega el ID del profesor asociado al JSON.
		json.addProperty("turno", turno); // Agrega el turno de la sala al JSON.
		json.add("historial", historial); // Agrega el historial de reservas al JSON.
		json.add("usuariosEnSala", usuariosEnSala); // Agrega la lista de usuarios en la sala al JSON.
		return json;
	}
}
