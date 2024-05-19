package Vista;

import java.util.InputMismatchException;
import java.util.Scanner; // Importa la clase Scanner para leer entradas del usuario.

/**
 * Clase Main para iniciar la aplicación.
 */
public class Main {
    /**
     * Método principal para ejecutar la aplicación.
     *
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        Menu menu = new Menu(); // Crea un nuevo objeto Menu para gestionar el menú principal y submenús.
        Scanner scanner = new Scanner(System.in); // Crea un objeto Scanner para leer la entrada del usuario.

        boolean salir = false; // Variable para controlar el bucle principal del menú.
        while (!salir) { // Bucle principal del menú.
            try {
                System.out.println("--- BIENVENIDO ---");
                System.out.println("1. Iniciar Sesión");
                System.out.println("2. Registrarse");
                System.out.println("3. Salir");
                System.out.print("Seleccione una opción: ");
                int opcion = scanner.nextInt(); // Lee la opción seleccionada por el usuario.

                switch (opcion) {
                    case 1:
                        boolean iniciarSesion = false; // Variable para controlar el bucle de inicio de sesión.
                        while (!iniciarSesion) { // Bucle de inicio de sesión.
                            System.out.print("Email: ");
                            String email = scanner.next(); // Lee el email ingresado por el usuario.
                            System.out.print("Password: ");
                            String password = scanner.next(); // Lee la contraseña ingresada por el usuario.
                            if (menu.iniciarSesion(email, password)) { // Verifica las credenciales del usuario.
                                iniciarSesion = true; // Finaliza el bucle de inicio de sesión si las credenciales son correctas.
                                // Obtener el ID del usuario actual después de iniciar sesión
                                int idUsuario = menu.getIdUsuarioLogueado(); // Obtiene el ID del usuario logueado.
                                System.out.println("Inicio de sesión exitoso. ID de usuario: " + idUsuario);
                            } else {
                                System.out.println("Credenciales incorrectas. ¿Desea intentarlo de nuevo? (s/n)");
                                String respuesta = scanner.next(); // Lee la respuesta del usuario.
                                if (!respuesta.equalsIgnoreCase("s")) { // Finaliza el bucle de inicio de sesión si el usuario no desea intentarlo de nuevo.
                                    iniciarSesion = true;
                                }
                            }
                        }
                        break;
                    case 2:
                        boolean registrarUsuario = false; // Variable para controlar el bucle de registro de usuario.
                        while (!registrarUsuario) { // Bucle de registro de usuario.
                            menu.registrarUsuario(); // Llama al método para registrar un nuevo usuario.
                            System.out.println("¿Desea intentarlo de nuevo? (s/n)");
                            String respuesta = scanner.next(); // Lee la respuesta del usuario.
                            if (!respuesta.equalsIgnoreCase("s")) { // Finaliza el bucle de registro de usuario si el usuario no desea intentarlo de nuevo.
                                registrarUsuario = true;
                            }
                        }
                        break;
                    case 3:
                        System.out.println("Gracias por usar nuestra aplicación. ¡Hasta luego!");
                        salir = true; // Finaliza el bucle principal del menú.
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, seleccione una opción válida.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número.");
                scanner.nextLine(); // Limpia el buffer del scanner.
            } catch (Exception e) {
                System.out.println("Ocurrió un error inesperado: " + e.getMessage());
                e.printStackTrace(); // Imprime la traza de la excepción para el desarrollador.
                scanner.nextLine(); // Limpia el buffer del scanner.
            }
        }
    }
}
