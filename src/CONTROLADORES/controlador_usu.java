package CONTROLADORES;

import java.io.FileReader; // Importa la clase FileReader para leer archivos.
import java.io.FileWriter; // Importa la clase FileWriter para escribir archivos.
import java.io.IOException; // Importa la clase IOException para manejar excepciones de entrada/salida.
import java.util.Scanner; // Importa la clase Scanner para leer entradas del usuario.

import com.google.gson.Gson; // Importa la clase Gson para manipular JSON.
import com.google.gson.JsonArray; // Importa la clase JsonArray para trabajar con arreglos JSON.
import com.google.gson.JsonElement; // Importa la clase JsonElement para trabajar con elementos JSON.
import com.google.gson.JsonObject; // Importa la clase JsonObject para trabajar con objetos JSON.
import com.google.gson.JsonPrimitive; // Importa la clase JsonPrimitive para trabajar con valores primitivos JSON.

import Vista.Menu; // Importa la clase Menu del paquete Vista.
import entidades.Usuario;

/**
 * Clase controlador_usu para gestionar usuarios y clases.
 */
public class controlador_usu {
	private JsonArray usuarios; // Arreglo JSON para almacenar los usuarios.
	private JsonArray salas; // Arreglo JSON para almacenar las salas.
	private Menu menu; // Objeto Menu para mostrar menús y submenús.
	private int idUsuarioLogueado; // ID del usuario que ha iniciado sesión.

