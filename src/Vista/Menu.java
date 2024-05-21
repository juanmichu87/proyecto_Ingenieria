package Vista;

import java.io.BufferedReader;
import java.io.FileReader; // Importa la clase FileReader para leer archivos.
import java.io.FileWriter; // Importa la clase FileWriter para escribir archivos.
import java.io.IOException; // Importa la clase IOException para manejar excepciones de entrada/salida.
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner; // Importa la clase Scanner para leer entradas del usuario.

import com.google.gson.Gson; // Importa la clase Gson para manipular JSON.
import com.google.gson.JsonArray; // Importa la clase JsonArray para trabajar con arreglos JSON.
import com.google.gson.JsonElement; // Importa la clase JsonElement para trabajar con elementos JSON.
import com.google.gson.JsonObject; // Importa la clase JsonObject para trabajar con objetos JSON.
import com.google.gson.reflect.TypeToken;

import CONTROLADORES.controlador_admin; // Importa la clase controlador_admin.
import CONTROLADORES.controlador_profe; // Importa la clase controlador_profe.
import CONTROLADORES.controlador_usu; // Importa la clase controlador_usu.
import entidades.Usuario;

/**
 * Clase Menu para gestionar las interacciones del usuario con el sistema.
 */
public class Menu {
	private JsonArray usuarios; // Arreglo JSON para almacenar los usuarios.
	private JsonArray salas; // Arreglo JSON para almacenar las salas.
	private int idUsuarioLogueado; // ID del usuario que ha iniciado sesión.
	private controlador_admin cA; // Controlador para funciones de administrador.
	private controlador_profe cP; // Controlador para funciones de profesor.
	private controlador_usu cU; // Controlador para funciones de usuario.

	/**
	 * Constructor de la clase Menu.
	 */
	public Menu() {
		cargarUsuarios(); // Carga los datos de usuarios desde el archivo JSON.
		cargarSalas(); // Carga los datos de salas desde el archivo JSON.
		this.idUsuarioLogueado = getIdUsuarioLogueado(); // Asigna el ID del usuario logueado.
	}

	/**
	 * Carga los datos de usuarios desde el archivo usuarios.json.
	 */
	public void cargarUsuarios() {
		try {
			FileReader reader = new FileReader("Data/usuarios.json"); // Crea un lector de archivos para usuarios.json.
			usuarios = new Gson().fromJson(reader, JsonArray.class); // Convierte el contenido del archivo en un
																		// JsonArray.
			reader.close(); // Cierra el lector de archivos.
		} catch (IOException e) {
			e.printStackTrace(); // Imprime la traza de la excepción en caso de error.
		}
	}

	/**
	 * Carga los datos de salas desde el archivo salas.json.
	 */
	public void cargarSalas() {
		try {
			FileReader reader = new FileReader("Data/salas.json"); // Crea un lector de archivos para salas.json.
			salas = new Gson().fromJson(reader, JsonArray.class); // Convierte el contenido del archivo en un JsonArray.
			reader.close(); // Cierra el lector de archivos.
		} catch (IOException e) {
			e.printStackTrace(); // Imprime la traza de la excepción en caso de error.
		}
	}

	/**
	 * Guarda los datos de los usuarios en el archivo usuarios.json.
	 */
	public void guardarUsuarios() {
		try {
			FileWriter writer = new FileWriter("Data/usuarios.json"); // Crea un escritor de archivos para
																		// usuarios.json.
			writer.write(new Gson().toJson(usuarios)); // Escribe los datos de los usuarios en formato JSON.
			writer.close(); // Cierra el escritor de archivos.
		} catch (IOException e) {
			e.printStackTrace(); // Imprime la traza de la excepción en caso de error.
		}
	}

	/**
	 * Obtiene el ID del usuario logueado.
	 *
	 * @return ID del usuario logueado.
	 */
	public int getIdUsuarioLogueado() {
		return idUsuarioLogueado; // Devuelve el ID del usuario logueado.
	}

