import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.UUID;

public class StandaloneHttpServer {

    static final String TRACE_ID = "x-vs-trace-id";

    static HttpHandler apiHelloHandler = (exchange) -> {
        exchange.getResponseHeaders().set(TRACE_ID, getTraceId(exchange.getRequestHeaders()));
        exchange.getResponseHeaders().set("Content-type", "application/json");
        String responseTextForGET = "Hello World GET!", responseTextForPOST = "{\"name\":\"fnu\"}";

        switch (exchange.getRequestMethod()) {
            case "GET":
                exchange.sendResponseHeaders(200, responseTextForGET.getBytes().length);
                exchange.getResponseBody().write(responseTextForGET.getBytes());
                exchange.getResponseBody().flush();
                break;
            case "POST":
                exchange.sendResponseHeaders(200, responseTextForPOST.getBytes().length);
                exchange.getResponseBody().write(responseTextForPOST.getBytes());
                exchange.getResponseBody().flush();
                break;
            default:
                exchange.sendResponseHeaders(405, -1);
                break;
        }

        exchange.close();
    };

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(6880), 0);
        server.createContext("/api/hello", apiHelloHandler);
        server.setExecutor(null);
        server.start();
    }

    private static String getTraceId(Headers requestHeaders) {
        // Headers is a Map<String, List<String>>
        return (null != requestHeaders.getFirst(TRACE_ID)) ? requestHeaders.getFirst(TRACE_ID) : UUID.randomUUID().toString();
    }
}