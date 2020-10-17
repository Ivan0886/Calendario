# Calendario
Aplicación Java que genera un calendario HTML con las citas incluidas leídas en un JSON.

La aplicación depende de un archivo JSON con la siguiente estructura:

[
  {
    "dia": "2020-01-06",
    "hora": "10:00",
    "motivo": "Reyes",
    "color": "orange"
  },
  .....
  .....
]

También se hace uso de Gradle al que se le añadio la libreria GSON
