import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.UUID;

public class StandaloneHttpServer {

    static final String TRACE_ID = "x-vs-trace-id";

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(6880), 0);
        server.createContext("/api/hello", (exchange -> {

            Headers requestHeaders = exchange.getRequestHeaders(); // Headers is a Map<String, List<String>>
            String traceId = (null != requestHeaders.getFirst(TRACE_ID)) ? requestHeaders.getFirst(TRACE_ID) : UUID.randomUUID().toString();

            if ("GET".equals(exchange.getRequestMethod())) {
                String responseText = "Hello World!";
                exchange.getResponseHeaders().set(TRACE_ID, traceId);
                exchange.sendResponseHeaders(200, responseText.getBytes().length);
                exchange.getResponseBody().write(responseText.getBytes());
                exchange.getResponseBody().flush();
            } else {
                exchange.getResponseHeaders().set(TRACE_ID, traceId);
                exchange.sendResponseHeaders(405, -1);
            }
            exchange.close();
        }));

        server.setExecutor(null);
        server.start();
    }
}
