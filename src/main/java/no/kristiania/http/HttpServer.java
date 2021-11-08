package no.kristiania.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.*;



public class HttpServer {

    private final ServerSocket serverSocket;
    private final HashMap<String, HttpController> controllers = new HashMap<>();


    public HttpServer(int serverPort) throws IOException {
        serverSocket = new ServerSocket(serverPort);

        new Thread(this::serveClients).start();
    }
    // -- Error handling --  //
    private void serveClients() {
        try {
            while (true) {
                serveClient();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void serveClient() throws IOException, SQLException {
        Socket clientSocket = serverSocket.accept();

        HttpMessage httpMessage = new HttpMessage(clientSocket);
        String[] requestLine = httpMessage.startLine.split(" ");
        String requestTarget = requestLine[1];

        int questionPos = requestTarget.indexOf('?');
        String targetFile;
        String query = null;
        if (questionPos != -1) {
            targetFile = requestTarget.substring(0, questionPos);
            query = requestTarget.substring(questionPos+1);
        } else {
            targetFile = requestTarget;
        }

        if(controllers.containsKey(targetFile)) {
            HttpMessage response = controllers.get(targetFile).handle(httpMessage);
            response.write(clientSocket);

        } else {

            // Add to controller --> //
            InputStream resourceFile = getClass().getResourceAsStream(targetFile);
            if (resourceFile != null) {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                resourceFile.transferTo(buffer);
                String responseText = buffer.toString();


                String contentType = "text/plain";
                if (requestTarget.endsWith(".html")) {
                    contentType = "text/html";
                } else if (requestTarget.endsWith(".css")) {
                    contentType = "text/css";
                }
                Response(clientSocket, responseText, contentType);
                return;

                // <-- Add to Controller //
            }

            String responseText = "File not found: " + requestTarget;

            String response = "HTTP/1.1 404 Not found\r\n" +
                    "Content-Length: " + responseText.length() + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    responseText;
            clientSocket.getOutputStream().write(response.getBytes());
        }
    }

    private void Response(Socket clientSocket, String responseText, String contentType) throws IOException {
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: " + responseText.length() + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                responseText;
        clientSocket.getOutputStream().write(response.getBytes());
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

   public void addController(String path, HttpController controller) {
       controllers.put(path, controller);
   }

}