	/**
	 * Muestra el submenú basado en el tipo de usuario.
	 *
	 * @param tipoUsuario El tipo de usuario logueado.
	 */
	public void mostrarSubMenu(String tipoUsuario) {
		System.out.println("Id del usuario: " + idUsuarioLogueado); // Imprime el ID del usuario logueado.
		cargarUsuarios(); // Carga los datos de usuarios.
		cargarSalas(); // Carga los datos de salas.
		cA = new controlador_admin(getIdUsuarioLogueado()); // Inicializa el controlador de administrador.
		cP = new controlador_profe(getIdUsuarioLogueado()); // Inicializa el controlador de profesor.
		cU = new controlador_usu(getIdUsuarioLogueado()); // Inicializa el controlador de usuario.

		Scanner scanner = new Scanner(System.in); // Crea un objeto Scanner para leer la entrada del usuario.
		int opcion = -1; // Variable para almacenar la opción seleccionada por el usuario.

		do {
			System.out.println("--- MENÚ PRINCIPAL ---");
			switch (tipoUsuario) {
			case "ADMINISTRADOR":
				System.out.println("[ADMINISTRADOR]");
				System.out.println("1. Gestión de Usuarios");
				System.out.println("2. Registro de Asistencia");
				System.out.println("3. Historial de Salas");
				System.out.println("4. Gestión de Salas");
				System.out.println("5. Exportar Datos Usuarios");
				System.out.println("6. Cerrar Sesión");

				break;
			case "PROFESOR":
				System.out.println("[PROFESOR DE SALA]");
				System.out.println("1. Ver Lista de Personas en Sala");
				System.out.println("2. Gestión de Clases");
				System.out.println("3. Ver Disponibilidad de Salas");
				System.out.println("4. Reservar Sala");
				System.out.println("5. Exportar Datos Salas");
				System.out.println("6. Cerrar Sesión");

				break;
			case "USUARIO":
				System.out.println("[USUARIO BÁSICO]");
				System.out.println("1. Ver Clases Disponibles");
				System.out.println("2. Inscribirse en Clase");
				System.out.println("3. Cancelar Inscripción en Clase");
				System.out.println("4. Ver Historial de Asistencia");
				System.out.println("5. Ver salas inscritas");
				System.out.println("6. Cerrar Sesión");

				break;
			default:
				System.out.println("Tipo de usuario no válido.");
				return;
			}

			System.out.print("Seleccione una opción: ");

			try {
				opcion = scanner.nextInt(); // Lee la opción seleccionada por el usuario.
			} catch (InputMismatchException e) {
				System.out.println("Opción no válida. Por favor, ingrese un número.");
				scanner.next(); // Limpiar la entrada inválida
				continue; // Volver a mostrar el menú
			}

			switch (tipoUsuario) {
			case "ADMINISTRADOR":
				switch (opcion) {
				case 1:
					cA.gestionarUsuarios(); // Llama al método para gestionar usuarios.
					break;
				case 2:
					cA.registrarAsistencia(); // Llama al método para registrar asistencia.
					break;
				case 3:
					cA.historialSalas(); // Llama al método para ver el historial de salas.
					break;
				case 4:
					cA.gestionSalas(); // Llama al método para gestionar salas.
					break;
				case 5:
					cA.exportarUsuariosACSV();
					break;
				case 6:
					System.out.println("Cerrando sesión...");
					break;
				default:
					System.out.println("Opción no válida."); // Maneja opciones no válidas.
					break;
				}
				break;
			case "PROFESOR":
				int idProfesor = getIdUsuarioLogueado(); // Obtiene el ID del profesor logueado.
				switch (opcion) {
				case 1:
					cP.verListaPersonasSala(idProfesor); // Llama al método para ver la lista de personas en la sala.
					break;
				case 2:
					cP.gestionClases(); // Llama al método para gestionar clases.
					break;
				case 3:
					cP.verDisponibilidadSalas(); // Llama al método para ver la disponibilidad de salas.
					break;
				case 4:
					cP.reservarSala(); // Llama al método para reservar una sala.
					break;
				case 5:
					cP.exportarSalasACSV();
					break;
				case 6:
					System.out.println("Cerrando sesión...");
					break;
				default:
					System.out.println("Opción no válida."); // Maneja opciones no válidas.
					break;
				}
				break;
			case "USUARIO":
				switch (opcion) {
				case 1:
					cU.verClasesDisponibles(); // Llama al método para ver las clases disponibles.
					break;
				case 2:
					cU.inscribirseEnClase(); // Llama al método para inscribirse en una clase.
					break;
				case 3:
					cU.cancelarInscripcionClase(); // Llama al método para cancelar la inscripción en una clase.
					break;
				case 4:
					cU.verHistorialAsistencia(); // Llama al método para ver el historial de asistencia.
					break;
				case 5:
					cU.verSalasInscrito();
					break;
				case 6:
					System.out.println("Cerrando sesión...");
					break;
				default:
					System.out.println("Opción no válida."); // Maneja opciones no válidas.
					break;
				}
				break;
			}
		} while (opcion != 6); // Repite el bucle hasta que se seleccione la opción de cerrar sesión.
	}

