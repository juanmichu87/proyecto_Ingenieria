package proyecto_ingenieria;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Menu menu = new Menu();
		Scanner scanner = new Scanner(System.in);

		boolean salir = false;
		while (!salir) {
			System.out.println("--- BIENVENIDO ---");
			System.out.println("1. Iniciar Sesión");
			System.out.println("2. Registrarse");
			System.out.println("3. Salir");
			System.out.print("Seleccione una opción: ");
			int opcion = scanner.nextInt();

			switch (opcion) {
			case 1:
				boolean iniciarSesion = false;
				while (!iniciarSesion) {
					System.out.print("Email: ");
					String email = scanner.next();
					System.out.print("Password: ");
					String password = scanner.next();
					if (menu.iniciarSesion(email, password)) {
						iniciarSesion = true;
						// Obtener el ID del usuario actual después de iniciar sesión
						int idUsuario = menu.getIdUsuarioLogueado();
						System.out.println("Inicio de sesión exitoso. ID de usuario: " + idUsuario);
					} else {
						System.out.println("¿Desea intentarlo de nuevo? (s/n)");
						String respuesta = scanner.next();
						if (!respuesta.equalsIgnoreCase("s")) {
							iniciarSesion = true;
						}
					}
				}
				break;
			case 2:
				boolean registrarUsuario = false;
				while (!registrarUsuario) {
					menu.registrarUsuario();
					System.out.println("¿Desea intentarlo de nuevo? (s/n)");
					String respuesta = scanner.next();
					if (!respuesta.equalsIgnoreCase("s")) {
						registrarUsuario = true;
					}
				}
				break;
			case 3:
				System.out.println("Gracias por usar nuestra aplicación. ¡Hasta luego!");
				salir = true;
				break;
			default:
				System.out.println("Opción no válida. Por favor, seleccione una opción válida.");
				break;
			}
		}
	}
}
