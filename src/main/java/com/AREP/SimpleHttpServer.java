package com.AREP;


import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SimpleHttpServer {
    private static final int PORT = 8080;
    private static final Path WWW_ROOT = Paths.get("www").toAbsolutePath().normalize();

    public static void main(String[] args) {
        System.out.println("Sirviendo desde: " + WWW_ROOT);
        System.out.println("Escuchando en http://localhost:" + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    handleClient(socket);
                } catch (Exception e) {
                    System.err.println("Error atendiendo cliente: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("No se pudo abrir el puerto " + PORT + ": " + e.getMessage());
        }
    }

    private static void handleClient(Socket socket) throws Exception {
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.US_ASCII));

        String requestLine = in.readLine();
        if (requestLine == null || requestLine.isEmpty()) return;
        String[] parts = requestLine.split(" ");
        if (parts.length < 3) return;
        String method = parts[0];
        URI uri = new URI(parts[1]);

        while (true) {
            String line = in.readLine();
            if (line == null || line.isEmpty()) break;
        }

        if (uri.getPath().startsWith("/app/hello")) {
            String name = "desconocido";
            if (uri.getQuery() != null && uri.getQuery().contains("=")) {
                name = uri.getQuery().split("=")[1];
            }
            String json = "{\"mensaje\":\"Hola " + name + "\"}";
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: application/json\r\n" +
                    "Content-Length: " + json.getBytes().length + "\r\n\r\n" +
                    json;
            os.write(response.getBytes(StandardCharsets.UTF_8));
        } else if (uri.getPath().startsWith("/app/time")) {
            // --------- GET /app/time
            String now = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            String json = "{\"hora\":\"" + now + "\"}";
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: application/json\r\n" +
                    "Content-Length: " + json.getBytes().length + "\r\n\r\n" +
                    json;
            os.write(response.getBytes(StandardCharsets.UTF_8));

        } else if (uri.getPath().startsWith("/app/echo")) {
            // --------- POST /app/echo
            StringBuilder body = new StringBuilder();
            // ⚡️ Importante: leer el body del POST
            while (in.ready()) {
                body.append((char) in.read());
            }

            String msg = body.toString().trim();
            if (msg.isEmpty()) msg = "Mensaje vacío";

            String json = "{\"mensaje\":\"" + msg + "\"}";
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: application/json\r\n" +
                    "Content-Length: " + json.getBytes().length + "\r\n\r\n" +
                    json;
            os.write(response.getBytes(StandardCharsets.UTF_8));
        } else {
            serveStatic(uri.getPath(), os);
        }

        os.flush();
        socket.close();
    }

    private static void serveStatic(String path, OutputStream os) throws IOException {
        if (path.equals("/")) path = "/index.html";
        Path file = WWW_ROOT.resolve("." + path).normalize();

        if (!file.startsWith(WWW_ROOT) || !Files.exists(file)) {
            String notFound = "HTTP/1.1 404 Not Found\r\n\r\nArchivo no encontrado";
            os.write(notFound.getBytes(StandardCharsets.UTF_8));
            return;
        }

        String mime = guessMime(path);
        byte[] bytes = Files.readAllBytes(file);
        String headers = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: " + mime + "\r\n" +
                "Content-Length: " + bytes.length + "\r\n\r\n";
        os.write(headers.getBytes(StandardCharsets.US_ASCII));
        os.write(bytes);
    }

    private static String guessMime(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg")) return "image/jpeg";
        return "text/plain";
    }
}
