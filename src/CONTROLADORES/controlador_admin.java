package CONTROLADORES;

import java.io.FileReader; // Importa la clase FileReader para leer archivos.
import java.io.FileWriter; // Importa la clase FileWriter para escribir archivos.
import java.io.IOException; // Importa la clase IOException para manejar excepciones de entrada/salida.
import java.util.InputMismatchException; // Importa la clase InputMismatchException para manejar excepciones de tipo de entrada.
import java.util.Scanner; // Importa la clase Scanner para leer entradas del usuario.

import com.google.gson.Gson; // Importa la clase Gson para manipular JSON.
import com.google.gson.JsonArray; // Importa la clase JsonArray para trabajar con arreglos JSON.
import com.google.gson.JsonElement; // Importa la clase JsonElement para trabajar con elementos JSON.
import com.google.gson.JsonObject; // Importa la clase JsonObject para trabajar con objetos JSON.

import Vista.Menu; // Importa la clase Menu del paquete Vista.

/**
 * Clase controlador_admin para gestionar usuarios y salas por parte de un
 * administrador.
 */
public class controlador_admin {
	private JsonArray usuarios; // Arreglo JSON para almacenar los usuarios.
	private JsonArray salas; // Arreglo JSON para almacenar las salas.
	private Menu menu; // Objeto Menu para mostrar menús y submenús.
	private int idUsuarioLogueado; // ID del usuario que ha iniciado sesión.

