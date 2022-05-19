import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.UUID;

public class StandaloneHttpServer {

    static final String TRACE_ID = "x-vs-trace-id";

    static HttpHandler apiHelloGETHandler = (exchange) -> {
        exchange.getResponseHeaders().set(TRACE_ID, getTraceId(exchange.getRequestHeaders()));

        if ("GET".equals(exchange.getRequestMethod())) {
            String responseText = "Hello World!";
            exchange.sendResponseHeaders(200, responseText.getBytes().length);
            exchange.getResponseBody().write(responseText.getBytes());
            exchange.getResponseBody().flush();
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
        exchange.close();
    };

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(6880), 0);
        server.createContext("/api/hello", apiHelloGETHandler);
        server.setExecutor(null);
        server.start();
    }

    private static String getTraceId(Headers requestHeaders) {
        // Headers is a Map<String, List<String>>
        return (null != requestHeaders.getFirst(TRACE_ID)) ? requestHeaders.getFirst(TRACE_ID) : UUID.randomUUID().toString();
    }
}
