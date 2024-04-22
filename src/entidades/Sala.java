package entidades;

public class Sala {
    private int id;
    private boolean reservada;
    private int idProfesorAsociado; // ID del profesor asociado a la sala
    private String turno;

    public Sala(int id, boolean reservada, int idProfesorAsociado, String turno) {
        this.id = id;
        this.reservada = reservada;
        this.idProfesorAsociado = idProfesorAsociado;
        this.turno = turno;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isReservada() {
		return reservada;
	}

	public void setReservada(boolean reservada) {
		this.reservada = reservada;
	}

	public int getIdProfesorAsociado() {
		return idProfesorAsociado;
	}

	public void setIdProfesorAsociado(int idProfesorAsociado) {
		this.idProfesorAsociado = idProfesorAsociado;
	}

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

    
}
