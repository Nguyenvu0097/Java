package vn.edu.eaut.lab4;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileLineCounterFrame extends JFrame {
    private File selectedFile;
    private final JButton btnChoose = new JButton("Chọn file");
    private final JButton btnCount = new JButton("Đếm dòng");
    private final JLabel lblFile = new JLabel("Chưa chọn file");
    private final JLabel lblResult = new JLabel("Số dòng: ");
    private final JProgressBar progressBar = new JProgressBar(0, 100);

    public FileLineCounterFrame() {
        setTitle("Bài 5 - Đọc file và đếm số dòng");
        setSize(650, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 1, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.add(btnChoose);
        panel.add(lblFile);
        panel.add(btnCount);
        panel.add(progressBar);
        panel.add(lblResult);
        add(panel);

        btnChoose.addActionListener(e -> chooseFile());
        btnCount.addActionListener(e -> countLines());
    }

    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.getSelectedFile();
            lblFile.setText("File: " + selectedFile.getAbsolutePath());
        }
    }

    private void countLines() {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn file trước");
            return;
        }

        btnCount.setEnabled(false);
        progressBar.setValue(0);
        lblResult.setText("Đang đọc file...");

        SwingWorker<Long, Void> worker = new SwingWorker<>() {
            @Override
            protected Long doInBackground() throws Exception {
                long totalBytes = Files.size(selectedFile.toPath());
                long readBytes = 0;
                long lines = 0;

                try (BufferedReader reader = Files.newBufferedReader(
                        selectedFile.toPath(), StandardCharsets.UTF_8)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        lines++;
                        readBytes += line.getBytes(StandardCharsets.UTF_8).length + 1;
                        int progress = totalBytes == 0 ? 100 :
                                (int) Math.min(100, readBytes * 100 / totalBytes);
                        setProgress(progress);
                    }
                }
                return lines;
            }

            @Override
            protected void done() {
                try {
                    lblResult.setText("Số dòng: " + get());
                } catch (Exception ex) {
                    lblResult.setText("Lỗi khi đọc file");
                }
                progressBar.setValue(100);
                btnCount.setEnabled(true);
            }
        };

        worker.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                progressBar.setValue((int) evt.getNewValue());
            }
        });
        worker.execute();
    }
}
