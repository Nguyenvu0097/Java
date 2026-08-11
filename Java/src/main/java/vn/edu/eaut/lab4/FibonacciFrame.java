package vn.edu.eaut.lab4;

import javax.swing.*;
import java.awt.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class FibonacciFrame extends JFrame {
    private final JTextField txtN = new JTextField();
    private final JButton btnFind = new JButton("Tìm");
    private final JLabel lblResult = new JLabel("Kết quả: ");
    private final JProgressBar progressBar = new JProgressBar();

    public FibonacciFrame() {
        setTitle("Bài 4 - Fibonacci bằng Memoization");
        setSize(550, 220);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.add(txtN);
        panel.add(btnFind);
        panel.add(progressBar);
        panel.add(lblResult);
        add(panel);

        btnFind.addActionListener(e -> findFibonacci());
    }

    private BigInteger fibonacci(int n, Map<Integer, BigInteger> memo) {
        if (n <= 1) return BigInteger.valueOf(n);
        if (memo.containsKey(n)) return memo.get(n);
        BigInteger value = fibonacci(n - 1, memo).add(fibonacci(n - 2, memo));
        memo.put(n, value);
        return value;
    }

    private void findFibonacci() {
        final int n;
        try {
            n = Integer.parseInt(txtN.getText().trim());
            if (n < 0) {
                JOptionPane.showMessageDialog(this, "N phải >= 0");
                return;
            }
            if (n > 10000) {
                JOptionPane.showMessageDialog(this, "N quá lớn, hãy nhập N <= 10000");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số nguyên hợp lệ");
            return;
        }

        btnFind.setEnabled(false);
        progressBar.setIndeterminate(true);
        lblResult.setText("Đang tính Fibonacci...");

        SwingWorker<BigInteger, Void> worker = new SwingWorker<>() {
            @Override
            protected BigInteger doInBackground() {
                Map<Integer, BigInteger> memo = new HashMap<>();
                return fibonacci(n, memo);
            }

            @Override
            protected void done() {
                try {
                    lblResult.setText("Fibonacci(" + n + ") = " + get());
                } catch (Exception ex) {
                    lblResult.setText("Có lỗi khi tính Fibonacci");
                }
                progressBar.setIndeterminate(false);
                progressBar.setValue(100);
                btnFind.setEnabled(true);
            }
        };
        worker.execute();
    }
}
