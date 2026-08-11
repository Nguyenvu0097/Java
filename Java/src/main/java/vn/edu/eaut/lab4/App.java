package vn.edu.eaut.lab4;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {
    public App() {
        setTitle("LAB 4 - Java SwingWorker - 10 Bài");
        setSize(520, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(10, 1, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        String[] names = {
            "Bài 1 - Đồng hồ đếm ngược",
            "Bài 2 - Mô phỏng tải dữ liệu",
            "Bài 3 - Tổng số nguyên tố",
            "Bài 4 - Fibonacci Memoization",
            "Bài 5 - Đếm số dòng file",
            "Bài 6 - Hủy tác vụ",
            "Bài 7 - Tìm kiếm từ khóa trong file",
            "Bài 8 - CSV điểm sinh viên",
            "Bài 9 - Tải danh sách sản phẩm",
            "Bài 10 - Quản lý sản phẩm CSV"
        };

        Runnable[] actions = {
            () -> new CountdownFrame().setVisible(true),
            () -> new ProgressDemoFrame().setVisible(true),
            () -> new PrimeSumFrame().setVisible(true),
            () -> new FibonacciFrame().setVisible(true),
            () -> new FileLineCounterFrame().setVisible(true),
            () -> new CancelTaskFrame().setVisible(true),
            () -> new FileKeywordSearchFrame().setVisible(true),
            () -> new StudentCsvFrame().setVisible(true),
            () -> new ProductLoaderFrame().setVisible(true),
            () -> new ProductManagerFrame().setVisible(true)
        };

        for (int i = 0; i < names.length; i++) {
            JButton button = new JButton(names[i]);
            final int index = i;
            button.addActionListener(e -> actions[index].run());
            panel.add(button);
        }

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App().setVisible(true));
    }
}
