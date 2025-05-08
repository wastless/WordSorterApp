import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.net.http.*;

public class WordSorterClient extends JFrame {
    private static final String SERVER_URL = "https://wordsorterapp.onrender.com";
    
    private JTextArea inputArea;
    private JTextArea outputArea;
    private JButton sortButton;
    
    public WordSorterClient() {
        setTitle("Сортировщик слов");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        
        // Create components
        inputArea = new JTextArea(5, 40);
        outputArea = new JTextArea(10, 40);
        sortButton = new JButton("Сортировать слова");
        
        // Set up layout
        setLayout(new BorderLayout());
        
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(new JLabel("Введете текст:"), BorderLayout.NORTH);
        inputPanel.add(new JScrollPane(inputArea), BorderLayout.CENTER);
        
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(new JLabel("Отсортированные уникальные слова:"), BorderLayout.NORTH);
        outputPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(sortButton);
        
        add(inputPanel, BorderLayout.NORTH);
        add(outputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Add action listener
        sortButton.addActionListener(e -> sortWords());
    }
    
    private void sortWords() {
        String text = inputArea.getText().trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Пожалуйста, введите текст!");
            return;
        }
        
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL))
                .header("Content-Type", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(text))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                outputArea.setText(response.body());
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Ошибка сервера: " + response.statusCode(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Ошибка при подключении к серверу: " + ex.getMessage(),
                "Ошибка",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WordSorterClient().setVisible(true);
        });
    }
}
