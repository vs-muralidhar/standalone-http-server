
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class StandaloneHttpServer {

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(6880), 0);
        server.createContext("/api/hello", (exchange -> {

            if ("GET".equals(exchange.getRequestMethod())) {
                String responseText = "Hello World!";
                exchange.sendResponseHeaders(200, responseText.getBytes().length);
                exchange.getResponseBody().write(responseText.getBytes());
                exchange.getResponseBody().flush();
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
            exchange.close();
        }));

        server.setExecutor(null);
        server.start();
    }
}
