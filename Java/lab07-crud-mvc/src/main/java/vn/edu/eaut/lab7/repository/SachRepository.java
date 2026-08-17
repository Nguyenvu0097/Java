package vn.edu.eaut.lab7.repository;

import vn.edu.eaut.lab7.model.Sach;
import java.util.ArrayList;
import java.util.List;

public class SachRepository {

    private static final List<Sach> d = new ArrayList<>();
    private static int n = 3;

    static {
        d.add(new Sach(
                1,
                "S001",
                "Lập trình Java",
                "Nguyễn A",
                "NXB Giáo dục",
                2024
        ));

        d.add(new Sach(
                2,
                "S002",
                "Web Java",
                "Trần B",
                "NXB Khoa học",
                2025
        ));
    }

    public List<Sach> findAll() {
        return d;
    }

    public Sach findById(int i) {
        for (Sach x : d) {
            if (x.getId() == i) {
                return x;
            }
        }
        return null;
    }

    public void add(Sach x) {
        x.setId(n++);
        d.add(x);
    }

    public void update(Sach x) {
        Sach o = findById(x.getId());

        if (o != null) {
            o.setMaSach(x.getMaSach());
            o.setTenSach(x.getTenSach());
            o.setTacGia(x.getTacGia());
            o.setNhaXuatBan(x.getNhaXuatBan());
            o.setNamXuatBan(x.getNamXuatBan());
        }
    }

    public void delete(int i) {
        for (int j = 0; j < d.size(); j++) {
            if (d.get(j).getId() == i) {
                d.remove(j);
                return;
            }
        }
    }

    public List<Sach> search(String k) {

        if (k == null || k.isBlank()) {
            return d;
        }

        String keyword = k.toLowerCase();

        List<Sach> result = new ArrayList<>();

        for (Sach x : d) {

            String tenSach = x.getTenSach() == null
                    ? ""
                    : x.getTenSach().toLowerCase();

            String tacGia = x.getTacGia() == null
                    ? ""
                    : x.getTacGia().toLowerCase();

            if (tenSach.contains(keyword) || tacGia.contains(keyword)) {
                result.add(x);
            }
        }

        return result;
    }
}