	/**
	 * Inicia sesión del usuario.
	 *
	 * @param email    El email del usuario.
	 * @param password La contraseña del usuario.
	 * @return true si las credenciales son correctas, false en caso contrario.
	 */
	public boolean iniciarSesion(String email, String password) {
		for (JsonElement element : usuarios) { // Itera sobre la lista de usuarios.
			JsonObject usuario = element.getAsJsonObject(); // Obtiene el objeto JSON del usuario actual.
			if (usuario.get("email").getAsString().equals(email)
					&& usuario.get("password").getAsString().equals(password)) {
				int idUsuario = usuario.get("id").getAsInt(); // Obtiene el ID del usuario.
				String tipoUsuario = usuario.get("tipo").getAsString(); // Obtiene el tipo de usuario.
				System.out.println("¡Inicio de sesión exitoso como " + tipoUsuario + "!");
				this.idUsuarioLogueado = idUsuario; // Establece el ID del usuario logueado en el menú.
				mostrarSubMenu(tipoUsuario); // Muestra el submenú correspondiente al tipo de usuario.
				return true;
			}
		}
		System.out.println("Credenciales incorrectas. Por favor, inténtelo de nuevo.");
		return false;
	}

	/**
	 * Registra un nuevo usuario en el sistema.
	 */
	public void registrarUsuario() {
		Scanner scanner = new Scanner(System.in); // Crea un objeto Scanner para leer la entrada del usuario.

		System.out.println("--- REGISTRO DE USUARIO ---");
		System.out.print("Nombre: ");
		String nombre = scanner.nextLine(); // Lee el nombre ingresado por el usuario.

		System.out.print("Email: ");
		String email = scanner.nextLine(); // Lee el email ingresado por el usuario.

		if (!validarEmail(email)) { // Valida el formato del email.
			System.out.println("El email ingresado no es válido.");
			return;
		}

		if (emailExiste(email)) { // Verifica si el email ya está registrado.
			System.out.println("El email ingresado ya está registrado.");
			return;
		}

		System.out.print("Password: ");
		String password = scanner.nextLine(); // Lee la contraseña ingresada por el usuario.

		if (!validarContraseña(password)) { // Valida la seguridad de la contraseña.
			System.out.println("La contraseña debe tener al menos una letra mayúscula y al menos un dígito.");
			return;
		}

		// Genera un nuevo ID para el usuario
		int nuevoId = obtenerSiguienteID("Data/usuarios.json");

		// Crea el objeto JSON del nuevo usuario
		JsonObject nuevoUsuario = new JsonObject();
		nuevoUsuario.addProperty("id", nuevoId);
		nuevoUsuario.addProperty("nombre", nombre);
		nuevoUsuario.addProperty("email", email);
		nuevoUsuario.addProperty("password", password);
		nuevoUsuario.addProperty("tipo", "USUARIO"); // Por defecto, se registra como usuario básico
		JsonArray historialAsistencia = new JsonArray(); // Crea un nuevo JsonArray vacío
		nuevoUsuario.add("historialAsistencia", historialAsistencia);

		// Agrega el nuevo usuario al arreglo
		usuarios.add(nuevoUsuario);

		// Guarda los usuarios actualizados en el archivo
		guardarUsuarios();

		System.out.println("¡Registro exitoso!");
	}

