package vn.edu.eaut.lab7.repository;

import vn.edu.eaut.lab7.model.SanPham;
import java.util.ArrayList;
import java.util.List;

public class SanPhamRepository {

    private static final List<SanPham> d = new ArrayList<>();
    private static int n = 3;

    static {
        d.add(new SanPham(
                1,
                "P001",
                "Bàn phím",
                "Bàn phím cơ",
                450000,
                10
        ));

        d.add(new SanPham(
                2,
                "P002",
                "Chuột",
                "Chuột không dây",
                250000,
                20
        ));
    }

    public List<SanPham> findAll() {
        return d;
    }

    public SanPham findById(int i) {
        for (SanPham x : d) {
            if (x.getId() == i) {
                return x;
            }
        }

        return null;
    }

    public void add(SanPham x) {
        x.setId(n++);
        d.add(x);
    }

    public void update(SanPham x) {
        SanPham o = findById(x.getId());

        if (o != null) {
            o.setMa(x.getMa());
            o.setTen(x.getTen());
            o.setMoTa(x.getMoTa());
            o.setGia(x.getGia());
            o.setSoLuong(x.getSoLuong());
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

    public List<SanPham> search(String k) {

        if (k == null || k.isBlank()) {
            return d;
        }

        String keyword = k.toLowerCase();

        List<SanPham> result = new ArrayList<>();

        for (SanPham x : d) {

            String ten = x.getTen() == null
                    ? ""
                    : x.getTen().toLowerCase();

            String ma = x.getMa() == null
                    ? ""
                    : x.getMa().toLowerCase();

            if (ten.contains(keyword) || ma.contains(keyword)) {
                result.add(x);
            }
        }

        return result;
    }
}