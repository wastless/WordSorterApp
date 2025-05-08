import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

public class WordSorterServer {
    private static final int DEFAULT_PORT = 5000;

    public static void main(String[] args) {
        // Get port from environment variable or use default
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", String.valueOf(DEFAULT_PORT)));
        
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);
            
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                    
                    // Read the text block from client
                    String textBlock = in.readLine();
                    if (textBlock != null) {
                        // Process the text and get sorted unique words
                        String sortedWords = processText(textBlock);
                        // Send the result back to client
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