	/**
	 * Constructor de la clase controlador_admin.
	 *
	 * @param idUsu ID del usuario que se loguea como administrador.
	 */
	public controlador_admin(int idUsu) {
		cargarUsuarios(); // Carga los datos de usuarios desde el archivo JSON.
		cargarSalas(); // Carga los datos de salas desde el archivo JSON.
		this.idUsuarioLogueado = idUsu; // Asigna el ID del usuario logueado.
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
	 * Permite al administrador gestionar los usuarios.
	 */
	public void gestionarUsuarios() {
		Scanner scanner = new Scanner(System.in); // Crea un objeto Scanner para leer la entrada del usuario.
		menu = new Menu(); // Inicializa el menú.

		while (true) {
			// Mostrar lista de usuarios
			System.out.println("--- LISTA DE USUARIOS ---");
			for (int i = 0; i < usuarios.size(); i++) { // Itera sobre la lista de usuarios.
				JsonObject usuario = usuarios.get(i).getAsJsonObject(); // Obtiene el objeto JSON del usuario actual.
				System.out.println((i + 1) + ". " + usuario.get("nombre").getAsString() + " ("
						+ usuario.get("tipo").getAsString() + ")");
			}

			// Solicitar al administrador que elija un usuario para gestionar
			System.out
					.print("Seleccione el número del usuario que desea gestionar (0 para volver al menú principal): ");
			int opcionUsuario = scanner.nextInt(); // Lee la opción seleccionada por el usuario.

			if (opcionUsuario == 0) {
				return; // Volver al menú principal si se selecciona 0.
			} else if (opcionUsuario < 1 || opcionUsuario > usuarios.size()) {
				System.out.println("Número de usuario no válido. Por favor, seleccione un número válido.");
				continue; // Volver a mostrar el menú de gestión de usuarios.
			}

			// Obtener el usuario seleccionado
			JsonObject usuarioSeleccionado = usuarios.get(opcionUsuario - 1).getAsJsonObject(); // Obtiene el usuario
																								// seleccionado.
			System.out.println("--- GESTIÓN DE USUARIO ---");
			System.out.println("Usuario seleccionado: " + usuarioSeleccionado.get("nombre").getAsString());

			// Mostrar opciones de gestión para el usuario seleccionado
			System.out.println("1. Modificar");
			System.out.println("2. Eliminar");
			System.out.println("3. Cambiar Rol");
			System.out.println("4. Salir");

			// Solicitar al administrador que elija una acción
			System.out.print("Seleccione la acción que desea realizar: ");
			int opcionAccion = scanner.nextInt(); // Lee la acción seleccionada por el usuario.
			switch (opcionAccion) {
			case 1:
				modificarUsuario(usuarioSeleccionado); // Llama al método para modificar el usuario.
				break;
			case 2:
				eliminarUsuario(usuarioSeleccionado); // Llama al método para eliminar el usuario.
				break;
			case 3:
				cambiarRol(usuarioSeleccionado); // Llama al método para cambiar el rol del usuario.
				break;

			case 4:
				return; // Salir del menú de gestión de usuarios.
			default:
				System.out.println("Opción no válida. Por favor, seleccione una opción válida.");
				break;
			}
		}
	}

	/**
	 * Modifica los datos de un usuario.
	 *
	 * @param usuario Objeto JSON del usuario a modificar.
	 */
	public void modificarUsuario(JsonObject usuario) {
		Scanner scanner = new Scanner(System.in); // Crea un objeto Scanner para leer la entrada del usuario.

		System.out.println("--- MODIFICAR USUARIO ---");
		System.out.println("Nombre actual: " + usuario.get("nombre").getAsString());
		System.out.print("Nuevo nombre: ");
		String nuevoNombre = scanner.nextLine(); // Lee el nuevo nombre del usuario.
		usuario.addProperty("nombre", nuevoNombre); // Actualiza el nombre del usuario.

		System.out.println("Correo actual: " + usuario.get("email").getAsString());
		System.out.print("Nuevo correo: ");
		String nuevoCorreo = scanner.nextLine(); // Lee el nuevo correo del usuario.
		usuario.addProperty("email", nuevoCorreo); // Actualiza el correo del usuario.

		System.out.println("Contraseña actual: " + usuario.get("password").getAsString());
		System.out.print("Nueva contraseña: ");
		String nuevaContraseña = scanner.nextLine(); // Lee la nueva contraseña del usuario.
		// Validar nueva contraseña (por ejemplo, debe contener al menos una mayúscula y
		// un número)
		if (validarContraseña(nuevaContraseña)) { // Verifica si la nueva contraseña es válida.
			usuario.addProperty("password", nuevaContraseña); // Actualiza la contraseña del usuario.
			guardarUsuarios(); // Guarda los datos actualizados de los usuarios.
			System.out.println("Usuario modificado exitosamente.");
		} else {
			System.out.println("La contraseña no cumple con los requisitos mínimos.");
		}
	}

	/**
	 * Elimina un usuario.
	 *
	 * @param usuario Objeto JSON del usuario a eliminar.
	 */
	public void eliminarUsuario(JsonObject usuario) {
		Scanner scanner = new Scanner(System.in); // Crea un objeto Scanner para leer la entrada del usuario.

		System.out.println("--- ELIMINAR USUARIO ---");
		System.out.println(
				"¿Está seguro de que desea eliminar al usuario " + usuario.get("nombre").getAsString() + "? (s/n)");
		String respuesta = scanner.nextLine(); // Lee la respuesta del usuario.
		if (respuesta.equalsIgnoreCase("s")) {
			// Eliminar usuario de la lista
			usuarios.remove(usuario); // Elimina el usuario del JsonArray de usuarios.
			guardarUsuarios(); // Guarda los datos actualizados de los usuarios.
			System.out.println("Usuario eliminado exitosamente.");
		} else {
			System.out.println("Operación cancelada. El usuario no ha sido eliminado.");
		}
	}

	/**
	 * Cambia el rol de un usuario.
	 *
	 * @param usuario Objeto JSON del usuario cuyo rol se cambiará.
	 */
	public void cambiarRol(JsonObject usuario) {
		Scanner scanner = new Scanner(System.in); // Crea un objeto Scanner para leer la entrada del usuario.

		System.out.println("--- CAMBIAR ROL DE USUARIO ---");
		System.out.println("Rol actual: " + usuario.get("tipo").getAsString());
		System.out.print("Nuevo rol (ADMINISTRADOR, PROFESOR, USUARIO): ");
		String nuevoRol = scanner.nextLine(); // Lee el nuevo rol del usuario.
		if (nuevoRol.equalsIgnoreCase("ADMINISTRADOR") || nuevoRol.equalsIgnoreCase("PROFESOR")
				|| nuevoRol.equalsIgnoreCase("USUARIO")) {
			usuario.addProperty("tipo", nuevoRol); // Actualiza el rol del usuario.
			guardarUsuarios(); // Guarda los datos actualizados de los usuarios.
			System.out.println("Rol de usuario cambiado exitosamente a: " + nuevoRol);
		} else {
			System.out.println("Rol no válido. Por favor, seleccione un rol válido.");
		}
	}

	/**
	 * Permite al administrador gestionar las salas.
	 */
	public void gestionSalas() {
		Scanner scanner = new Scanner(System.in); // Crea un objeto Scanner para leer la entrada del usuario.

		System.out.println("--- GESTIÓN DE SALAS ---");
		System.out.println("ID\tReservada\tProfesor Asociado\tTurno");

		for (JsonElement element : salas) { // Itera sobre la lista de salas.
			JsonObject sala = element.getAsJsonObject(); // Obtiene el objeto JSON de la sala actual.
			int idSala = sala.get("id").getAsInt(); // Obtiene el ID de la sala.
			boolean reservada = sala.get("reservada").getAsBoolean(); // Obtiene el estado de reserva de la sala.
			JsonElement profesorElement = sala.get("idProfesorAsociado"); // Obtiene el ID del profesor asociado.
			String profesorAsociado = (profesorElement != null && !profesorElement.isJsonNull())
					? obtenerNombreProfesor(profesorElement.getAsInt())
					: "Sin profesor"; // Obtiene el nombre del profesor asociado o asigna "Sin profesor".
			String turno = sala.get("turno").getAsString(); // Obtiene el turno de la sala.

			String infoSala = idSala + "\t" + reservada + "\t\t" + profesorAsociado + "\t\t" + turno;
			System.out.println(infoSala); // Muestra la información de la sala.
		}

		try {
			System.out.print("¿Desea modificar alguna sala? (S/N): ");
			String respuesta = scanner.nextLine(); // Lee la respuesta del usuario.

			if (respuesta.equalsIgnoreCase("S")) {
				System.out.print("Ingrese el ID de la sala a modificar: ");
				int idSala2 = scanner.nextInt(); // Lee el ID de la sala a modificar.
				scanner.nextLine(); // Limpiar el buffer

				// Llamar al método para modificar la sala
				modificarSala(idSala2); // Llama al método para modificar la sala.
			}
		} catch (InputMismatchException e) {
			System.out.println("Por favor, ingrese un ID válido.");
		}
	}

	/**
	 * Obtiene el nombre de un profesor por su ID.
	 *
	 * @param idProfesor ID del profesor.
	 * @return Nombre del profesor o "Desconocido" si no se encuentra.
	 */
	public String obtenerNombreProfesor(int idProfesor) {
		for (JsonElement element : usuarios) { // Itera sobre la lista de usuarios.
			JsonObject usuario = element.getAsJsonObject(); // Obtiene el objeto JSON del usuario actual.
			if (usuario.get("id").getAsInt() == idProfesor) {
				return usuario.get("nombre").getAsString(); // Devuelve el nombre del profesor si se encuentra.
			}
		}
		return "Desconocido"; // Devuelve "Desconocido" si no se encuentra el profesor.
	}

	/**
	 * Modifica los datos de una sala.
	 *
	 * @param idSala ID de la sala a modificar.
	 */
	public void modificarSala(int idSala) {
		Scanner scanner = new Scanner(System.in); // Crea un objeto Scanner para leer la entrada del usuario.

		// Encontrar la sala con el ID proporcionado
		JsonObject salaModificar = null;
		for (JsonElement element : salas) { // Itera sobre la lista de salas.
			JsonObject sala = element.getAsJsonObject(); // Obtiene el objeto JSON de la sala actual.
			if (sala.get("id").getAsInt() == idSala) { // Verifica si el ID de la sala coincide con el ID proporcionado.
				salaModificar = sala; // Asigna la sala a modificar.
				break; // Sale del bucle.
			}
		}

		// Verificar si se encontró la sala
		if (salaModificar == null) {
			System.out.println("No se encontró ninguna sala con el ID proporcionado.");
			return; // Sale del método si no se encuentra la sala.
		}

		// Mostrar información actual de la sala
		System.out.println("--- MODIFICAR SALA ---");
		System.out.println("Sala ID: " + salaModificar.get("id").getAsInt());
		System.out.println("Reservada: " + salaModificar.get("reservada").getAsBoolean());

		JsonElement idProfesorElement = salaModificar.get("idProfesorAsociado");
		String profesorAsociado = (idProfesorElement != null && !idProfesorElement.isJsonNull())
				? obtenerNombreProfesor(idProfesorElement.getAsInt())
				: "Sin profesor";
		System.out.println("Profesor Asociado: " + profesorAsociado);

		JsonElement turnoElement = salaModificar.get("turno");
		String turno = (turnoElement != null && !turnoElement.isJsonNull()) ? turnoElement.getAsString() : "Sin turno";
		System.out.println("Turno: " + turno);

		// Pedir nuevos valores para la sala
		try {
			System.out.print("¿Cambiar estado de reserva? (true/false): ");
			boolean nuevaReserva = scanner.nextBoolean(); // Lee el nuevo estado de reserva de la sala.
			salaModificar.addProperty("reservada", nuevaReserva); // Actualiza el estado de reserva de la sala.

			if (nuevaReserva) {
				// Si la sala se reserva, pedir el ID del profesor asociado
				System.out.print("Ingrese el ID del profesor asociado (o 0 si no tiene profesor): ");
				int nuevoIdProfesor = scanner.nextInt(); // Lee el nuevo ID del profesor asociado.
				salaModificar.addProperty("idProfesorAsociado", nuevoIdProfesor); // Actualiza el ID del profesor
																					// asociado.
			} else {
				// Si la sala no se reserva, eliminar el profesor asociado y el turno
				salaModificar.remove("idProfesorAsociado"); // Elimina el ID del profesor asociado.
				salaModificar.remove("turno"); // Elimina el turno de la sala.
			}

			// Pedir el nuevo turno (si se modifica)
			if (nuevaReserva) {
				scanner.nextLine(); // Limpiar el buffer
				System.out.print("¿Cambiar turno? (mañana/tarde): ");
				String nuevoTurno = scanner.nextLine(); // Lee el nuevo turno de la sala.
				salaModificar.addProperty("turno", nuevoTurno); // Actualiza el turno de la sala.
			}

			// Guardar los cambios en el JSON
			guardarSalas(); // Guarda los datos actualizados de las salas.

			System.out.println("Sala modificada exitosamente.");
		} catch (InputMismatchException e) {
			System.out.println("Por favor, ingrese un valor válido.");
		}
	}

	/**
	 * Muestra el historial de reservas de las salas.
	 */
	public void historialSalas() {
		System.out.println("--- HISTORIAL DE SALAS ---");

		for (JsonElement element : salas) { // Itera sobre la lista de salas.
			JsonObject salaJson = element.getAsJsonObject(); // Obtiene el objeto JSON de la sala actual.
			int idSala = salaJson.get("id").getAsInt(); // Obtiene el ID de la sala.
			boolean reservada = salaJson.get("reservada").getAsBoolean(); // Obtiene el estado de reserva de la sala.
			JsonElement profesorAsociadoElement = salaJson.get("idProfesorAsociado"); // Obtiene el ID del profesor
																						// asociado.
			int idProfesorAsociado = (profesorAsociadoElement != null && !profesorAsociadoElement.isJsonNull())
					? profesorAsociadoElement.getAsInt()
					: 0; // Asigna el ID del profesor asociado o 0 si no hay profesor asociado.
			String turno = salaJson.get("turno").getAsString(); // Obtiene el turno de la sala.
			JsonArray historial = salaJson.getAsJsonArray("historial"); // Obtiene el historial de la sala.

			System.out.println("Sala ID: " + idSala);
			System.out.println("Reservada: " + (reservada ? "Sí" : "No"));
			System.out.println("Profesor Asociado (ID): "
					+ (idProfesorAsociado != 0 ? idProfesorAsociado : "Sin profesor asociado"));
			System.out.println("Turno: " + turno);

			if (historial != null && historial.size() > 0) {
				System.out.println("Historial:");
				for (JsonElement evento : historial) { // Itera sobre el historial de la sala.
					JsonObject eventoSala = evento.getAsJsonObject(); // Obtiene el objeto JSON del evento actual.
					int idProfesorReserva = eventoSala.get("profesorReserva").getAsInt(); // Obtiene el ID del profesor
																							// que reservó la sala.
					String turnoReserva = eventoSala.get("turnoReserva").getAsString(); // Obtiene el turno de la
																						// reserva.
					System.out.println("  - Fue reservada por Profesor (ID): " + idProfesorReserva
							+ ", en el turno de: " + turnoReserva);
				}
			} else {
				System.out.println("Historial: Nunca ha sido reservada");
			}

			System.out.println(); // Línea en blanco entre salas
		}
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
	 * Guarda los datos de los usuarios en el archivo usuarios.json.
	 */
	public void guardarUsuarios() {
		try {
			FileWriter writer = new FileWriter("Data/usuarios.json"); // Crea un escritor de archivos para usuarios.json.
			writer.write(new Gson().toJson(usuarios)); // Escribe los datos de los usuarios en formato JSON.
			writer.close(); // Cierra el escritor de archivos.
		} catch (IOException e) {
			e.printStackTrace(); // Imprime la traza de la excepción en caso de error.
		}
	}

	/**
	 * Guarda los datos de las salas en el archivo salas.json.
	 */
	public void guardarSalas() {
		try {
			FileWriter writer = new FileWriter("Data/salas.json"); // Crea un escritor de archivos para salas.json.
			writer.write(new Gson().toJson(salas)); // Escribe los datos de las salas en formato JSON.
			writer.close(); // Cierra el escritor de archivos.
		} catch (IOException e) {
			System.out.println("Error al guardar las salas en el archivo JSON.");
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
	 * Registra la asistencia de los usuarios en las salas.
	 */
	public void registrarAsistencia() {
		for (JsonElement element : salas) { // Itera sobre la lista de salas.
			JsonObject sala = element.getAsJsonObject(); // Obtiene el objeto JSON de la sala actual.

			JsonArray historialAsistencia = sala.getAsJsonArray("historial"); // Obtiene el historial de asistencia de
																				// la sala.

			System.out.println("Historial de asistencia para la Sala " + sala.get("id").getAsInt() + ":");

			if (historialAsistencia.size() == 0) {
				System.out.println("La sala nunca ha sido reservada o no ha asistido nadie.");
			} else {
				for (JsonElement registro : historialAsistencia) { // Itera sobre el historial de asistencia de la sala.
					JsonObject registroAsistencia = registro.getAsJsonObject(); // Obtiene el objeto JSON del registro
																				// de asistencia.
					int idProfesor = registroAsistencia.get("profesorReserva").getAsInt(); // Obtiene el ID del profesor
																							// que reservó la sala.
					System.out.println("ID del profesor: " + idProfesor);
				}
			}
			System.out.println(); // Línea en blanco entre salas
		}
	}

//////////////////////////////EXPORTAR DATOS A CSV////////////////////////	
	
	/**
	 * Exporta los datos de los usuarios a un archivo CSV.
	 */
	public void exportarUsuariosACSV() {
		try (FileWriter writer = new FileWriter("Exportaciones/usuarios.csv")) {
			// Escribir encabezados
			writer.append("ID,Nombre,Email,Contraseña,Tipo\n");

			// Escribir datos de usuarios
			for (JsonElement element : usuarios) {
				JsonObject usuario = element.getAsJsonObject();
				writer.append(usuario.get("id").getAsString()).append(",");
				writer.append(usuario.get("nombre").getAsString()).append(",");
				writer.append(usuario.get("email").getAsString()).append(",");
				writer.append(usuario.get("password").getAsString()).append(",");
				writer.append(usuario.get("tipo").getAsString()).append("\n");
			}

			System.out.println("Datos de usuarios exportados exitosamente a usuarios.csv");
		} catch (IOException e) {
			System.out.println("Error al exportar datos de usuarios a CSV");
			e.printStackTrace();
		}
	}
}
