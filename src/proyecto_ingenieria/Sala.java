package proyecto_ingenieria;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Sala {
	private int id;
	private boolean reservada;
	private int idProfesorAsociado; // ID del profesor asociado a la sala
	private String turno;
	private JsonArray historial; // Lista de registros de historial
	private JsonArray usuariosEnSala; // Lista de usuarios presentes en la sala

	public Sala(int id, boolean reservada, int idProfesorAsociado, String turno, JsonArray historial) {
		this.id = id;
		this.reservada = reservada;
		this.idProfesorAsociado = idProfesorAsociado;
		this.turno = turno;
		this.historial = historial;
		this.usuariosEnSala = new JsonArray(); // Inicializamos la lista de usuarios en la sala
	}

	// Getters y setters para los campos de la sala

	public JsonArray getUsuariosEnSala() {
		return usuariosEnSala;
	}

	public void setUsuariosEnSala(JsonArray usuariosEnSala) {
		this.usuariosEnSala = usuariosEnSala;
	}

	public void agregarUsuarioEnSala(JsonObject usuario) {
		this.usuariosEnSala.add(usuario);
	}

	public void quitarUsuarioEnSala(JsonObject usuario) {
		this.usuariosEnSala.remove(usuario);
	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json.addProperty("id", id);
		json.addProperty("reservada", reservada);
		json.addProperty("idProfesorAsociado", idProfesorAsociado);
		json.addProperty("turno", turno);
		json.add("historial", historial);
		json.add("usuariosEnSala", usuariosEnSala); // Incluimos la lista de usuarios en la sala en el JSON
		return json;
	}
}
