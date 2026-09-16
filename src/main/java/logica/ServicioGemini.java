package logica;

import modelo.Categoria;
import modelo.DatosReservaIA;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServicioGemini {

    private static final String MODELO = "gemini-3.6-flash";
    private static final String URL_API =
            "https://generativelanguage.googleapis.com/v1beta/models/"
                    + MODELO + ":generateContent";

    private final HttpClient clienteHttp;

    public ServicioGemini() {
        clienteHttp = HttpClient.newHttpClient();
    }

    public DatosReservaIA interpretarSolicitud(
            String solicitud,
            List<Categoria> categorias,
            String claveApi
    ) throws GeminiException {
        validarEntrada(solicitud, categorias, claveApi);

        String cuerpo = crearCuerpoSolicitud(solicitud, categorias);

        HttpRequest peticion = HttpRequest.newBuilder()
                .uri(URI.create(URL_API))
                .header("Content-Type", "application/json")
                .header("x-goog-api-key", claveApi)
                .POST(HttpRequest.BodyPublishers.ofString(cuerpo))
                .build();

        try {
            HttpResponse<String> respuesta = clienteHttp.send(
                    peticion,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (respuesta.statusCode() < 200 || respuesta.statusCode() >= 300) {
                throw new GeminiException(
                        "Gemini no pudo procesar la solicitud: "
                                + extraerMensajeError(respuesta.body())
                );
            }

            String textoGenerado = extraerTextoGenerado(respuesta.body());

            return interpretarRespuestaIA(textoGenerado, categorias);

        } catch (IOException e) {
            throw new GeminiException(
                    "No fue posible conectar con Gemini. Revise su conexión a internet.",
                    e
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            throw new GeminiException(
                    "La solicitud a Gemini fue interrumpida.",
                    e
            );
        }
    }

    DatosReservaIA interpretarRespuestaIA(
            String respuesta,
            List<Categoria> categorias
    ) throws GeminiException {
        String actividad = null;
        String fechaTexto = null;
        String horaInicioTexto = null;
        String horaFinTexto = null;
        String categoriasTexto = null;

        String[] lineas = respuesta.replace("\r", "").split("\n");

        for (int i = 0; i < lineas.length; i++) {
            String linea = lineas[i].trim();
            int posicionIgual = linea.indexOf('=');

            if (posicionIgual <= 0) {
                continue;
            }

            String campo = linea.substring(0, posicionIgual)
                    .trim()
                    .toUpperCase(Locale.ROOT);
            String valor = linea.substring(posicionIgual + 1).trim();

            if ("ACTIVIDAD".equals(campo)) {
                actividad = valor;
            } else if ("FECHA".equals(campo)) {
                fechaTexto = valor;
            } else if ("HORA_INICIO".equals(campo)) {
                horaInicioTexto = valor;
            } else if ("HORA_FIN".equals(campo)) {
                horaFinTexto = valor;
            } else if ("CATEGORIAS".equals(campo)) {
                categoriasTexto = valor;
            }
        }

        if (actividad == null || actividad.isEmpty()
                || fechaTexto == null || horaInicioTexto == null
                || horaFinTexto == null || categoriasTexto == null) {
            throw new GeminiException(
                    "Gemini devolvió una respuesta incompleta. Intente redactar la solicitud nuevamente."
            );
        }

        try {
            LocalDate fecha = LocalDate.parse(fechaTexto);
            LocalTime horaInicio = LocalTime.parse(horaInicioTexto);
            LocalTime horaFin = LocalTime.parse(horaFinTexto);

            if (!horaFin.isAfter(horaInicio)) {
                throw new GeminiException(
                        "Gemini indicó un horario inválido para la reserva."
                );
            }

            List<String> idsCategorias = obtenerIdsCategorias(
                    categoriasTexto,
                    categorias
            );

            return new DatosReservaIA(
                    actividad,
                    fecha,
                    horaInicio,
                    horaFin,
                    idsCategorias
            );

        } catch (java.time.format.DateTimeParseException e) {
            throw new GeminiException(
                    "Gemini devolvió una fecha u hora con un formato inválido.",
                    e
            );
        }
    }

    private void validarEntrada(
            String solicitud,
            List<Categoria> categorias,
            String claveApi
    ) throws GeminiException {
        if (claveApi == null || claveApi.trim().isEmpty()) {
            throw new GeminiException("Debe configurar una clave de Gemini.");
        }

        if (solicitud == null || solicitud.trim().isEmpty()) {
            throw new GeminiException("Debe escribir una solicitud para Gemini.");
        }

        if (categorias == null || categorias.isEmpty()) {
            throw new GeminiException(
                    "No existen categorías disponibles para completar la reserva."
            );
        }
    }

    private String crearCuerpoSolicitud(
            String solicitud,
            List<Categoria> categorias
    ) {
        String prompt = crearPrompt(solicitud, categorias);

        return "{\"contents\":[{\"parts\":[{\"text\":\""
                + escaparJson(prompt)
                + "\"}]}],\"generationConfig\":{\"temperature\":0.1,"
                + "\"responseMimeType\":\"text/plain\"}}";
    }

    private String crearPrompt(String solicitud, List<Categoria> categorias) {
        StringBuilder categoriasPermitidas = new StringBuilder();

        for (int i = 0; i < categorias.size(); i++) {
            Categoria categoria = categorias.get(i);

            categoriasPermitidas.append(categoria.getId())
                    .append(" | ")
                    .append(categoria.getDescripcion());

            if (i < categorias.size() - 1) {
                categoriasPermitidas.append("\n");
            }
        }

        return "Extrae los datos de una reserva a partir de la solicitud del usuario. "
                + "La fecha actual es " + LocalDate.now() + ". "
                + "Solo puedes usar los identificadores de categoría permitidos. "
                + "No inventes datos. Si la solicitud dice mañana o una fecha relativa, "
                + "resuélvela desde la fecha actual. Responde exclusivamente con estas "
                + "cinco líneas, sin explicaciones ni formato Markdown:\n"
                + "ACTIVIDAD=texto\n"
                + "FECHA=AAAA-MM-DD\n"
                + "HORA_INICIO=HH:MM\n"
                + "HORA_FIN=HH:MM\n"
                + "CATEGORIAS=ID1,ID2\n\n"
                + "Categorías permitidas:\n"
                + categoriasPermitidas
                + "\n\nSolicitud del usuario:\n"
                + solicitud.trim();
    }

    private String extraerTextoGenerado(String cuerpoRespuesta)
            throws GeminiException {
        String texto = extraerCampoJson(cuerpoRespuesta, "text");

        if (texto == null || texto.trim().isEmpty()) {
            throw new GeminiException(
                    "Gemini no devolvió información para completar la reserva."
            );
        }

        return texto;
    }

    private String extraerMensajeError(String cuerpoRespuesta) {
        String mensaje = extraerCampoJson(cuerpoRespuesta, "message");

        if (mensaje == null || mensaje.trim().isEmpty()) {
            return "Error HTTP al llamar al servicio.";
        }

        return mensaje;
    }

    private String extraerCampoJson(String textoJson, String nombreCampo) {
        String expresion = "\\\"" + nombreCampo
                + "\\\"\\s*:\\s*\\\"((?:\\\\.|[^\\\"\\\\])*)\\\"";
        Pattern patron = Pattern.compile(expresion, Pattern.DOTALL);
        Matcher coincidencia = patron.matcher(textoJson);

        if (!coincidencia.find()) {
            return null;
        }

        return desescaparJson(coincidencia.group(1));
    }

    private String desescaparJson(String texto) {
        StringBuilder resultado = new StringBuilder();

        for (int i = 0; i < texto.length(); i++) {
            char caracter = texto.charAt(i);

            if (caracter != '\\' || i + 1 >= texto.length()) {
                resultado.append(caracter);
                continue;
            }

            i++;
            char siguiente = texto.charAt(i);

            if (siguiente == 'n') {
                resultado.append('\n');
            } else if (siguiente == 'r') {
                resultado.append('\r');
            } else if (siguiente == 't') {
                resultado.append('\t');
            } else if (siguiente == 'b') {
                resultado.append('\b');
            } else if (siguiente == 'f') {
                resultado.append('\f');
            } else if (siguiente == 'u' && i + 4 < texto.length()) {
                String hexadecimal = texto.substring(i + 1, i + 5);

                try {
                    resultado.append((char) Integer.parseInt(hexadecimal, 16));
                    i += 4;
                } catch (NumberFormatException e) {
                    resultado.append('u');
                }
            } else {
                resultado.append(siguiente);
            }
        }

        return resultado.toString();
    }

    private String escaparJson(String texto) {
        StringBuilder resultado = new StringBuilder();

        for (int i = 0; i < texto.length(); i++) {
            char caracter = texto.charAt(i);

            if (caracter == '\\') {
                resultado.append("\\\\");
            } else if (caracter == '\"') {
                resultado.append("\\\"");
            } else if (caracter == '\n') {
                resultado.append("\\n");
            } else if (caracter == '\r') {
                resultado.append("\\r");
            } else if (caracter == '\t') {
                resultado.append("\\t");
            } else {
                resultado.append(caracter);
            }
        }

        return resultado.toString();
    }

    private List<String> obtenerIdsCategorias(
            String categoriasTexto,
            List<Categoria> categoriasDisponibles
    ) throws GeminiException {
        List<String> ids = new ArrayList<>();
        String[] idsRespuesta = categoriasTexto.split(",");

        for (int i = 0; i < idsRespuesta.length; i++) {
            String id = idsRespuesta[i].trim();

            if (id.isEmpty()) {
                continue;
            }

            if (!esCategoriaPermitida(id, categoriasDisponibles)) {
                throw new GeminiException(
                        "Gemini indicó una categoría que no está disponible: " + id
                );
            }

            if (!ids.contains(id)) {
                ids.add(id);
            }
        }

        if (ids.isEmpty()) {
            throw new GeminiException(
                    "Gemini no seleccionó una categoría para la reserva."
            );
        }

        return ids;
    }

    private boolean esCategoriaPermitida(
            String id,
            List<Categoria> categoriasDisponibles
    ) {
        for (int i = 0; i < categoriasDisponibles.size(); i++) {
            Categoria categoria = categoriasDisponibles.get(i);

            if (id.equals(categoria.getId())) {
                return true;
            }
        }

        return false;
    }
}
