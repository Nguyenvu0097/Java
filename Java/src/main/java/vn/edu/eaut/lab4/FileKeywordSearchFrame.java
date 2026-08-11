package vn.edu.eaut.lab4;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileKeywordSearchFrame extends JFrame {
    private File selectedFile;
    private final JButton btnChoose = new JButton("Chọn file .txt");
    private final JTextField txtKeyword = new JTextField();
    private final JButton btnSearch = new JButton("Tìm kiếm");
    private final JLabel lblFile = new JLabel("Chưa chọn file");
    private final JLabel lblResult = new JLabel("Số dòng tìm thấy: 0");
    private final JTextArea txtOutput = new JTextArea();
    private final JProgressBar progressBar = new JProgressBar(0, 100);

    public FileKeywordSearchFrame() {
        setTitle("Bài 7 - Tìm kiếm từ khóa trong file");
        setSize(750, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel top = new JPanel(new GridLayout(4, 1, 8, 8));
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        top.add(btnChoose);
        top.add(lblFile);

        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        searchPanel.add(new JLabel("Từ khóa:"), BorderLayout.WEST);
        searchPanel.add(txtKeyword, BorderLayout.CENTER);
        searchPanel.add(btnSearch, BorderLayout.EAST);
        top.add(searchPanel);

        top.add(progressBar);
        add(top, BorderLayout.NORTH);

        txtOutput.setEditable(false);
        add(new JScrollPane(txtOutput), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(lblResult);
        add(bottom, BorderLayout.SOUTH);

        btnChoose.addActionListener(e -> chooseFile());
        btnSearch.addActionListener(e -> search());
    }

    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.getSelectedFile();
            lblFile.setText("File: " + selectedFile.getAbsolutePath());
        }
    }

    private void search() {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn file");
            return;
        }
        String keyword = txtKeyword.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa");
            return;
        }

        btnSearch.setEnabled(false);
        txtOutput.setText("");
        lblResult.setText("Đang tìm...");
        progressBar.setIndeterminate(true);

        SwingWorker<Integer, String> worker = new SwingWorker<>() {
            @Override
            protected Integer doInBackground() throws Exception {
                int count = 0;
                try (BufferedReader reader = Files.newBufferedReader(
                        selectedFile.toPath(), StandardCharsets.UTF_8)) {
                    String line;
                    int lineNumber = 0;
                    while ((line = reader.readLine()) != null) {
                        lineNumber++;
                        if (line.toLowerCase().contains(keyword)) {
                            count++;
                            publish("Dòng " + lineNumber + ": " + line);
                        }
                    }
                }
                return count;
            }

            @Override
            protected void process(java.util.List<String> chunks) {
                for (String s : chunks) {
                    txtOutput.append(s + "\n");
                }
            }

            @Override
            protected void done() {
                progressBar.setIndeterminate(false);
                try {
                    lblResult.setText("Số dòng tìm thấy: " + get());
                } catch (Exception ex) {
                    lblResult.setText("Lỗi khi tìm kiếm");
                }
                btnSearch.setEnabled(true);
            }
        };
        worker.execute();
    }
}
