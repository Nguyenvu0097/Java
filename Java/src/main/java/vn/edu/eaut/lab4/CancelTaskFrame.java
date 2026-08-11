package vn.edu.eaut.lab4;

import javax.swing.*;
import java.awt.*;

public class CancelTaskFrame extends JFrame {
    private final JTextField txtSeconds = new JTextField("20");
    private final JButton btnStart = new JButton("Bắt đầu");
    private final JButton btnCancel = new JButton("Hủy");
    private final JLabel lblStatus = new JLabel("Sẵn sàng");
    private final JProgressBar progressBar = new JProgressBar(0, 100);
    private SwingWorker<Void, Integer> worker;

    public CancelTaskFrame() {
        setTitle("Bài 6 - Hủy tác vụ");
        setSize(500, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.add(txtSeconds);

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(btnStart);
        buttons.add(btnCancel);
        panel.add(buttons);
        panel.add(progressBar);
        panel.add(lblStatus);
        panel.add(new JLabel("Nhập số giây để mô phỏng tác vụ"));
        add(panel);

        btnCancel.setEnabled(false);
        btnStart.addActionListener(e -> startTask());
        btnCancel.addActionListener(e -> cancelTask());
    }

    private void startTask() {
        final int seconds;
        try {
            seconds = Integer.parseInt(txtSeconds.getText().trim());
            if (seconds <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số nguyên > 0");
            return;
        }

        btnStart.setEnabled(false);
        btnCancel.setEnabled(true);
        progressBar.setValue(0);
        lblStatus.setText("Đang chạy...");

        worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (int i = 0; i <= seconds; i++) {
                    if (isCancelled()) return null;
                    setProgress(i * 100 / seconds);
                    Thread.sleep(1000);
                }
                return null;
            }

            @Override
            protected void done() {
                if (isCancelled()) {
                    lblStatus.setText("Đã hủy tác vụ");
                } else {
                    progressBar.setValue(100);
                    lblStatus.setText("Tác vụ hoàn thành");
                }
                btnStart.setEnabled(true);
                btnCancel.setEnabled(false);
            }
        };

        worker.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                progressBar.setValue((int) evt.getNewValue());
            }
        });
        worker.execute();
    }

    private void cancelTask() {
        if (worker != null && !worker.isDone()) {
            worker.cancel(true);
            lblStatus.setText("Đang hủy...");
        }
    }
}
