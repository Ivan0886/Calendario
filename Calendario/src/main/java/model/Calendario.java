package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * Class Calendario
 * @author Iván Moriche
 * Esta clase cuanta con varios métodos privados, ya que solo son llamados desde el método público construirCalendario
 */
public class Calendario 
{
	// Atributos
	private List <Evento> dias;
	
	/**
	 * Contructor por defecto
	 */
	public Calendario() 
	{
		this.dias = new ArrayList<Evento>();
	}
	
	/**
	 * Método getDias
	 * @return the dias
	 */
	public List<Evento> getDias() 
	{
		return dias;
	}
	
	/**
	 * Método setDias
	 * @param dias the calendario to set
	 */
	public void setDias(List<Evento> dias) 
	{
		this.dias = dias;
	}
	
	/**
	 * Método construirCalendario
	 * @param fichero
	 */
	@SuppressWarnings("serial")
	public void construirCalendario(String fichero) 
	{
		BufferedReader br = null;
		Type type = new TypeToken<List<Evento>>(){}.getType();
		List<Evento> eventos;
		
		// Deserializador que permite transformar fechas JSON en fechas JAVA
		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>()
		{
		    @Override
		    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
		            throws JsonParseException 
		    {
		        return LocalDate.parse(json.getAsString());
		    }
		}).create();
		
