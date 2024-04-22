package vista;

import java.util.Scanner;

import controlador.LoginControler;
import controlador.RegistroControler;
import entidades.Usuario;

public class Menu {
	
	private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_INTENTOS = 3;

	public void MenuInicial() {
		Scanner scanner = new Scanner(System.in);
        RegistroControler registroController = new RegistroControler();
        LoginControler loginController = new LoginControler();
        
       

        while (true) {
            System.out.println("1. Registrarse");
            System.out.println("2. Iniciar sesión");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir nueva línea después de nextInt()

            switch (opcion) {
                case 1:
                    registrarUsuario(scanner, registroController);
                    break;
                case 2:
                    iniciarSesion(scanner, loginController);
                    break;
                case 3:
                    System.out.println("Gracias por usar nuestra aplicación. ¡Hasta luego!");
                    return;
                default:
                    System.out.println("Opción inválida. Inténtalo de nuevo.");
            }
        }
	}
	
	public static void mostrarSubMenu(String tipoUsuario) {
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
			case "USUARIO BASICO":
				System.out.println("[USUARIO BASICO]");
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
					//gestionarUsuarios();
					break;
				case 4:
					//gestionSalas();
					break;
				// Agregar casos para otras opciones del administrador si es necesario
				default:
					// Lógica para otras opciones del administrador
					break;
				}
				break;
			case "PROFESOR":
				// Lógica para el menú del Profesor
				break;
			case "USUARIO":
				// Lógica para el menú del Usuario
				break;
			}

		} while (opcion != 5);

		// Si se selecciona la opción de cerrar sesión, no hacer nada y salir del método
	}
	
	private static void registrarUsuario(Scanner scanner, RegistroControler registroController) {
	    boolean registroExitoso = false;

	    while (!registroExitoso) {
	        System.out.println("Registro de usuario");
	        System.out.print("Ingrese nombre de usuario: ");
	        String nombreUsuario = scanner.nextLine();

	        if (registroController.existeNombreUsuario(nombreUsuario)) {
	            System.out.println("El nombre de usuario ya está en uso. Por favor, elija otro.");
	            continue; // Vuelve a solicitar el nombre de usuario
	        }

	        System.out.print("Ingrese email: ");
	        String email = scanner.nextLine();
	        if (!email.matches(EMAIL_REGEX)) {
	            System.out.println("Formato de correo electrónico inválido. Por favor, ingrese un correo válido.");
	            continue; // Vuelve a solicitar el email
	        }

	        if (registroController.existeCorreoElectronico(email)) {
	            System.out.println("El correo electrónico ya está en uso. Por favor, elija otro.");
	            continue; // Vuelve a solicitar el email
	        }

	        System.out.print("Ingrese contraseña: ");
	        String contraseña = scanner.nextLine();
	        if (!isPasswordSecure(contraseña)) {
	            System.out.println("La contraseña no es segura. Debe tener al menos 8 caracteres, incluyendo una letra mayúscula, una minúscula, un número y un símbolo especial.");
	            continue; // Vuelve a solicitar la contraseña
	        }

	        String tipo = "USUARIO BASICO";
	        Usuario usuario = new Usuario(0, nombreUsuario, email, contraseña, tipo);
	        registroExitoso = registroController.registrarUsuario(usuario); // Modificado

	        if (registroExitoso) {
	            System.out.println("Usuario registrado con éxito.");
	        } else {
	            System.out.println("Error al registrar usuario. Por favor, inténtelo de nuevo.");
	        }
	    }
	}


    private static boolean isPasswordSecure(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH) return false;
        if (!password.matches(".*[a-z].*")) return false; // check for lowercase
        if (!password.matches(".*[A-Z].*")) return false; // check for uppercase
        if (!password.matches(".*[0-9].*")) return false; // check for numeric
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) return false;
        return true;
    }

    private static void iniciarSesion(Scanner scanner, LoginControler loginController) {
        System.out.println("Inicio de sesión");

        int intentos = 0;
        while (intentos < MAX_INTENTOS) {
            System.out.print("Ingrese nombre de usuario o correo electrónico: ");
            String nombreUsuarioOEmail = scanner.nextLine();
            System.out.print("Ingrese contraseña: ");
            String contraseña = scanner.nextLine();

            boolean inicioSesionExitoso = loginController.iniciarSesion(nombreUsuarioOEmail, contraseña);
            if (inicioSesionExitoso) {
                System.out.println("Inicio de sesión exitoso. ¡Bienvenido!");
                Usuario user = loginController.getUsuarioLogueado();
                mostrarSubMenu(user.getTipo());
                return;
            } else {
                intentos++;
                System.out.println("Inicio de sesión fallido. Credenciales incorrectas.");
                if (intentos < MAX_INTENTOS) {
                    System.out.println("Por favor, inténtelo de nuevo.");
                }
            }
        }

        System.out.println("Has alcanzado el máximo número de intentos. Volviendo al menú principal.");
    }
}
