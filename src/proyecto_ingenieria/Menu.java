package proyecto_ingenieria;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class Menu {
	private JsonArray usuarios;
	private JsonArray salas;
	private int idUsuarioLogueado;

	public Menu() {
		cargarUsuarios();
		cargarSalas();
	}

	public void cargarUsuarios() {
		try {
			FileReader reader = new FileReader("usuarios.json");
			usuarios = new Gson().fromJson(reader, JsonArray.class);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void cargarSalas() {
		try {
			FileReader reader = new FileReader("salas.json");
			salas = new Gson().fromJson(reader, JsonArray.class);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void guardarUsuarios() {
		try {
			FileWriter writer = new FileWriter("usuarios.json");
			writer.write(new Gson().toJson(usuarios));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getIdUsuarioLogueado() {
		return idUsuarioLogueado;
	}

	public void mostrarSubMenu(String tipoUsuario) {
		Scanner scanner = new Scanner(System.in);
		int opcion;

		do {
			System.out.println("--- MENÚ PRINCIPAL ---");
			switch (tipoUsuario) {
			case "ADMINISTRADOR":
				System.out.println("[ADMINISTRADOR]");
				System.out.println("1. Gestión de Usuarios");
				System.out.println("2. Registro de Asistencia");
				System.out.println("3. Historial de Salas");
				System.out.println("4. Gestión de Salas");
				break;
			case "PROFESOR":
				System.out.println("[PROFESOR DE SALA]");
				System.out.println("1. Ver Lista de Personas en Sala");
				System.out.println("2. Gestión de Clases");
				System.out.println("3. Ver Disponibilidad de Salas");
				System.out.println("4. Reservar Sala");
				break;
			case "USUARIO":
				System.out.println("[USUARIO BÁSICO]");
				System.out.println("1. Ver Clases Disponibles");
				System.out.println("2. Inscribirse en Clase");
				System.out.println("3. Cancelar Inscripción en Clase");
				System.out.println("4. Ver Historial de Asistencia");
				break;
			default:
				System.out.println("Tipo de usuario no válido.");
				return;
			}
			System.out.println("5. Cerrar Sesión");
			System.out.print("Seleccione una opción: ");
			opcion = scanner.nextInt();

			switch (tipoUsuario) {
			case "ADMINISTRADOR":
				switch (opcion) {
				case 1:
					gestionarUsuarios();
					break;
				case 3:
					historialSalas();
					break;
				case 4:
					gestionSalas();
					break;
				// Agregar casos para otras opciones del administrador si es necesario
				default:
					// Lógica para otras opciones del administrador
					break;
				}
				break;
			case "PROFESOR":
				int idProfesor = getIdUsuarioLogueado(); // Función para obtener el ID de la sala reservada
				switch (opcion) {
				case 1:
					verListaPersonasSala(idProfesor);
					break;
				case 2:
					gestionClases();
					break;
				case 3:
					verDisponibilidadSalas();
					break;
				case 4:
					//reservarSala();
					break;

				default:
					// Manejar otras opciones del menú del profesor si es necesario
					break;
				}
				break;
			case "USUARIO":
				switch (opcion) {
				case 1:
					verClasesDisponibles();
					break;
				case 2:
					inscribirseEnClase();
					break;
				case 3:
					cancelarInscripcionClase();
					break;
				case 4:
					reservarSala();
					break;

					
					
					
				default:
					// Manejar otras opciones del menú del profesor si es necesario
					break;
				}
				break;

			}
		} while (opcion != 5);

		// Si se selecciona la opción de cerrar sesión, no hacer nada y salir del método
	}
//////////////////////////////ACCIONES INICIAR SESION//////////////////////////////////

	public boolean iniciarSesion(String email, String password) {
		for (JsonElement element : usuarios) {
			JsonObject usuario = element.getAsJsonObject();
			if (usuario.get("email").getAsString().equals(email)
					&& usuario.get("password").getAsString().equals(password)) {
				int idUsuario = usuario.get("id").getAsInt(); // Obtener el ID del usuario
				String tipoUsuario = usuario.get("tipo").getAsString();
				System.out.println("¡Inicio de sesión exitoso como " + tipoUsuario + "!");
				this.idUsuarioLogueado = idUsuario; // Establecer el ID del usuario logueado en el menú
				mostrarSubMenu(tipoUsuario);
				return true;
			}
		}
		System.out.println("Credenciales incorrectas. Por favor, inténtelo de nuevo.");
		return false;
	}

//////////////////////////////FIN ACCIONES INICIAR SESION//////////////////////////////////

//////////////////////////////ACCIONES REGISTRARSE//////////////////////////////////

	public void registrarUsuario() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("--- REGISTRO DE USUARIO ---");
		System.out.print("Nombre: ");
		String nombre = scanner.nextLine();

		System.out.print("Email: ");
		String email = scanner.nextLine();

		if (!validarEmail(email)) {
			System.out.println("El email ingresado no es válido.");
			return;
		}

		System.out.print("Password: ");
		String password = scanner.nextLine();

		if (!validarContraseña(password)) {
			System.out.println("La contraseña debe tener al menos una letra mayúscula y al menos un dígito.");
			return;
		}

		// Generar un nuevo ID para el usuario
		int nuevoId = usuarios.size() + 1;

		// Crear el objeto JSON del nuevo usuario
		JsonObject nuevoUsuario = new JsonObject();
		nuevoUsuario.addProperty("id", nuevoId);
		nuevoUsuario.addProperty("nombre", nombre);
		nuevoUsuario.addProperty("email", email);
		nuevoUsuario.addProperty("password", password);
		nuevoUsuario.addProperty("tipo", "USUARIO"); // Por defecto, se registra como usuario básico

		// Agregar el nuevo usuario al arreglo
		usuarios.add(nuevoUsuario);

		// Guardar los usuarios actualizados en el archivo
		guardarUsuarios();

		System.out.println("¡Registro exitoso!");
	}

	private boolean validarEmail(String email) {
		// Validar el formato del email (puedes implementar una lógica más avanzada
		// aquí)
		return email.matches("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b");
	}

//////////////////////////////FIN ACCIONES REGISTRARSE//////////////////////////////////

//////////////////////////////ACCIONES ADMINISTRADOR//////////////////////////////////

	public void gestionarUsuarios() {
		Scanner scanner = new Scanner(System.in);

		// Mostrar lista de usuarios
		System.out.println("--- LISTA DE USUARIOS ---");
		for (int i = 0; i < usuarios.size(); i++) {
			JsonObject usuario = usuarios.get(i).getAsJsonObject();
			System.out.println((i + 1) + ". " + usuario.get("nombre").getAsString() + " ("
					+ usuario.get("tipo").getAsString() + ")");
		}

		// Solicitar al administrador que elija un usuario para gestionar
		System.out.print("Seleccione el número del usuario que desea gestionar (0 para volver al menú principal): ");
		int opcionUsuario = scanner.nextInt();
		if (opcionUsuario == 0) {
			return; // Volver al menú principal si se selecciona 0
		} else if (opcionUsuario < 1 || opcionUsuario > usuarios.size()) {
			System.out.println("Número de usuario no válido. Por favor, seleccione un número válido.");
			return;
		}

		// Obtener el usuario seleccionado
		JsonObject usuarioSeleccionado = usuarios.get(opcionUsuario - 1).getAsJsonObject();
		System.out.println("--- GESTIÓN DE USUARIO ---");
		System.out.println("Usuario seleccionado: " + usuarioSeleccionado.get("nombre").getAsString());

		// Mostrar opciones de gestión para el usuario seleccionado
		System.out.println("1. Modificar");
		System.out.println("2. Eliminar");
		System.out.println("3. Cambiar Rol");
		System.out.println("4. Salir");

		// Solicitar al administrador que elija una acción
		System.out.print("Seleccione la acción que desea realizar: ");
		int opcionAccion = scanner.nextInt();
		switch (opcionAccion) {
		case 1:
			modificarUsuario(usuarioSeleccionado);
			break;
		case 2:
			eliminarUsuario(usuarioSeleccionado);
			break;
		case 3:
			cambiarRol(usuarioSeleccionado);
			break;
		case 4:
			mostrarSubMenu("ADMINISTRADOR");
			break;
		default:
			System.out.println("Opción no válida. Por favor, seleccione una opción válida.");
			break;
		}
	}

	private void modificarUsuario(JsonObject usuario) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("--- MODIFICAR USUARIO ---");
		System.out.println("Nombre actual: " + usuario.get("nombre").getAsString());
		System.out.print("Nuevo nombre: ");
		String nuevoNombre = scanner.nextLine();
		usuario.addProperty("nombre", nuevoNombre);

		System.out.println("Correo actual: " + usuario.get("email").getAsString());
		System.out.print("Nuevo correo: ");
		String nuevoCorreo = scanner.nextLine();
		usuario.addProperty("email", nuevoCorreo);

		System.out.println("Contraseña actual: " + usuario.get("password").getAsString());
		System.out.print("Nueva contraseña: ");
		String nuevaContraseña = scanner.nextLine();
		// Validar nueva contraseña (por ejemplo, debe contener al menos una mayúscula y
		// un número)
		if (validarContraseña(nuevaContraseña)) {
			usuario.addProperty("password", nuevaContraseña);
			guardarUsuarios();
			System.out.println("Usuario modificado exitosamente.");
		} else {
			System.out.println("La contraseña no cumple con los requisitos mínimos.");
		}
	}

	private void eliminarUsuario(JsonObject usuario) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("--- ELIMINAR USUARIO ---");
		System.out.println(
				"¿Está seguro de que desea eliminar al usuario " + usuario.get("nombre").getAsString() + "? (s/n)");
		String respuesta = scanner.nextLine();
		if (respuesta.equalsIgnoreCase("s")) {
			// Eliminar usuario de la lista
			usuarios.remove(usuario);
			guardarUsuarios();
			System.out.println("Usuario eliminado exitosamente.");
		} else {
			System.out.println("Operación cancelada. El usuario no ha sido eliminado.");
		}
	}

	private void cambiarRol(JsonObject usuario) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("--- CAMBIAR ROL DE USUARIO ---");
		System.out.println("Rol actual: " + usuario.get("tipo").getAsString());
		System.out.print("Nuevo rol (ADMINISTRADOR, PROFESOR, USUARIO): ");
		String nuevoRol = scanner.nextLine();
		if (nuevoRol.equalsIgnoreCase("ADMINISTRADOR") || nuevoRol.equalsIgnoreCase("PROFESOR")
				|| nuevoRol.equalsIgnoreCase("USUARIO")) {
			usuario.addProperty("tipo", nuevoRol);
			guardarUsuarios();
			System.out.println("Rol de usuario cambiado exitosamente a: " + nuevoRol);
		} else {
			System.out.println("Rol no válido. Por favor, seleccione un rol válido.");
		}
	}

	private void gestionSalas() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("--- GESTIÓN DE SALAS ---");
		System.out.println("ID\tReservada\tProfesor Asociado\tTurno");

		for (JsonElement element : salas) {
			JsonObject sala = element.getAsJsonObject();
			int idSala = sala.get("id").getAsInt();
			boolean reservada = sala.get("reservada").getAsBoolean();
			JsonElement profesorElement = sala.get("idProfesorAsociado");
			String profesorAsociado = (profesorElement != null && !profesorElement.isJsonNull())
					? obtenerNombreProfesor(profesorElement.getAsInt())
					: "Sin profesor";
			String turno = sala.get("turno").getAsString();

			String infoSala = idSala + "\t" + reservada + "\t\t" + profesorAsociado + "\t\t" + turno;
			System.out.println(infoSala);

		}

		try {
			System.out.print("¿Desea modificar alguna sala? (S/N): ");
			String respuesta = scanner.nextLine();

			if (respuesta.equalsIgnoreCase("S")) {
				System.out.print("Ingrese el ID de la sala a modificar: ");
				int idSala2 = scanner.nextInt();
				scanner.nextLine(); // Limpiar el buffer

				// Llamar al método para modificar la sala
				modificarSala(idSala2);
			}
		} catch (InputMismatchException e) {
			System.out.println("Por favor, ingrese un ID válido.");
		}
	}

	private String obtenerNombreProfesor(int idProfesor) {
		for (JsonElement element : usuarios) {
			JsonObject usuario = element.getAsJsonObject();
			if (usuario.get("id").getAsInt() == idProfesor) {
				return usuario.get("nombre").getAsString();
			}
		}
		return "Desconocido";
	}

	private void modificarSala(int idSala) {
		Scanner scanner = new Scanner(System.in);

		// Encontrar la sala con el ID proporcionado
		JsonObject salaModificar = null;
		for (JsonElement element : salas) {
			JsonObject sala = element.getAsJsonObject();
			if (sala.get("id").getAsInt() == idSala) {
				salaModificar = sala;
				break;
			}
		}

		// Verificar si se encontró la sala
		if (salaModificar == null) {
			System.out.println("No se encontró ninguna sala con el ID proporcionado.");
			return;
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
			boolean nuevaReserva = scanner.nextBoolean();
			salaModificar.addProperty("reservada", nuevaReserva);

			if (nuevaReserva) {
				// Si la sala se reserva, pedir el ID del profesor asociado
				System.out.print("Ingrese el ID del profesor asociado (o 0 si no tiene profesor): ");
				int nuevoIdProfesor = scanner.nextInt();
				salaModificar.addProperty("idProfesorAsociado", nuevoIdProfesor);
			} else {
				// Si la sala no se reserva, eliminar el profesor asociado y el turno
				salaModificar.remove("idProfesorAsociado");
				salaModificar.remove("turno");
			}

			// Pedir el nuevo turno (si se modifica)
			if (nuevaReserva) {
				scanner.nextLine(); // Limpiar el buffer
				System.out.print("¿Cambiar turno? (mañana/tarde): ");
				String nuevoTurno = scanner.nextLine();
				salaModificar.addProperty("turno", nuevoTurno);
			}

			// Guardar los cambios en el JSON
			guardarSalas();

			System.out.println("Sala modificada exitosamente.");
		} catch (InputMismatchException e) {
			System.out.println("Por favor, ingrese un valor válido.");
		}
	}

	private void guardarSalas() {
		try {
			FileWriter writer = new FileWriter("salas.json");
			writer.write(new Gson().toJson(salas));
			writer.close();
		} catch (IOException e) {
			System.out.println("Error al guardar las salas en el archivo JSON.");
			e.printStackTrace();
		}
	}

	private void historialSalas() {
		System.out.println("--- HISTORIAL DE SALAS ---");

		for (JsonElement element : salas) {
			JsonObject salaJson = element.getAsJsonObject();
			int idSala = salaJson.get("id").getAsInt();
			boolean reservada = salaJson.get("reservada").getAsBoolean();
			JsonElement profesorAsociadoElement = salaJson.get("idProfesorAsociado");
			int idProfesorAsociado = (profesorAsociadoElement != null && !profesorAsociadoElement.isJsonNull())
					? profesorAsociadoElement.getAsInt()
					: 0;
			String turno = salaJson.get("turno").getAsString();
			JsonArray historial = salaJson.getAsJsonArray("historial");

			System.out.println("Sala ID: " + idSala);
			System.out.println("Reservada: " + (reservada ? "Sí" : "No"));
			System.out.println("Profesor Asociado (ID): "
					+ (idProfesorAsociado != 0 ? idProfesorAsociado : "Sin profesor asociado"));
			System.out.println("Turno: " + turno);

			if (historial != null && historial.size() > 0) {
				System.out.println("Historial:");
				for (JsonElement evento : historial) {
					JsonObject eventoSala = evento.getAsJsonObject();
					int idProfesorReserva = eventoSala.get("profesorReserva").getAsInt();
					String turnoReserva = eventoSala.get("turnoReserva").getAsString();
					System.out.println("  - Fue reservada por Profesor (ID): " + idProfesorReserva
							+ ", en el turno de: " + turnoReserva);
				}
			} else {
				System.out.println("Historial: Nunca ha sido reservada");
			}

			System.out.println(); // Línea en blanco entre salas
		}
	}

//////////////////////////////FIN ACCIONES ADMINISTRADOR//////////////////////////////

//////////////////////////////ACCIONES PROFESOR///////////////////////////////////////

	// Ver lista de personas en sala
	private void verListaPersonasSala(int idProfesor) {
		boolean encontradas = false;
		System.out.println("Salas reservadas por el profesor:");
		for (JsonElement element : salas) {
			JsonObject sala = element.getAsJsonObject();
			if (sala.has("idProfesorAsociado") && sala.get("idProfesorAsociado").getAsInt() == idProfesor) {
				encontradas = true;
				int idSala = sala.get("id").getAsInt();
				System.out.println("ID de Sala: " + idSala);
				JsonArray usuariosEnSala = sala.getAsJsonArray("usuariosEnSala");
				if (usuariosEnSala == null || usuariosEnSala.size() == 0) {
					System.out.println("No hay usuarios en esta sala.");
				} else {
					System.out.println("Usuarios en esta sala:");
					for (JsonElement usuarioElement : usuariosEnSala) {
						int idUsuario = usuarioElement.getAsInt();
						JsonObject usuario = obtenerUsuarioPorId(idUsuario);
						if (usuario != null) {
							String nombreUsuario = usuario.get("nombre").getAsString();
							System.out.println("- " + nombreUsuario);
						}
					}
				}
				System.out.println(); // Línea en blanco para separar las salas
			}
		}
		if (!encontradas) {
			System.out.println("El profesor no tiene salas reservadas.");
		}
	}

	// Ver disponibilidad de salas
	private void verDisponibilidadSalas() {
		boolean disponiblesEncontradas = false;
		System.out.println("Salas disponibles:");
		for (JsonElement element : salas) {
			JsonObject sala = element.getAsJsonObject();
			if (!sala.has("reservada") || !sala.get("reservada").getAsBoolean()) {
				disponiblesEncontradas = true;
				int idSala = sala.get("id").getAsInt();
				String turno = sala.get("turno").getAsString();
				System.out.println("ID de Sala: " + idSala + ", Turno: " + turno);
			}
		}
		if (!disponiblesEncontradas) {
			System.out.println("No hay salas disponibles en este momento.");
		}
	}

	// Reservar sala
	private void reservarSala() {
		int idUsuario = getIdUsuarioLogueado();
		if (idUsuario == -1) {
			System.out.println("No se pudo obtener el ID del usuario actual.");
			return;
		}

		// Verificar si el usuario actual ya tiene una sala reservada
		boolean salaReservadaManana = false;
		boolean salaReservadaTarde = false;
		for (JsonElement element : salas) {
			JsonObject sala = element.getAsJsonObject();
			if (sala.has("reservada") && sala.get("reservada").getAsBoolean()
					&& sala.get("idProfesorAsociado").getAsInt() == idUsuario) {
				String turnoReserva = sala.get("turno").getAsString();
				if (turnoReserva.equals("mañana")) {
					salaReservadaManana = true;
				} else if (turnoReserva.equals("tarde")) {
					salaReservadaTarde = true;
				}
			}
		}

		// Verificar si el usuario ya tiene una sala reservada en ambos turnos
		if (salaReservadaManana && salaReservadaTarde) {
			System.out.println("Ya tienes salas reservadas para ambos turnos.");
			return;
		}

		Scanner scanner = new Scanner(System.in);

		// Determinar los turnos disponibles para reserva
		String[] turnosDisponibles;
		if (!salaReservadaManana && !salaReservadaTarde) {
			turnosDisponibles = new String[] { "mañana", "tarde" };
		} else if (!salaReservadaManana) {
			turnosDisponibles = new String[] { "mañana" };
		} else if (!salaReservadaTarde) {
			turnosDisponibles = new String[] { "tarde" };
		} else {
			System.out.println("No puedes reservar más salas.");
			return;
		}

		// Mostrar las salas disponibles para reserva en los turnos correspondientes
		boolean disponiblesEncontradas = false;
		System.out.println("Salas disponibles para reserva:");
		for (JsonElement element : salas) {
			JsonObject sala = element.getAsJsonObject();
			if (!sala.has("reservada") || !sala.get("reservada").getAsBoolean()) {
				String turno = sala.get("turno").getAsString();
				for (String turnoDisponible : turnosDisponibles) {
					if (turno.equals(turnoDisponible)) {
						disponiblesEncontradas = true;
						int idSala = sala.get("id").getAsInt();
						System.out.println("ID de Sala: " + idSala + ", Turno: " + turno);
						break;
					}
				}
			}
		}

		if (!disponiblesEncontradas) {
			System.out.println("No hay salas disponibles para reserva en este momento.");
			return;
		}

		// Solicitar al usuario que ingrese el ID de la sala que desea reservar
		System.out.print("Ingrese el ID de la sala que desea reservar o 0 para salir: ");
		int idSala = scanner.nextInt();

		// Verificar si el usuario desea salir
		if (idSala == 0) {
			System.out.println("Saliendo del menú de reserva de sala.");
			return;
		}

		// Buscar la sala seleccionada por ID
		JsonObject salaReservar = null;
		for (JsonElement element : salas) {
			JsonObject sala = element.getAsJsonObject();
			if (sala.get("id").getAsInt() == idSala) {
				salaReservar = sala;
				break;
			}
		}

		if (salaReservar == null) {
			System.out.println("No se encontró ninguna sala con el ID proporcionado.");
			return;
		}

		// Reservar la sala
		salaReservar.addProperty("reservada", true);
		salaReservar.addProperty("idProfesorAsociado", idUsuario);
		System.out.println("Sala reservada exitosamente.");

		// Guardar los cambios en el JSON
		guardarSalasProfe();
	}

	private void guardarSalasProfe() {
		try {
			FileWriter writer = new FileWriter("salas.json");
			writer.write(new Gson().toJson(salas));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private JsonObject obtenerUsuarioPorId(int idUsuario) {
		for (JsonElement element : usuarios) {
			JsonObject usuario = element.getAsJsonObject();
			if (usuario.get("id").getAsInt() == idUsuario) {
				return usuario;
			}
		}
		return null;
	}

	private int obtenerIdSalaReservada() {
		int idUsuario = getIdUsuarioLogueado(); // Obtener el ID del usuario logueado
		for (JsonElement element : salas) {
			JsonObject sala = element.getAsJsonObject();
			if (sala.get("reservada").getAsBoolean() && sala.get("idProfesorAsociado").getAsInt() == idUsuario) {
				return sala.get("id").getAsInt(); // Devolver el ID de la sala reservada por el usuario logueado
			}
		}
		System.out.println("No se encontró ninguna sala reservada por el usuario.");
		return -1; // Si no se encuentra ninguna sala reservada por el usuario, devolver -1 o algún
					// otro valor que indique que no se encontró ninguna sala.
	}

	public void gestionClases() {
		Scanner scanner = new Scanner(System.in);

		// Mostrar todas las clases asociadas al profesor
		mostrarClasesAsociadas(idUsuarioLogueado);

		// Pedir al usuario que elija una acción
		System.out.println("--- GESTIÓN DE CLASES ---");
		System.out.println("1. Eliminar usuario asociado a una clase");
		System.out.println("2. Cancelar reserva de sala para una clase");
		System.out.println("3. Volver al menú principal");
		System.out.print("Seleccione una opción: ");
		int opcion = scanner.nextInt();

		switch (opcion) {
		case 1:
			eliminarUsuarioAsociado();
			break;
		case 2:
			cancelarReservaSala();
			break;
		case 3:
			return;
		default:
			System.out.println("Opción no válida.");
			break;
		}
	}

	private void mostrarClasesAsociadas(int idProfesor) {
		System.out.println("--- CLASES ASOCIADAS ---");
		for (JsonElement salaElement : salas) {
			JsonObject sala = salaElement.getAsJsonObject();
			if (sala.has("idProfesorAsociado") && sala.get("idProfesorAsociado").getAsInt() == idProfesor) {
				System.out.println("Sala ID: " + sala.get("id").getAsInt());
				System.out.println("Turno: " + sala.get("turno").getAsString());
				System.out.println("Usuarios en Sala: " + sala.get("usuariosEnSala").getAsJsonArray());
				System.out.println();
			}
		}
	}

	private void eliminarUsuarioAsociado() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Ingrese el ID de la sala:");
		int idSala = scanner.nextInt();
		System.out.println("Ingrese el ID del usuario a eliminar:");
		int idUsuario = scanner.nextInt();

		for (JsonElement salaElement : salas) {
			JsonObject sala = salaElement.getAsJsonObject();
			if (sala.get("id").getAsInt() == idSala) {
				JsonArray usuariosEnSala = sala.get("usuariosEnSala").getAsJsonArray();
				for (int i = 0; i < usuariosEnSala.size(); i++) {
					if (usuariosEnSala.get(i).getAsInt() == idUsuario) {
						usuariosEnSala.remove(i);
						System.out.println("Usuario eliminado con éxito.");
						guardarSalasProfe();
						return;
					}
				}
			}
		}
		System.out.println("No se encontró el usuario en la sala especificada.");
	}

	private void cancelarReservaSala() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Ingrese el ID de la sala:");
		int idSala = scanner.nextInt();

		for (JsonElement salaElement : salas) {
			JsonObject sala = salaElement.getAsJsonObject();
			if (sala.get("id").getAsInt() == idSala) {
				sala.remove("idProfesorAsociado");
				sala.remove("usuariosEnSala");
				sala.addProperty("reservada", false);
				System.out.println("Reserva de sala cancelada con éxito.");
				guardarSalasProfe();
				return;
			}
		}
		System.out.println("No se encontró la sala especificada.");
	}
//////////////////////////////FIN ACCIONES PROFESOR/////////////////////////////////

////////////////////////////// ACCIONES USUARIO///////////////////////////////////
	private void verClasesDisponibles() {
		System.out.println("--- CLASES DISPONIBLES ---");
		for (JsonElement element : salas) {
			JsonObject sala = element.getAsJsonObject();
			JsonElement idProfesorAsociadoElement = sala.get("idProfesorAsociado");
			if (idProfesorAsociadoElement != null && !idProfesorAsociadoElement.isJsonNull()) {
				int idProfesorAsociado = idProfesorAsociadoElement.getAsInt();
				System.out.println("ID: " + sala.get("id").getAsInt());
				System.out.println("Reservada: " + sala.get("reservada").getAsBoolean());
				System.out.println("ID Profesor Asociado: " + idProfesorAsociado);
				System.out.println("Turno: " + sala.get("turno").getAsString());
				System.out.println("Usuarios en Sala: " + sala.get("usuariosEnSala").getAsJsonArray().size());
				System.out.println("----------------------");
				return;

			}
		}
	}

	private void inscribirseEnClase() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Ingrese el ID de la clase en la que desea inscribirse:");
		int idSala = scanner.nextInt();

		for (JsonElement element : salas) {
			JsonObject sala = element.getAsJsonObject();
			if (sala.get("id").getAsInt() == idSala) {
				if (!sala.get("reservada").getAsBoolean()) {
					sala.addProperty("reservada", true);
					JsonArray usuariosEnSala = sala.get("usuariosEnSala").getAsJsonArray();
					usuariosEnSala.add(idUsuarioLogueado);
					System.out.println("Inscripción en la clase exitosa.");
					guardarSalas();
					return;
				} else {
					System.out.println("La clase ya está completa.");
					return;
				}
			}
		}
		System.out.println("No se encontró ninguna clase con el ID especificado.");
	}

	private void cancelarInscripcionClase() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Ingrese el ID de la clase de la que desea cancelar su inscripción:");
		int idSala = scanner.nextInt();

		for (JsonElement element : salas) {
			JsonObject sala = element.getAsJsonObject();
			if (sala.get("id").getAsInt() == idSala) {
				JsonArray usuariosEnSala = sala.get("usuariosEnSala").getAsJsonArray();
				JsonPrimitive idUsuario = new JsonPrimitive(idUsuarioLogueado);
				if (usuariosEnSala.contains(idUsuario)) {
					sala.addProperty("reservada", false);
					usuariosEnSala.remove(idUsuario);
					System.out.println("Se ha cancelado su inscripción en la clase.");
					guardarSalas();
					return;
				} else {
					System.out.println("No estás inscrito en esta clase.");
					return;
				}
			}
		}
		System.out.println("No se encontró ninguna clase con el ID especificado.");
	}

	private void verHistorialAsistencia() {
		// Lógica para mostrar el historial de asistencia del usuario a las clases
	}

//////////////////////////////FIN ACCIONES USUARIO//////////////////////////////////

	private boolean validarContraseña(String contraseña) {
		// Validar que la contraseña tenga al menos una mayúscula y un número
		boolean tieneMayuscula = false;
		boolean tieneNumero = false;
		for (char caracter : contraseña.toCharArray()) {
			if (Character.isUpperCase(caracter)) {
				tieneMayuscula = true;
			} else if (Character.isDigit(caracter)) {
				tieneNumero = true;
			}
		}
		return tieneMayuscula && tieneNumero;
	}

}
