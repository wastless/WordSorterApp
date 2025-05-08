import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;
import com.sun.net.httpserver.*;

public class WordSorterServer {
    private static final int PORT = Integer.parseInt(System.getenv().getOrDefault("PORT", "5000"));
    private static final String HOST = System.getenv().getOrDefault("HOST", "0.0.0.0");

    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(HOST, PORT), 0);
            server.createContext("/", new WordSorterHandler());
            server.setExecutor(null);
            server.start();
            System.out.println("Server started on " + HOST + ":" + PORT);
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    static class WordSorterHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                // Read request body
                String text = new String(exchange.getRequestBody().readAllBytes());
                
                // Process the text
                String sortedWords = processText(text);
                
                // Send response
                exchange.sendResponseHeaders(200, sortedWords.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(sortedWords.getBytes());
                }
            } else {
                String response = "Please use POST method with text in body";
                exchange.sendResponseHeaders(405, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        }
    }

    private static String processText(String text) {
        // Convert to lowercase and remove punctuation and special characters
        String cleanedText = text.toLowerCase()
            .replaceAll("[^а-яёa-z\\s]", " ") // Replace all non-letters with space
            .replaceAll("\\s+", " ")          // Replace multiple spaces with single space
            .trim();                          // Remove leading/trailing spaces
        
        // Split text into words and remove duplicates
        Set<String> uniqueWords = new TreeSet<>(Arrays.asList(
            cleanedText.split("\\s+")
        ));
        
        // Filter out empty strings and join words with newlines
        return uniqueWords.stream()
            .filter(word -> !word.isEmpty())
            .collect(Collectors.joining("\n"));
    }
}
