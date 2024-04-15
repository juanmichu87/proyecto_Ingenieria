package vista;
import entidades.Usuario;
import java.util.Scanner;

import controlador.LoginControler;
import controlador.RegistroControler;

public class Main {
	
	private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final int MIN_PASSWORD_LENGTH = 8;
    
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

        if (registroController.existeNombreUsuario(nombreUsuario)) {
            System.out.println("El nombre de usuario ya está en uso. Por favor, elija otro.");
            return;
        }

        System.out.print("Ingrese email: ");
        String email = scanner.nextLine();
        if (!email.matches(EMAIL_REGEX)) {
            System.out.println("Formato de correo electrónico inválido. Por favor, ingrese un correo válido.");
            return;
        }

        if (registroController.existeCorreoElectronico(email)) {
            System.out.println("El correo electrónico ya está en uso. Por favor, elija otro.");
            return;
        }

        System.out.print("Ingrese contraseña: ");
        String contraseña = scanner.nextLine();
        if (!isPasswordSecure(contraseña)) {
            System.out.println("La contraseña no es segura. Debe tener al menos 8 caracteres, incluyendo una letra mayúscula, una minúscula, un número y un símbolo especial.");
            return;
        }

        String tipo = "USUARIO BASICO";
        Usuario usuario = new Usuario(0, nombreUsuario, email, contraseña, tipo);
        registroController.registrarUsuario(usuario);
        System.out.println("Usuario registrado con éxito.");
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
        System.out.print("Ingrese nombre de usuario o correo electrónico: ");
        String nombreUsuarioOEmail = scanner.nextLine();
        System.out.print("Ingrese contraseña: ");
        String contraseña = scanner.nextLine();

        boolean inicioSesionExitoso = loginController.iniciarSesion(nombreUsuarioOEmail, contraseña);
        if (inicioSesionExitoso) {
            System.out.println("Inicio de sesión exitoso. ¡Bienvenido!");
        } else {
            System.out.println("Inicio de sesión fallido. Credenciales incorrectas.");
        }
    }
}
