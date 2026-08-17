package vn.edu.eaut.lab7.repository;

import vn.edu.eaut.lab7.model.SinhVien;
import java.util.ArrayList;
import java.util.List;

public class SinhVienRepository {

    private static final List<SinhVien> d = new ArrayList<>();
    private static int n = 3;

    static {
        d.add(new SinhVien(
                1,
                "20240001",
                "Nguyễn Văn An",
                "an@gmail.com",
                "DCCNTT15.10.1"
        ));

        d.add(new SinhVien(
                2,
                "20240002",
                "Trần Thị Bình",
                "binh@gmail.com",
                "DCCNTT15.10.2"
        ));
    }

    public List<SinhVien> findAll() {
        return d;
    }

    public SinhVien findById(int i) {
        for (SinhVien x : d) {
            if (x.getId() == i) {
                return x;
            }
        }

        return null;
    }

    public void add(SinhVien x) {
        x.setId(n++);
        d.add(x);
    }

    public void update(SinhVien x) {
        SinhVien o = findById(x.getId());

        if (o != null) {
            o.setMaSinhVien(x.getMaSinhVien());
            o.setHoTen(x.getHoTen());
            o.setEmail(x.getEmail());
            o.setLop(x.getLop());
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

    public List<SinhVien> search(String k) {

        if (k == null || k.isBlank()) {
            return d;
        }

        String keyword = k.toLowerCase();

        List<SinhVien> result = new ArrayList<>();

        for (SinhVien x : d) {

            String hoTen = x.getHoTen() == null
                    ? ""
                    : x.getHoTen().toLowerCase();

            String lop = x.getLop() == null
                    ? ""
                    : x.getLop().toLowerCase();

            if (hoTen.contains(keyword) || lop.contains(keyword)) {
                result.add(x);
            }
        }

        return result;
    }
}