		try 
		{
			br = new BufferedReader(new FileReader(fichero));
		} 
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}

		// Se crea una lista con todos los eventos del JSON
		eventos = gson.fromJson(br, type); 
		
		crearListaCalendario(eventos);
	}
	
	/**
	 * Método crearListaCalendario
	 * @param eventos
	 */
	private void crearListaCalendario(List<Evento> eventos) 
	{	
		LocalDate fechaContador;
		
		/* 
		 * Se añaden fechas mientras que la fechaContador sea menor o igual a 31 de diciembre del año del último evento
		 * Se inicia fechaContador a 1 de enero en el mismo año que el primer evento.
		 */
		for(fechaContador = LocalDate.of(eventos.get(0).getDia().getYear(), 01, 01); 
				fechaContador.isBefore(LocalDate.of(eventos.get(eventos.size() - 1).getDia().getYear(), 12, 31).plusDays(1)); 
				fechaContador = fechaContador.plusDays(1))	
		{
			Evento e = new Evento(fechaContador);
			int posicion = eventos.indexOf(e);
			
			// Si existe la fecha en la lista de eventos, se añade el Evento. Si no, simplemente una fecha
			this.dias.add(posicion < 0 ? e : eventos.get(posicion));
		}
		
		// Se crea el CSS que es común a todos los HTML
		escribirFicheroCSS();
		
		// Se crea el HTML. Se le pasa la posicion desde donde va a empezar a escribir
		escribirFicheroHTML(0);
	}
	
	/**
	 * Método escribirFicheroHTML
	 * @param posicion
	 * Por cada año, se crea un HTML
	 */
	private void escribirFicheroHTML(int posicion) 
	{
		int i = posicion, anio = this.dias.get(i).getDia().getYear();
		
		try 
		{
			Writer ouW = new OutputStreamWriter(new FileOutputStream("anio" + anio + ".html"), "utf-8");
			
			// Se escribe el head del HTML
			ouW.write(escribirCabeceraHTML(anio));
			
			// Se pintan los eventos del año en cuestión
			for(; i < this.dias.size() && this.dias.get(i).getDia().getYear() == anio; i++)
			{
				// Si la fecha coincide con la de hoy, se le asigna una clase css especial
				String clase = (this.dias.get(i).getDia().equals(LocalDate.now())) ? 
						"hoy" : "" + this.dias.get(i).getDia().getMonth();
				
				// Se escribe la fecha
				ouW.write("<div class=\"diasEscritos dias\">"
						+ "<div class=\"mes " + clase + "\">"
						+ "📅<b>" + this.dias.get(i).getDia().getMonth().getDisplayName(TextStyle.FULL, new Locale("es","ES")).toUpperCase() + "</b>"
						+ "<br />\r\n"
						+ this.dias.get(i).getDia().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es","ES")).toUpperCase()
						+ "<br />\r\n"
						+ this.dias.get(i).getDia() + "</div>\r\n");
				
				// Si la hora no es null, significa que tiene algun evento, por lo tanto se pintan esos datos
				if(this.dias.get(i).getHora() != null)
				{
					ouW.write("<div style=\"background-color: " + this.dias.get(i).getColor() + ";\">⏰" 
							+ this.dias.get(i).getHora() + "</div>\r\n" 
							+ "<div>" + this.dias.get(i).getMotivo() + "</div>\r\n");
				}
				ouW.write("</div>");
			}
			
			// Se escribe el fin del body en el HTML
			ouW.write(escribirFinHTML(anio));
			
			ouW.close();
			
			// Si quedan más años por pintar, se hace una llamada recursiva pasando la posición por la que va
			if(i < this.dias.size() && this.dias.get(i).getDia().getYear() != anio) escribirFicheroHTML(i);
		}	
		catch(IOException e) 
		{
			System.err.println(e);
		}
	}
	
	/**
	 * Método escribirCabeceraHTML
	 * @return the String of HTML
	 */
	private String escribirCabeceraHTML(int anio) 
	{	
		int divsVacios = LocalDate.of(anio, 01, 01).getDayOfWeek().getValue();
		
		String comienzoHTML = "<!DOCTYPE html>\r\n" 
				+ "<html lang=\"es\">\r\n" 
				+ "		<head>\r\n"
				+ "		<meta charset=\"UTF-8\">\r\n"
				+ "		<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
				+ "		<title>Calendario</title>\r\n"
				+ "		<link rel=\"stylesheet\" href=\".\\style.css\">"
				+ "		<link href=\"https://fonts.googleapis.com/css2?family=Sansita+Swashed:wght@300&display=swap\" rel=\"stylesheet\">"
				+ "		<link href=\"https://fonts.googleapis.com/css2?family=Josefin+Sans:ital,wght@1,300&display=swap\" rel=\"stylesheet\">"
				+ "	</head>\r\n" 
				+ "	<body>\r\n"
				+ "		<h1>Año " + anio + "</h1>";
		
		// Bucle que escribe divs vacios para que el resto de lineas pintadas empiecen por Lunes
		for(int i = 1; i < divsVacios; i++) {
			comienzoHTML += "<div class=\"dias\"></div>";
		}
		
		return comienzoHTML;
	}
	
	/**
	 * Método escribirFinHTML
	 * @param anio
	 * @return the String of HTML
	 */
	private String escribirFinHTML(int anio) 
	{
		File anioSiguiente = new File("anio" + (anio + 1) + ".html");
		File anioAnterior = new File("anio" + (anio - 1) + ".html");
		String html = "<br />";
		
		// Si existen los archivos, se añade un enlace a ellos en el HTML
		if (anioAnterior.exists()) html += "<a href=\".\\" + anioAnterior + "\"><button>Año anterior</button></a>\r\n";
		
		if (anioSiguiente.exists()) html += "<a href=\".\\" + anioSiguiente + "\"><button>Año siguiente</button></a>\r\n";
		
		html += "</body>\r\n" + "</html>";
		return html;
	}
	
	/**
	 * Método escribirFicheroCSS
	 */
	private void escribirFicheroCSS() 
	{
		try 
		{
			Writer ouW = new OutputStreamWriter(new FileOutputStream("style.css"), "utf-8");
			ouW.write("body {\r\n"
					+ "		font-family: 'Sansita Swashed', cursive;\r\n"
					+ "}\r\n"
					+ ".diasEscritos {\r\n"
					+ "		border-color: black !important;\r\n"
					+ "}\r\n"
					+ ".dias {\r\n" 
					+ "		display: inline-block;\r\n"
					+ "		vertical-align: top;\r\n"
					+ "		margin: 0.5%;\r\n"  
					+ "		width: 12%;\r\n"
					+ "		border-width: 2px;\r\n"
					+ "		border-style: solid;\r\n"
					+ "		border-color: white;\r\n"
					+ "		border-radius: 5px;\r\n"
					+ "}\r\n"
					+ ".JANUARY {\r\n" 
					+ "		background-color: blue;\r\n"
					+ "}\r\n"
					+ ".FEBRUARY {\r\n" 
					+ "		background-color: green;\r\n"
					+ "		color: white;\r\n"
					+ "}\r\n"
					+ ".MARCH {\r\n" 
					+ "		background-color: yellow;\r\n"
					+ "}\r\n"
					+ ".APRIL {\r\n" 
					+ "		background-color: pink;\r\n"
					+ "}\r\n"
					+ ".MAY {\r\n" 
					+ "		background-color: brown;\r\n"
					+ "		color: white;\r\n"
					+ "}\r\n"
					+ ".JUNE {\r\n" 
					+ "		background-color: red;\r\n"
					+ "}\r\n"
					+ ".JULY {\r\n" 
					+ "		background-color: orange;\r\n"
					+ "		padding: 4%;\r\n"
					+ "}\r\n"
					+ ".AUGUST {\r\n" 
					+ "		background-color: darksalmon;\r\n"
					+ "}\r\n"
					+ ".SEPTEMBER {\r\n" 
					+ "		background-color: magenta;\r\n"
					+ "}\r\n"
					+ ".OCTOBER {\r\n" 
					+ "		background-color: aqua;\r\n"
					+ "}\r\n"
					+ ".NOVEMBER {\r\n" 
					+ "		background-color: olive;\r\n"
					+ "}\r\n"
					+ ".DECEMBER {\r\n" 
					+ "		background-color: maroon;\r\n"
					+ "		color: white;\r\n"
					+ "}\r\n"
					+ ".hoy {\r\n" 
					+ "		background-color: gray;\r\n"
					+ "		color: white;\r\n"
					+ "}\r\n"
					+ ".mes {\r\n" 
					+ "		padding: 4%;\r\n"
					+ "		font-family: 'Josefin Sans', sans-serif;"
					+ "}\r\n");
			ouW.close();
		}	
		catch(IOException e) 
		{
			System.err.println(e);
		}
	}
		
}