	/**
	 * Constructor de la clase controlador_usu.
	 *
	 * @param idUsu ID del usuario que se loguea.
	 */
	public controlador_usu(int idUsu) {
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
	 * Muestra las clases disponibles.
	 */
	public void verClasesDisponibles() {
		System.out.println("--- CLASES DISPONIBLES ---");
		for (JsonElement element : salas) { // Itera sobre la lista de salas.
			JsonObject sala = element.getAsJsonObject(); // Obtiene el objeto JSON de la sala actual.
			JsonElement idProfesorAsociadoElement = sala.get("idProfesorAsociado"); // Obtiene el ID del profesor
																					// asociado.
			if (idProfesorAsociadoElement != null && !idProfesorAsociadoElement.isJsonNull()) {
				int idProfesorAsociado = idProfesorAsociadoElement.getAsInt(); // Convierte el ID del profesor asociado
																				// a int.
				System.out.println("ID: " + sala.get("id").getAsInt());
				System.out.println("Reservada: " + sala.get("reservada").getAsBoolean());
				System.out.println("ID Profesor Asociado: " + idProfesorAsociado);
				System.out.println("Turno: " + sala.get("turno").getAsString());
				System.out.println("Usuarios en Sala: " + sala.get("usuariosEnSala").getAsJsonArray().size());
				System.out.println("----------------------");
			}
		}
	}

	/**
	 * Permite al usuario inscribirse en una clase.
	 */
	public void inscribirseEnClase() {
	    Scanner scanner = new Scanner(System.in); // Crea un objeto Scanner para leer la entrada del usuario.

	    while (true) {
	        // Mostrar las salas disponibles en las que el usuario no esté inscrito
	        System.out.println("--- Salas Disponibles ---");
	        boolean algunaSalaDisponible = false; // Variable para verificar si hay alguna sala disponible.
	        for (JsonElement element : salas) { // Itera sobre la lista de salas.
	            JsonObject sala = element.getAsJsonObject(); // Obtiene el objeto JSON de la sala actual.
	            JsonElement idProfesorAsociadoElement = sala.get("idProfesorAsociado"); // Obtiene el ID del profesor asociado.
	            if (idProfesorAsociadoElement != null && !idProfesorAsociadoElement.isJsonNull()) {
	                int idSala = sala.get("id").getAsInt(); // Obtiene el ID de la sala.
	                boolean yaInscrito = sala.get("usuariosEnSala").getAsJsonArray()
	                        .contains(new JsonPrimitive(idUsuarioLogueado)); // Verifica si el usuario ya está inscrito en la sala.
	                if (!yaInscrito) {
	                    algunaSalaDisponible = true; // Marca que hay una sala disponible.
	                    System.out.println("- " + idSala);
	                }
	            }
	        }

	        if (!algunaSalaDisponible) {
	            System.out.println("No hay salas disponibles para inscribirse.");
	            return;
	        }

	        // Pedir al usuario que ingrese el ID de la clase en la que desea inscribirse o 'v' para volver
	        System.out.println("Ingrese el ID de la clase en la que desea inscribirse o 'v' para volver:");
	        String input = scanner.nextLine(); // Lee la entrada del usuario.

	        if (input.equalsIgnoreCase("v")) {
	            System.out.println("Volviendo al menú principal...");
	            return;
	        }

	        try {
	            int idSala = Integer.parseInt(input); // Convierte la entrada a un entero.
	            // Verificar si el ID de la sala ingresado por el usuario es válido y realizar la inscripción
	            boolean salaEncontrada = false;
	            for (JsonElement element : salas) { // Itera sobre la lista de salas.
	                JsonObject sala = element.getAsJsonObject(); // Obtiene el objeto JSON de la sala actual.
	                if (sala.get("id").getAsInt() == idSala) {
	                    salaEncontrada = true;
	                    JsonElement idProfesorAsociadoElement = sala.get("idProfesorAsociado"); // Obtiene el ID del profesor asociado.
	                    if (idProfesorAsociadoElement != null && !idProfesorAsociadoElement.isJsonNull()) {
	                        JsonArray usuariosEnSala = sala.getAsJsonArray("usuariosEnSala"); // Obtiene la lista de usuarios en la sala.
	                        int cantidadUsuarios = usuariosEnSala.size(); // Obtiene la cantidad de usuarios en la sala.

	                        // Verificar si hay menos de 10 personas en la sala
	                        if (cantidadUsuarios < 10) {
	                            // Verificar si el usuario ya está inscrito en la sala
	                            boolean yaInscrito = usuariosEnSala.contains(new JsonPrimitive(idUsuarioLogueado)); // Verifica si el usuario ya está inscrito.
	                            if (!yaInscrito) {
	                                // Realizar la inscripción
	                                usuariosEnSala.add(new JsonPrimitive(idUsuarioLogueado)); // Añade el usuario a la lista de usuarios en la sala.
	                                System.out.println("Inscripción en la clase exitosa.");
	                                System.out.println("Cantidad de usuarios inscritos en la sala: " + usuariosEnSala.size());

	                                // Actualizar el historial de asistencia del usuario
	                                for (JsonElement usuarioElement : usuarios) { // Itera sobre la lista de usuarios.
	                                    JsonObject usuario = usuarioElement.getAsJsonObject(); // Obtiene el objeto JSON del usuario actual.
	                                    if (usuario.get("id").getAsInt() == idUsuarioLogueado) {
	                                        JsonArray historialUsu = usuario.getAsJsonArray("historialAsistencia"); // Obtiene el historial de asistencia del usuario.
	                                        historialUsu.add(new JsonPrimitive(idSala)); // Añade el ID de la sala al historial del usuario.
	                                        guardarUsuarios(); // Guarda los cambios en los usuarios.
	                                        break;
	                                    }
	                                }

	                                guardarSalas(); // Guarda los cambios en las salas.
	                                return;
	                            } else {
	                                System.out.println("Ya estás inscrito en esta clase.");
	                                return;
	                            }
	                        } else {
	                            System.out.println("La clase ya está completa.");
	                            return;
	                        }
	                    } else {
	                        System.out.println("La clase no tiene un profesor asociado.");
	                        return;
	                    }
	                }
	            }
	            if (!salaEncontrada) {
	                System.out.println("No se encontró ninguna clase con el ID especificado.");
	            }
	        } catch (NumberFormatException e) {
	            System.out.println("Entrada no válida. Por favor, ingrese un ID válido o 'v' para volver.");
	        }
	    }
	}


	/**
	 * Permite al usuario cancelar su inscripción en una clase.
	 */
	public void cancelarInscripcionClase() {
		Scanner scanner = new Scanner(System.in); // Crea un objeto Scanner para leer la entrada del usuario.

		while (true) {
			// Mostrar las salas en las que el usuario está inscrito
			System.out.println("--- Salas a las que estas inscrito ---");
			boolean algunaSalaDisponible = false; // Variable para verificar si hay alguna sala disponible.
			for (JsonElement element : salas) { // Itera sobre la lista de salas.
				JsonObject sala = element.getAsJsonObject(); // Obtiene el objeto JSON de la sala actual.
				JsonElement idProfesorAsociadoElement = sala.get("idProfesorAsociado"); // Obtiene el ID del profesor
																						// asociado.
				if (idProfesorAsociadoElement != null && !idProfesorAsociadoElement.isJsonNull()) {
					int idSala = sala.get("id").getAsInt(); // Obtiene el ID de la sala.
					boolean yaInscrito = sala.get("usuariosEnSala").getAsJsonArray()
							.contains(new JsonPrimitive(idUsuarioLogueado)); // Verifica si el usuario ya está inscrito
																				// en la sala.
					if (yaInscrito) {
						algunaSalaDisponible = true; // Marca que hay una sala disponible para cancelar inscripción.
						System.out.println("- " + idSala);
					}
				}
			}

			if (!algunaSalaDisponible) {
				System.out.println("No hay salas disponibles para desinscribirse.");
				return;
			}

			System.out.println("Ingrese el ID de la sala de la que desea cancelar su inscripción o 's' para volver:");
			String input = scanner.nextLine(); // Lee la entrada del usuario.

			if (input.equalsIgnoreCase("s")) {
				System.out.println("Volviendo al menú principal...");
				return;
			}

			try {
				int idSala = Integer.parseInt(input); // Convierte la entrada a un entero.
				for (JsonElement element : salas) { // Itera sobre la lista de salas.
					JsonObject sala = element.getAsJsonObject(); // Obtiene el objeto JSON de la sala actual.
					if (sala.get("id").getAsInt() == idSala) {
						JsonArray usuariosEnSala = sala.get("usuariosEnSala").getAsJsonArray(); // Obtiene la lista de
																								// usuarios en la sala.
						JsonPrimitive idUsuario = new JsonPrimitive(idUsuarioLogueado); // Crea un JsonPrimitive con el
																						// ID del usuario logueado.
						if (usuariosEnSala.contains(idUsuario)) {
							sala.addProperty("reservada", false); // Marca la sala como no reservada.
							usuariosEnSala.remove(idUsuario); // Elimina el usuario de la lista de usuarios en la sala.
							System.out.println("Se ha cancelado su inscripción en la sala " + idSala + ".");
							guardarSalas(); // Guarda los cambios en las salas.
							return;
						} else {
							System.out.println("No estás inscrito en esa sala.");
							return;
						}
					}
				}
				System.out.println("No se encontró ninguna sala con el ID especificado.");
			} catch (NumberFormatException e) {
				System.out.println("Entrada no válida. Por favor, ingrese un ID válido o 's' para volver.");
			}
		}
	}

	/**
	 * Obtiene el usuario logueado.
	 *
	 * @return Objeto Usuario con sus datos y historial de asistencia.
	 */
	public Usuario obtenerUsuario() {
		int idUsuarioActual = getIdUsuarioLogueado(); // Obtiene el ID del usuario logueado.

		for (JsonElement element : usuarios) { // Itera sobre la lista de usuarios.
			JsonObject usuarioObject = element.getAsJsonObject(); // Obtiene el objeto JSON del usuario actual.
			int id = usuarioObject.get("id").getAsInt(); // Obtiene el ID del usuario.
			String nombre = usuarioObject.get("nombre").getAsString(); // Obtiene el nombre del usuario.
			String email = usuarioObject.get("email").getAsString(); // Obtiene el email del usuario.
			String password = usuarioObject.get("password").getAsString(); // Obtiene la contraseña del usuario.
			String tipo = usuarioObject.get("tipo").getAsString(); // Obtiene el tipo de usuario.
			JsonArray historialAsistencia = usuarioObject.get("historialAsistencia").getAsJsonArray(); // Obtiene el
																										// historial de
																										// asistencia
																										// del usuario.

			if (id == idUsuarioActual) {
				// Crea un objeto Usuario con su historial de asistencia
				return new Usuario(id, nombre, email, password, tipo, historialAsistencia);
			}
		}

		return null; // Si no se encuentra el usuario, devuelve null.
	}

	/**
	 * Muestra el historial de asistencia del usuario logueado.
	 */
	public void verHistorialAsistencia() {
		Usuario usuario = obtenerUsuario(); // Obtiene el usuario logueado.

		int[] historialAsistencia = usuario.obtenerHistorialAsistencia(); // Obtiene el historial de asistencia del
																			// usuario.

		// Imprime el historial de asistencia
		System.out.println("Historial de Asistencia:");
		for (int asistencia : historialAsistencia) {
			System.out.println(asistencia);
		}
	}

	////////////////////////////// FIN ACCIONES
	////////////////////////////// USUARIO//////////////////////////////////

	/**
	 * Valida que la contraseña tenga al menos una mayúscula y un número.
	 *
	 * @param contraseña La contraseña a validar.
	 * @return true si la contraseña es válida, false en caso contrario.
	 */
	public boolean validarContraseña(String contraseña) {
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
			FileWriter writer = new FileWriter("usuarios.json"); // Crea un escritor de archivos para usuarios.json.
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
	 * Guarda los datos de las salas en el archivo salas.json.
	 */
	public void guardarSalas() {
		try {
			FileWriter writer = new FileWriter("salas.json"); // Crea un escritor de archivos para salas.json.
			writer.write(new Gson().toJson(salas)); // Escribe los datos de las salas en formato JSON.
			writer.close(); // Cierra el escritor de archivos.
		} catch (IOException e) {
			System.out.println("Error al guardar las salas en el archivo JSON.");
			e.printStackTrace(); // Imprime la traza de la excepción en caso de error.
		}
	}
}
