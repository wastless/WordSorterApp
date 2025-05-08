import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class WordSorterClient extends JFrame {
    // private static final String SERVER_HOST = "localhost";
    private static final String SERVER_HOST = "wordsorterapp.onrender.com";
    private static final int SERVER_PORT = 443;
    
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
        
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            
            // Send text to server
            out.println(text);
            
            // Read response
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line).append("\n");
            }
            
            // Display result
            outputArea.setText(response.toString());
            
        } catch (IOException ex) {
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
