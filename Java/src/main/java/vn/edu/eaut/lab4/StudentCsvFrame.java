package vn.edu.eaut.lab4;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class StudentCsvFrame extends JFrame {
    private final JButton btnChoose = new JButton("Chọn CSV");
    private final JTable table = new JTable();
    private final JLabel lblAverage = new JLabel("Điểm trung bình: ");
    private final JLabel lblHighest = new JLabel("Điểm cao nhất: ");
    private final JProgressBar progressBar = new JProgressBar(0, 100);
    private File selectedFile;

    public StudentCsvFrame() {
        setTitle("Bài 8 - CSV điểm sinh viên");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(btnChoose);
        top.add(progressBar);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(2, 1));
        bottom.add(lblAverage);
        bottom.add(lblHighest);
        add(bottom, BorderLayout.SOUTH);

        btnChoose.addActionListener(e -> chooseAndLoad());
    }

    private void chooseAndLoad() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        selectedFile = chooser.getSelectedFile();

        btnChoose.setEnabled(false);
        progressBar.setValue(0);

        SwingWorker<List<Student>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Student> doInBackground() throws Exception {
                List<Student> list = new ArrayList<>();
                List<String> lines = Files.readAllLines(selectedFile.toPath(), StandardCharsets.UTF_8);
                int total = lines.size();

                for (int i = 1; i < lines.size(); i++) {
                    String[] p = lines.get(i).split(",", -1);
                    if (p.length >= 3) {
                        list.add(new Student(p[0].trim(), p[1].trim(),
                                Double.parseDouble(p[2].trim())));
                    }
                    setProgress(total == 0 ? 100 : i * 100 / total);
                }
                return list;
            }

            @Override
            protected void done() {
                try {
                    List<Student> list = get();
                    DefaultTableModel model = new DefaultTableModel(
                            new Object[]{"MaSV", "HoTen", "Diem"}, 0);
                    double sum = 0;
                    Student highest = null;

                    for (Student s : list) {
                        model.addRow(new Object[]{s.id, s.name, s.score});
                        sum += s.score;
                        if (highest == null || s.score > highest.score) highest = s;
                    }

                    table.setModel(model);
                    lblAverage.setText("Điểm trung bình: " +
                            (list.isEmpty() ? 0 : String.format("%.2f", sum / list.size())));
                    lblHighest.setText("Sinh viên điểm cao nhất: " +
                            (highest == null ? "Không có dữ liệu" :
                                    highest.name + " - " + highest.score));
                    progressBar.setValue(100);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(StudentCsvFrame.this,
                            "Lỗi đọc CSV: " + ex.getMessage());
                }
                btnChoose.setEnabled(true);
            }
        };

        worker.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                progressBar.setValue((int) evt.getNewValue());
            }
        });
        worker.execute();
    }

    private static class Student {
        String id, name;
        double score;
        Student(String id, String name, double score) {
            this.id = id; this.name = name; this.score = score;
        }
    }
}
