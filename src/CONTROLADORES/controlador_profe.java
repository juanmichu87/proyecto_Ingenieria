package CONTROLADORES;

import java.io.FileReader; // Importa la clase FileReader para leer archivos.
import java.io.FileWriter; // Importa la clase FileWriter para escribir archivos.
import java.io.IOException; // Importa la clase IOException para manejar excepciones de entrada/salida.
import java.util.InputMismatchException;
import java.util.Scanner; // Importa la clase Scanner para leer entradas del usuario.

import com.google.gson.Gson; // Importa la clase Gson para manipular JSON.
import com.google.gson.JsonArray; // Importa la clase JsonArray para trabajar con arreglos JSON.
import com.google.gson.JsonElement; // Importa la clase JsonElement para trabajar con elementos JSON.
import com.google.gson.JsonObject; // Importa la clase JsonObject para trabajar con objetos JSON.

import Vista.Menu; // Importa la clase Menu del paquete Vista.

/**
 * Clase controlador_profe para gestionar usuarios y salas por parte de un
 * profesor.
 */
public class controlador_profe {
	private JsonArray usuarios; // Arreglo JSON para almacenar los usuarios.
	private JsonArray salas; // Arreglo JSON para almacenar las salas.
	private Menu menu; // Objeto Menu para mostrar menús y submenús.
	private int idUsuarioLogueado; // ID del usuario que ha iniciado sesión.

