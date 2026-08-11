package vn.edu.eaut.lab4;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ProductManagerFrame extends JFrame {
    private final JTextField txtId = new JTextField();
    private final JTextField txtName = new JTextField();
    private final JTextField txtPrice = new JTextField();
    private final JButton btnAdd = new JButton("Thêm");
    private final JButton btnEdit = new JButton("Sửa");
    private final JButton btnDelete = new JButton("Xóa");
    private final JButton btnLoad = new JButton("Đọc CSV");
    private final JButton btnSave = new JButton("Lưu CSV");
    private final JTable table = new JTable();
    private final JLabel lblStatus = new JLabel("Sẵn sàng");
    private final JProgressBar progressBar = new JProgressBar(0, 100);

    private final List<Product> products = new ArrayList<>();
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Mã SP", "Tên SP", "Đơn giá"}, 0);
    private File csvFile;

    public ProductManagerFrame() {
        setTitle("Bài 10 - Quản lý sản phẩm bằng CSV");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel input = new JPanel(new GridLayout(3, 2, 8, 8));
        input.setBorder(BorderFactory.createTitledBorder("Thông tin sản phẩm"));
        input.add(new JLabel("Mã SP:"));
        input.add(txtId);
        input.add(new JLabel("Tên SP:"));
        input.add(txtName);
        input.add(new JLabel("Đơn giá:"));
        input.add(txtPrice);

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(btnAdd);
        buttons.add(btnEdit);
        buttons.add(btnDelete);
        buttons.add(btnLoad);
        buttons.add(btnSave);

        JPanel north = new JPanel(new BorderLayout());
        north.add(input, BorderLayout.CENTER);
        north.add(buttons, BorderLayout.SOUTH);
        add(north, BorderLayout.NORTH);

        table.setModel(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout());
        south.add(lblStatus, BorderLayout.WEST);
        south.add(progressBar, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addProduct());
        btnEdit.addActionListener(e -> editProduct());
        btnDelete.addActionListener(e -> deleteProduct());
        btnLoad.addActionListener(e -> loadCsv());
        btnSave.addActionListener(e -> saveCsv());
        table.getSelectionModel().addListSelectionListener(e -> fillForm());
    }

    private Product readForm() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        double price = Double.parseDouble(txtPrice.getText().trim());

        if (id.isEmpty() || name.isEmpty() || price < 0) {
            throw new IllegalArgumentException("Dữ liệu không hợp lệ");
        }
        return new Product(id, name, price);
    }

    private void addProduct() {
        try {
            Product p = readForm();
            products.add(p);
            refreshTable();
            clearForm();
            lblStatus.setText("Đã thêm sản phẩm");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void editProduct() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn sản phẩm cần sửa");
            return;
        }

        try {
            products.set(row, readForm());
            refreshTable();
            clearForm();
            lblStatus.setText("Đã sửa sản phẩm");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void deleteProduct() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn sản phẩm cần xóa");
            return;
        }

        products.remove(row);
        refreshTable();
        clearForm();
        lblStatus.setText("Đã xóa sản phẩm");
    }

    private void fillForm() {
        int row = table.getSelectedRow();
        if (row >= 0 && row < products.size()) {
            Product p = products.get(row);
            txtId.setText(p.id);
            txtName.setText(p.name);
            txtPrice.setText(String.valueOf(p.price));
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtPrice.setText("");
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (Product p : products) {
            model.addRow(new Object[]{p.id, p.name, p.price});
        }
    }

    private void loadCsv() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        csvFile = chooser.getSelectedFile();

        setButtons(false);
        lblStatus.setText("Đang đọc CSV...");
        progressBar.setIndeterminate(true);

        SwingWorker<List<Product>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Product> doInBackground() throws Exception {
                List<Product> result = new ArrayList<>();
                List<String> lines = Files.readAllLines(
                        csvFile.toPath(), StandardCharsets.UTF_8);

                int start = lines.isEmpty() ? 0 : 1;
                for (int i = start; i < lines.size(); i++) {
                    String[] p = lines.get(i).split(",", -1);
                    if (p.length >= 3) {
                        result.add(new Product(p[0].trim(), p[1].trim(),
                                Double.parseDouble(p[2].trim())));
                    }
                }
                return result;
            }

            @Override
            protected void done() {
                try {
                    products.clear();
                    products.addAll(get());
                    refreshTable();
                    lblStatus.setText("Đọc CSV thành công");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ProductManagerFrame.this,
                            "Lỗi đọc CSV: " + ex.getMessage());
                    lblStatus.setText("Lỗi đọc CSV");
                }
                progressBar.setIndeterminate(false);
                progressBar.setValue(100);
                setButtons(true);
            }
        };
        worker.execute();
    }

    private void saveCsv() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        csvFile = chooser.getSelectedFile();

        setButtons(false);
        lblStatus.setText("Đang lưu CSV...");
        progressBar.setIndeterminate(true);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                try (BufferedWriter writer = Files.newBufferedWriter(
                        csvFile.toPath(), StandardCharsets.UTF_8)) {
                    writer.write("MaSP,TenSP,DonGia");
                    writer.newLine();

                    for (Product p : products) {
                        writer.write(p.id + "," + p.name + "," + p.price);
                        writer.newLine();
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    lblStatus.setText("Lưu CSV thành công");
                } catch (Exception ex) {
                    lblStatus.setText("Lỗi lưu CSV");
                    JOptionPane.showMessageDialog(ProductManagerFrame.this,
                            "Lỗi lưu CSV: " + ex.getMessage());
                }
                progressBar.setIndeterminate(false);
                progressBar.setValue(100);
                setButtons(true);
            }
        };
        worker.execute();
    }

    private void setButtons(boolean enabled) {
        btnAdd.setEnabled(enabled);
        btnEdit.setEnabled(enabled);
        btnDelete.setEnabled(enabled);
        btnLoad.setEnabled(enabled);
        btnSave.setEnabled(enabled);
    }

    private static class Product {
        String id, name;
        double price;

        Product(String id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductManagerFrame().setVisible(true));
    }
}
