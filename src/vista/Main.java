package vista;
import entidades.Usuario;
import java.util.Scanner;

import controlador.LoginControler;
import controlador.RegistroControler;

public class Main {

    public static void main(String[] args) {
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
                    System.out.println("Gracias por usar el sistema. ¡Hasta luego!");
                    return;
                default:
                    System.out.println("Opción inválida. Inténtalo de nuevo.");
            }
        }
    }

    private static void registrarUsuario(Scanner scanner, RegistroControler registroController) {
        System.out.println("Registro de usuario");
        System.out.print("Ingrese nombre de usuario: ");
        String nombreUsuario = scanner.nextLine();

        // Verificar si el nombre de usuario ya existe en la base de datos
        if (registroController.existeNombreUsuario(nombreUsuario)) {
            System.out.println("El nombre de usuario ya está en uso. Por favor, elija otro.");
            return;
        }

        System.out.print("Ingrese email: ");
        String email = scanner.nextLine();

        // Verificar si el correo electrónico ya existe en la base de datos
        if (registroController.existeCorreoElectronico(email)) {
            System.out.println("El correo electrónico ya está en uso. Por favor, elija otro.");
            return;
        }

        System.out.print("Ingrese contraseña: ");
        String contraseña = scanner.nextLine();
        System.out.print("Ingrese tipo de usuario: ");
        String tipo = scanner.nextLine();

        Usuario usuario = new Usuario(0, nombreUsuario, email, contraseña, tipo);
        registroController.registrarUsuario(usuario);
        System.out.println("Usuario registrado con éxito.");
    }

    private static void iniciarSesion(Scanner scanner, LoginControler loginController) {
        System.out.println("Inicio de sesión");
        System.out.print("Ingrese nombre de usuario: ");
        String nombreUsuario = scanner.nextLine();
        System.out.print("Ingrese contraseña: ");
        String contraseña = scanner.nextLine();

        boolean inicioSesionExitoso = loginController.iniciarSesion(nombreUsuario, contraseña);
        if (inicioSesionExitoso) {
            System.out.println("Inicio de sesión exitoso. ¡Bienvenido!");
        } else {
            System.out.println("Inicio de sesión fallido. Credenciales incorrectas.");
        }
    }
}
