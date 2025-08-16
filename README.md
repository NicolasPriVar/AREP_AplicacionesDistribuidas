# 🌐 Servidor Web en Java

Este proyecto implementa un **servidor HTTP sencillo en Java** que puede:

- Servir contenido estático desde la carpeta `www/`.
- Exponer servicios REST básicos (`/app/hello`, `/app/time`, `/app/echo`).
- Manejar peticiones **GET** y **POST**.
- Responder en formato **JSON**.

Incluye además una interfaz web (HTML + CSS + JS) para probar los servicios desde el navegador.

---

## 🛠️ Explicación de los Métodos Principales

### `main(String[] args)`
- Inicializa el servidor en el puerto **8080**.  
- Acepta conexiones de clientes en un bucle infinito.  
- Llama a `handleClient(socket)` para procesar cada petición.  

---

### `handleClient(Socket socket)`
- Lee la petición HTTP (**método, URI, headers, body**).  
- Identifica si la ruta corresponde a:  
  - `/app/hello`  
  - `/app/time`  
  - `/app/echo`  
  - o bien, un archivo estático (`serveStatic`).  
- Construye y envía la respuesta HTTP adecuada.  

---

### `serveStatic(String path, OutputStream os)`
- Sirve archivos desde la carpeta **`www/`**.  
- Valida que el archivo exista y evita accesos fuera de la ruta (**Path Traversal**).  
- Detecta el tipo MIME (`text/html`, `text/css`, `application/javascript`, `image/png`, etc.).  
- Envía el contenido con encabezados HTTP correctos.  

---

### `guessMime(String path)`
- Determina el **Content-Type** del archivo según su extensión.  

---

## 🎨 Interfaz Web

La carpeta **`www/`** contiene:  
- `index.html`: Página principal.  
- `style.css`: Estilos oscuros con acento en azul.  
- `script.js`: Maneja los botones y realiza las peticiones a los endpoints mediante `fetch`.  

### Ejemplo de uso desde la interfaz:
- Escribir un nombre → presionar **Probar servicio** → mostrar JSON con saludo.  
- Presionar **Consultar hora actual** → mostrar hora en JSON.  
- Escribir mensaje → presionar **Enviar mensaje** → mostrar eco del mensaje.  

---

## 🚀 Tecnologías usadas
- **Java 17+** → Servidor HTTP básico con `ServerSocket`.  
- **HTML5, CSS3, JavaScript (Fetch API)** → Interfaz web.  
- **JSON** → Formato de intercambio de datos.  

---

## 📸 Capturas de pantalla

- Al ejecutar el programa:  
<img width="669" height="52" alt="image" src="https://github.com/user-attachments/assets/4233f44b-6a24-484e-8f13-6d232ca36c76" />

- Al abrir el navegador y entrar a http://localhost:8080  
<img width="1038" height="774" alt="image" src="https://github.com/user-attachments/assets/44d6c678-06da-411b-9b21-4a53213bdccc" />

- Al ejecutar los métodos:  
<img height="352" alt="image" src="https://github.com/user-attachments/assets/60c9111f-0bd1-43a3-ac80-395b0e7e638b" />
<img height="352" alt="image" src="https://github.com/user-attachments/assets/c87c280d-7ea7-432a-9a50-eaefceb4ffcc" />
<img height="352" alt="image" src="https://github.com/user-attachments/assets/0db52106-0232-4960-a91e-8f4a07c42b32" />  

- Si se cambian datos mientras la página se ejecuta:
<img width="369" alt="image" src="https://github.com/user-attachments/assets/d35b92d7-7c90-4217-9470-996731e57e9a" />
<img width="369" alt="image" src="https://github.com/user-attachments/assets/c09cc8a2-33a9-413b-806f-7d1c0c331980" />

