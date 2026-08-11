package vn.edu.eaut.lab4;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class ProductLoaderFrame extends JFrame {
    private final JButton btnLoad = new JButton("Tải sản phẩm");
    private final JTable table = new JTable();
    private final JProgressBar progressBar = new JProgressBar(0, 100);
    private final JLabel lblStatus = new JLabel("Chưa tải dữ liệu");

    public ProductLoaderFrame() {
        setTitle("Bài 9 - Mô phỏng tải danh sách sản phẩm");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel top = new JPanel(new BorderLayout(10, 0));
        top.add(btnLoad, BorderLayout.WEST);
        top.add(progressBar, BorderLayout.CENTER);
        top.add(lblStatus, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnLoad.addActionListener(e -> loadProducts());
    }

    private void loadProducts() {
        btnLoad.setEnabled(false);
        progressBar.setValue(0);
        lblStatus.setText("Đang tải...");

        SwingWorker<List<Product>, Product> worker = new SwingWorker<>() {
            @Override
            protected List<Product> doInBackground() throws Exception {
                List<Product> products = new ArrayList<>();
                products.add(new Product("SP01", "Bàn phím", 250000));
                products.add(new Product("SP02", "Chuột", 150000));
                products.add(new Product("SP03", "Màn hình", 2500000));
                products.add(new Product("SP04", "Tai nghe", 450000));
                products.add(new Product("SP05", "USB", 120000));

                for (int i = 0; i < products.size(); i++) {
                    Thread.sleep(1000);
                    products.get(i);
                    setProgress((i + 1) * 100 / products.size());
                }
                return products;
            }

            @Override
            protected void done() {
                try {
                    DefaultTableModel model = new DefaultTableModel(
                            new Object[]{"Mã SP", "Tên SP", "Đơn giá"}, 0);
                    for (Product p : get()) {
                        model.addRow(new Object[]{p.id, p.name, p.price});
                    }
                    table.setModel(model);
                    lblStatus.setText("Tải hoàn tất");
                    progressBar.setValue(100);
                } catch (Exception ex) {
                    lblStatus.setText("Lỗi tải dữ liệu");
                }
                btnLoad.setEnabled(true);
            }
        };

        worker.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                progressBar.setValue((int) evt.getNewValue());
            }
        });
        worker.execute();
    }

    private record Product(String id, String name, double price) {}
}
