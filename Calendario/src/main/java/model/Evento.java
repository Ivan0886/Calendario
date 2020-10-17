package model;

import java.time.LocalDate;

/**
 * Class Evento
 * @author Iván Moriche
 */
public class Evento 
{
	// Atributos
	private LocalDate dia;
	private String hora, motivo, color;
	// TODO Averiguar como hacer la hora con LocalTime.
	
	/**
	 * Constructor con parametros
	 * @param dia
	 * @param hora
	 * @param motivo
	 * @param color
	 */
	public Evento(LocalDate dia, String hora, String motivo, String color) 
	{
		this.dia = dia;
		this.hora = hora;
		this.motivo = motivo;
		this.color = color;
	}

	/**
	 * Constructor con clave
	 * @param dia
	 */
	public Evento(LocalDate dia) 
	{
		this(dia, null, "", "");
	}

	/**
	 * Método getDia
	 * @return the dia
	 */
	public LocalDate getDia() 
	{
		return dia;
	}

	/**
	 * Método setDia
	 * @param dia the dia to set
	 */
	public void setDia(LocalDate dia) 
	{
		this.dia = dia;
	}

	/**
	 * Método getHora
	 * @return the hora
	 */
	public String getHora() 
	{
		return hora;
	}

	/**
	 * Método setHora
	 * @param hora the hora to set
	 */
	public void setHora(String hora) 
	{
		this.hora = hora;
	}

	/**
	 * Método getMotivo
	 * @return the motivo
	 */
	public String getMotivo() 
	{
		return motivo;
	}

	/**
	 * Método setMotivo
	 * @param motivo the motivo to set
	 */
	public void setMotivo(String motivo) 
	{
		this.motivo = motivo;
	}

	/**
	 * Método getColor
	 * @return the color
	 */
	public String getColor() 
	{
		return color;
	}

	/**
	 * Método setColor
	 * @param color the color to set
	 */
	public void setColor(String color) 
	{
		this.color = color;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() 
	{
		return "Evento [fecha=" + dia + ", hora=" + hora + ", motivo=" + motivo + ", color=" + color + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dia == null) ? 0 : dia.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Evento other = (Evento) obj;
		if (dia == null) {
			if (other.dia != null)
				return false;
		} else if (!dia.equals(other.dia))
			return false;
		return true;
	}
	
}