	/**
	 * Constructor de la clase controlador_profe.
	 *
	 * @param idUsu ID del usuario que se loguea como profesor.
	 */
	public controlador_profe(int idUsu) {
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
	 * Muestra la lista de personas en las salas reservadas por el profesor.
	 *
	 * @param idProfesor ID del profesor.
	 */
	public void verListaPersonasSala(int idProfesor) {
		boolean encontradas = false; // Variable para verificar si se encontraron salas.
		System.out.println("Salas reservadas por el profesor:");
		for (JsonElement element : salas) { // Itera sobre la lista de salas.
			JsonObject sala = element.getAsJsonObject(); // Obtiene el objeto JSON de la sala actual.
			if (sala.has("idProfesorAsociado") && sala.get("idProfesorAsociado").getAsInt() == idProfesor) { // Verifica
																												// si la
																												// sala
																												// está
																										// asociada
				// al
				// profesor.
				encontradas = true; // Marca que se encontraron salas.
				int idSala = sala.get("id").getAsInt(); // Obtiene el ID de la sala.
				System.out.println("ID de Sala: " + idSala);
				JsonArray usuariosEnSala = sala.getAsJsonArray("usuariosEnSala"); // Obtiene la lista de usuarios en la
																					// sala.
				if (usuariosEnSala == null || usuariosEnSala.size() == 0) {
					System.out.println("No hay usuarios en esta sala.");
				} else {
					System.out.println("Usuarios en esta sala:");
					for (JsonElement usuarioElement : usuariosEnSala) { // Itera sobre la lista de usuarios en la sala.
						int idUsuario = usuarioElement.getAsInt(); // Obtiene el ID del usuario.
						JsonObject usuario = obtenerUsuarioPorId(idUsuario); // Obtiene el objeto JSON del usuario por
																				// ID.
						if (usuario != null) {
							String nombreUsuario = usuario.get("nombre").getAsString(); // Obtiene el nombre del
																						// usuario.
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

	/**
	 * Muestra la disponibilidad de salas.
	 */
	public void verDisponibilidadSalas() {
		boolean disponiblesEncontradas = false; // Variable para verificar si se encontraron salas disponibles.
		System.out.println("Salas disponibles:");
		for (JsonElement element : salas) { // Itera sobre la lista de salas.
			JsonObject sala = element.getAsJsonObject(); // Obtiene el objeto JSON de la sala actual.
			if (!sala.has("reservada") || !sala.get("reservada").getAsBoolean()) { // Verifica si la sala no está
																					// reservada.
				disponiblesEncontradas = true; // Marca que se encontraron salas disponibles.
				int idSala = sala.get("id").getAsInt(); // Obtiene el ID de la sala.
				String turno = sala.get("turno").getAsString(); // Obtiene el turno de la sala.
				System.out.println("ID de Sala: " + idSala + ", Turno: " + turno);
			}
		}
		if (!disponiblesEncontradas) {
			System.out.println("No hay salas disponibles en este momento.");
		}
	}

	/**
	 * Permite al profesor reservar una sala.
	 */
	public void reservarSala() {
	    int idUsuario = getIdUsuarioLogueado(); // Obtiene el ID del usuario logueado.
	    if (idUsuario == -1) {
	        System.out.println("No se pudo obtener el ID del usuario actual.");
	        return;
	    }

	    // Verificar si el usuario actual ya tiene una sala reservada
	    boolean salaReservadaManana = false; // Variable para verificar si hay una sala reservada en el turno de mañana.
	    boolean salaReservadaTarde = false; // Variable para verificar si hay una sala reservada en el turno de tarde.
	    for (JsonElement element : salas) { // Itera sobre la lista de salas.
	        JsonObject sala = element.getAsJsonObject(); // Obtiene el objeto JSON de la sala actual.
	        if (sala.has("reservada") && sala.get("reservada").getAsBoolean()
	                && sala.get("idProfesorAsociado").getAsInt() == idUsuario) { // Verifica si la sala está reservada
	                                                                            // por el usuario actual.
	            String turnoReserva = sala.get("turno").getAsString(); // Obtiene el turno de la sala.
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

	    Scanner scanner = new Scanner(System.in); // Crea un objeto Scanner para leer la entrada del usuario.

	    String turnoReserva = ""; // Define la variable fuera del bloque if para que sea accesible en todo el método.

	    // Determinar los turnos disponibles para reserva
	    String[] turnosDisponibles;
	    if (!salaReservadaManana && !salaReservadaTarde) {
	        turnosDisponibles = new String[] { "mañana", "tarde" };
	        turnoReserva = "mañana"; // Asigna el valor de turnoReserva aquí.
	    } else if (!salaReservadaManana) {
	        turnosDisponibles = new String[] { "mañana" };
	        turnoReserva = "mañana"; // Asigna el valor de turnoReserva aquí.
	    } else if (!salaReservadaTarde) {
	        turnosDisponibles = new String[] { "tarde" };
	        turnoReserva = "tarde"; // Asigna el valor de turnoReserva aquí.
	    } else {
	        System.out.println("No puedes reservar más salas.");
	        return;
	    }

	    // Mostrar las salas disponibles para reserva en los turnos correspondientes
	    boolean disponiblesEncontradas = false; // Variable para verificar si se encontraron salas disponibles para reserva.
	    System.out.println("Salas disponibles para reserva:");
	    for (JsonElement element : salas) { // Itera sobre la lista de salas.
	        JsonObject sala = element.getAsJsonObject(); // Obtiene el objeto JSON de la sala actual.
	        if (!sala.has("reservada") || !sala.get("reservada").getAsBoolean()) { // Verifica si la sala no está reservada.
	            String turno = sala.get("turno").getAsString(); // Obtiene el turno de la sala.
	            for (String turnoDisponible : turnosDisponibles) { // Itera sobre los turnos disponibles.
	                if (turno.equals(turnoDisponible)) {
	                    disponiblesEncontradas = true; // Marca que se encontraron salas disponibles para reserva.
	                    int idSala = sala.get("id").getAsInt(); // Obtiene el ID de la sala.
	                    System.out.println("ID de Sala: " + idSala + ", Turno: " + turno);
	                    break; // Sale del bucle interno.
	                }
	            }
	        }
	    }

	    if (!disponiblesEncontradas) {
	        System.out.println("No hay salas disponibles para reserva en este momento.");
	        return;
	    }

	    int idSala = -1; // Inicializa la variable para almacenar el ID de la sala seleccionada.
	    boolean inputValido = false; // Variable para controlar si la entrada es válida.

	    // Solicitar al usuario que ingrese el ID de la sala que desea reservar
	    while (!inputValido) {
	        System.out.print("Ingrese el ID de la sala que desea reservar o 0 para salir: ");
	        try {
	            idSala = scanner.nextInt(); // Lee el ID de la sala que desea reservar.
	            inputValido = true; // Marca que la entrada es válida.
	        } catch (InputMismatchException e) {
	            System.out.println("Entrada no válida. Por favor, ingrese un número.");
	            scanner.next(); // Limpiar la entrada inválida
	        }
	    }

	    // Verificar si el usuario desea salir
	    if (idSala == 0) {
	        System.out.println("Saliendo del menú de reserva de sala.");
	        return;
	    }

	    // Buscar la sala seleccionada por ID
	    JsonObject salaReservar = null; // Define la variable para almacenar la sala a reservar.
	    for (JsonElement element : salas) { // Itera sobre la lista de salas.
	        JsonObject sala = element.getAsJsonObject(); // Obtiene el objeto JSON de la sala actual.
	        if (sala.get("id").getAsInt() == idSala) { // Verifica si el ID de la sala coincide con el ID proporcionado.
	            salaReservar = sala; // Asigna la sala a reservar.
	            break; // Sale del bucle.
	        }
	    }

	    if (salaReservar == null) {
	        System.out.println("No se encontró ninguna sala con el ID proporcionado.");
	        return;
	    }

	    // Reservar la sala
	    salaReservar.addProperty("reservada", true); // Marca la sala como reservada.
	    salaReservar.addProperty("idProfesorAsociado", idUsuario); // Asocia la sala al profesor.
	    JsonObject registroHistorial = new JsonObject(); // Crea un nuevo objeto JSON para el registro de historial.
	    registroHistorial.addProperty("profesorReserva", idUsuario); // Añade el ID del profesor al historial.
	    registroHistorial.addProperty("turnoReserva", turnoReserva); // Añade el turno de la reserva al historial.
	    salaReservar.getAsJsonArray("historial").add(registroHistorial); // Añade el registro de historial a la sala.

	    System.out.println("Sala reservada exitosamente.");

	    // Guardar los cambios en el JSON
	    guardarSalasProfe(); // Llama al método para guardar las salas.
	}

	

	/**
	 * Guarda los datos de las salas en el archivo salas.json.
	 */
	public void guardarSalasProfe() {
		try {
			FileWriter writer = new FileWriter("Data/salas.json"); // Crea un escritor de archivos para salas.json.
			writer.write(new Gson().toJson(salas)); // Escribe los datos de las salas en formato JSON.
			writer.close(); // Cierra el escritor de archivos.
		} catch (IOException e) {
			e.printStackTrace(); // Imprime la traza de la excepción en caso de error.
		}
	}

	/**
	 * Obtiene un usuario por su ID.
	 *
	 * @param idUsuario ID del usuario.
	 * @return Objeto JSON del usuario o null si no se encuentra.
	 */
	public JsonObject obtenerUsuarioPorId(int idUsuario) {
		for (JsonElement element : usuarios) { // Itera sobre la lista de usuarios.
			JsonObject usuario = element.getAsJsonObject(); // Obtiene el objeto JSON del usuario actual.
			if (usuario.get("id").getAsInt() == idUsuario) {
				return usuario; // Devuelve el objeto JSON del usuario si se encuentra.
			}
		}
		return null; // Devuelve null si no se encuentra el usuario.
	}

	/**
	 * Obtiene el ID de la sala reservada por el usuario logueado.
	 *
	 * @return ID de la sala reservada o -1 si no se encuentra.
	 */
	public int obtenerIdSalaReservada() {
		int idUsuario = getIdUsuarioLogueado(); // Obtener el ID del usuario logueado.
		for (JsonElement element : salas) { // Itera sobre la lista de salas.
			JsonObject sala = element.getAsJsonObject(); // Obtiene el objeto JSON de la sala actual.
			if (sala.get("reservada").getAsBoolean() && sala.get("idProfesorAsociado").getAsInt() == idUsuario) {
				return sala.get("id").getAsInt(); // Devuelve el ID de la sala reservada por el usuario logueado.
			}
		}
		System.out.println("No se encontró ninguna sala reservada por el usuario.");
		return -1; // Si no se encuentra ninguna sala reservada por el usuario, devolver -1 o algún
					// otro valor que indique que no se encontró ninguna sala.
	}

	/**
	 * Permite al profesor gestionar las clases.
	 */
	public void gestionClases() {
	    Scanner scanner = new Scanner(System.in); // Crea un objeto Scanner para leer la entrada del usuario.
	    boolean inputValido = false; // Variable para controlar si la entrada es válida.
	    int opcion = -1; // Inicializa la variable para almacenar la opción seleccionada por el usuario.

	    // Mostrar todas las clases asociadas al profesor
	    mostrarClasesAsociadas(); // Llama al método para mostrar las clases asociadas.

	    // Pedir al usuario que elija una acción
	    System.out.println("--- GESTIÓN DE CLASES ---");
	    System.out.println("1. Eliminar usuario asociado a una clase");
	    System.out.println("2. Cancelar reserva de sala para una clase");
	    System.out.println("3. Volver al menú principal");

	    while (!inputValido) {
	        System.out.print("Seleccione una opción: ");
	        try {
	            opcion = scanner.nextInt(); // Lee la opción seleccionada por el usuario.
	            inputValido = true; // Marca que la entrada es válida.
	        } catch (InputMismatchException e) {
	            System.out.println("Entrada no válida. Por favor, ingrese un número.");
	            scanner.next(); // Limpiar la entrada inválida.
	        }
	    }

	    switch (opcion) {
	    case 1:
	        eliminarUsuarioAsociado(); // Llama al método para eliminar un usuario asociado a una clase.
	        break;
	    case 2:
	        cancelarReservaSala(); // Llama al método para cancelar la reserva de una sala.
	        break;
	    case 3:
	        return; // Vuelve al menú principal.
	    default:
	        System.out.println("Opción no válida.");
	        break;
	    }
	}


	/**
	 * Muestra las clases asociadas al profesor.
	 */
	public void mostrarClasesAsociadas() {
		System.out.println("--- CLASES ASOCIADAS ---");
		for (JsonElement salaElement : salas) { // Itera sobre la lista de salas.
			JsonObject sala = salaElement.getAsJsonObject(); // Obtiene el objeto JSON de la sala actual.
			if (sala.has("idProfesorAsociado") && sala.get("idProfesorAsociado").getAsInt() == idUsuarioLogueado) {
				System.out.println("Sala ID: " + sala.get("id").getAsInt());
				System.out.println("Turno: " + sala.get("turno").getAsString());
				System.out.println("Usuarios en Sala: " + sala.get("usuariosEnSala").getAsJsonArray());
				System.out.println();
			}
		}
	}

	/**
	 * Elimina un usuario asociado a una clase.
	 */
	public void eliminarUsuarioAsociado() {
		Scanner scanner = new Scanner(System.in); // Crea un objeto Scanner para leer la entrada del usuario.
		System.out.println("Ingrese el ID de la sala:");
		int idSala = scanner.nextInt(); // Lee el ID de la sala.
		System.out.println("Ingrese el ID del usuario a eliminar:");
		int idUsuario = scanner.nextInt(); // Lee el ID del usuario a eliminar.

		for (JsonElement salaElement : salas) { // Itera sobre la lista de salas.
			JsonObject sala = salaElement.getAsJsonObject(); // Obtiene el objeto JSON de la sala actual.
			if (sala.get("id").getAsInt() == idSala) {
				JsonArray usuariosEnSala = sala.getAsJsonArray("usuariosEnSala"); // Obtiene la lista de usuarios en la
																					// sala.
				for (int i = 0; i < usuariosEnSala.size(); i++) { // Itera sobre la lista de usuarios en la sala.
					if (usuariosEnSala.get(i).getAsInt() == idUsuario) {
						usuariosEnSala.remove(i); // Elimina el usuario de la lista.
						System.out.println("Usuario eliminado con éxito.");
						guardarSalasProfe(); // Guarda los cambios en el JSON.
						return;
					}
				}
			}
		}
		System.out.println("No se encontró el usuario en la sala especificada.");
	}

	/**
	 * Cancela la reserva de una sala.
	 */
	public void cancelarReservaSala() {
		Scanner scanner = new Scanner(System.in); // Crea un objeto Scanner para leer la entrada del usuario.
		System.out.println("Ingrese el ID de la sala:");
		int idSala = scanner.nextInt(); // Lee el ID de la sala.

		for (JsonElement salaElement : salas) { // Itera sobre la lista de salas.
			JsonObject sala = salaElement.getAsJsonObject(); // Obtiene el objeto JSON de la sala actual.
			if (sala.get("id").getAsInt() == idSala) {
				sala.remove("idProfesorAsociado"); // Elimina el ID del profesor asociado.
				JsonArray usuariosEnSala = new JsonArray(); // Crear un nuevo JsonArray vacío.
				sala.add("usuariosEnSala", usuariosEnSala); // Añade el nuevo JsonArray vacío a la sala.
				sala.addProperty("reservada", false); // Marca la sala como no reservada.
				System.out.println("Reserva de sala cancelada con éxito.");
				guardarSalasProfe(); // Guarda los cambios en el JSON.
				return;
			}
		}
		System.out.println("No se encontró la sala especificada.");
	}

	////////////////////////////// FIN ACCIONES
	////////////////////////////// PROFESOR/////////////////////////////////

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
			FileWriter writer = new FileWriter("Data/usuarios.json"); // Crea un escritor de archivos para usuarios.json.
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
			FileWriter writer = new FileWriter("Data/salas.json"); // Crea un escritor de archivos para salas.json.
			writer.write(new Gson().toJson(salas)); // Escribe los datos de las salas en formato JSON.
			writer.close(); // Cierra el escritor de archivos.
		} catch (IOException e) {
			System.out.println("Error al guardar las salas en el archivo JSON.");
			e.printStackTrace(); // Imprime la traza de la excepción en caso de error.
		}
	}
	
	/**
	 * Exporta los datos de las salas a un archivo CSV.
	 */
	public void exportarSalasACSV() {
		try (FileWriter writer = new FileWriter("Exportaciones/salas.csv")) {
			// Escribir encabezados
			writer.append("ID,Reservada,ID Profesor Asociado,Turno,Historial\n");

			// Escribir datos de salas
			for (JsonElement element : salas) {
				JsonObject sala = element.getAsJsonObject();
				writer.append(sala.get("id").getAsString()).append(",");
				writer.append(sala.get("reservada").getAsString()).append(",");
				JsonElement profesorElement = sala.get("idProfesorAsociado");
				if (profesorElement != null && !profesorElement.isJsonNull()) {
					writer.append(profesorElement.getAsString());
				} else {
					writer.append("N/A");
				}
				writer.append(",");
				writer.append(sala.get("turno").getAsString()).append(",");

				JsonArray historial = sala.getAsJsonArray("historial");
				if (historial != null && historial.size() > 0) {
					for (JsonElement evento : historial) {
						JsonObject eventoSala = evento.getAsJsonObject();
						writer.append(eventoSala.get("profesorReserva").getAsString()).append(":");
						writer.append(eventoSala.get("turnoReserva").getAsString()).append(";");
					}
					writer.append("\n");
				} else {
					writer.append("No hay historial\n");
				}
			}

			System.out.println("Datos de salas exportados exitosamente a salas.csv");
		} catch (IOException e) {
			System.out.println("Error al exportar datos de salas a CSV");
			e.printStackTrace();
		}
	}

}