	/**
	 * Verifica si un email ya está registrado en el sistema.
	 *
	 * @param email El email a verificar.
	 * @return true si el email ya está registrado, false en caso contrario.
	 */
	private boolean emailExiste(String email) {
		for (JsonElement element : usuarios) { // Itera sobre la lista de usuarios.
			JsonObject usuario = element.getAsJsonObject(); // Obtiene el objeto JSON del usuario actual.
			if (usuario.get("email").getAsString().equals(email)) {
				return true; // Retorna true si se encuentra un usuario con el mismo email.
			}
		}
		return false; // Retorna false si no se encuentra ningún usuario con el mismo email.
	}

	/**
	 * Valida el formato del email.
	 *
	 * @param email El email a validar.
	 * @return true si el formato es válido, false en caso contrario.
	 */
	private boolean validarEmail(String email) {
		// Validar el formato del email (puedes implementar una lógica más avanzada
		// aquí)
		return email.matches("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b");
	}

	/**
	 * Valida que la contraseña tenga al menos una mayúscula y un número.
	 *
	 * @param contraseña La contraseña a validar.
	 * @return true si la contraseña es válida, false en caso contrario.
	 */
	private boolean validarContraseña(String contraseña) {
		boolean tieneMayuscula = false; // Indica si la contraseña tiene al menos una mayúscula.
		boolean tieneNumero = false; // Indica si la contraseña tiene al menos un número.
		for (char caracter : contraseña.toCharArray()) { // Itera sobre los caracteres de la contraseña.
			if (Character.isUpperCase(caracter)) { // Verifica si el carácter es una mayúscula.
				tieneMayuscula = true;
			} else if (Character.isDigit(caracter)) { // Verifica si el carácter es un dígito.
				tieneNumero = true;
			}
		}
		return tieneMayuscula && tieneNumero; // Devuelve true si la contraseña tiene al menos una mayúscula y un
												// número.
	}

	/**
	 * Método que se encarga de seleccionar el ID del último usuario y sumarle 1
	 * para generar el id del usuario que se va a registrar.
	 * 
	 * @param archivoJSON ruta del archivo json de usuarios.
	 * @return ID del último usuario sumándole 1.
	 */
	public static int obtenerSiguienteID(String archivoJSON) {
		int siguienteID = 1; // Valor por defecto si el archivo está vacío o no se puede leer

		try (BufferedReader br = new BufferedReader(new FileReader(archivoJSON))) {
			// Utilizamos Gson para leer el archivo JSON
			Gson gson = new Gson();
			// Definimos el tipo de lista que esperamos recibir del JSON
			Type listaUsuariosType = new TypeToken<ArrayList<Usuario>>() {
			}.getType();
			// Leemos el contenido del archivo JSON en un ArrayList de usuarios
			ArrayList<Usuario> usuarios = gson.fromJson(br, listaUsuariosType);

			// Si la lista de usuarios no está vacía, obtenemos el último ID y le sumamos
			// uno
			if (usuarios != null && !usuarios.isEmpty()) {
				siguienteID = usuarios.get(usuarios.size() - 1).getId() + 1;
			} else {
				siguienteID = 1;
			}
		} catch (IOException e) {
			// Manejo de excepciones si ocurre algún problema al leer el archivo
			e.printStackTrace();
		}

		return siguienteID;
	}
}
