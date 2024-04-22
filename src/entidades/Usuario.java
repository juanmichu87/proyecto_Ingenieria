package entidades;

public class Usuario {
//hola
private int id;
private String nombre;
private String email;
private String password;
private String tipo;

public Usuario(int id, String nombre, String email, String password, String tipo) {
	super();
	this.id = id;
	this.nombre = nombre;
	this.email = email;
	this.password = password;
	this.tipo = tipo;
}

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public String getNombre() {
	return nombre;
}

public void setNombre(String nombre) {
	this.nombre = nombre;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}

public String getTipo() {
	return tipo;
}

public void setTipo(String tipo) {
	this.tipo = tipo;
}

}
