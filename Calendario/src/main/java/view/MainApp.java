package view;

import model.Calendario;

/**
 * Class MainApp
 * @author Iván Moriche
 */
public class MainApp 
{

	/**
	 * Método main
	 * @param args
	 */
	public static void main(String[] args)
	{	
		// Constantes
		final String FICHERO = "Eventos.json";
		
		// Variables
		Calendario calendario = new Calendario();
		
		/* 
		 * Se leen los eventos del archivo JSON. Como parametro se le pasa el nombre del archivo.
		 * Esta función también se encarga de pintar el HTML y CSS correspondientes
		*/
		calendario.construirCalendario(FICHERO);
	}

}
