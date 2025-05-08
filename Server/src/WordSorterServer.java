import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

public class WordSorterServer {
    private static final int DEFAULT_PORT = 10000; // Render.com default port

    public static void main(String[] args) {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", String.valueOf(DEFAULT_PORT)));
        
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);
            
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                    
                    // Read HTTP request
                    String requestLine = in.readLine();
                    if (requestLine != null && requestLine.startsWith("POST")) {
                        // Skip headers
                        String line;
                        int contentLength = 0;
                        while ((line = in.readLine()) != null && !line.isEmpty()) {
                            if (line.toLowerCase().startsWith("content-length:")) {
                                contentLength = Integer.parseInt(line.substring(16).trim());
                            }
                        }
                        
                        // Read request body
                        char[] body = new char[contentLength];
                        in.read(body, 0, contentLength);
                        String textBlock = new String(body);
                        
                        // Process the text and get sorted unique words
                        String sortedWords = processText(textBlock);
                        
                        // Send HTTP response
                        out.println("HTTP/1.1 200 OK");
                        out.println("Content-Type: text/plain; charset=utf-8");
                        out.println("Content-Length: " + sortedWords.getBytes("UTF-8").length);
                        out.println();
                        out.println(sortedWords);
                    }
                } catch (IOException e) {
                    System.err.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
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
