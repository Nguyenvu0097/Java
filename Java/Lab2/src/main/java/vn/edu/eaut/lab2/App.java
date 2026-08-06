package vn.edu.eaut.lab2;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("===== CHUONG TRINH TINH DIEM HOC PHAN =====");

        System.out.print("Nhap ma sinh vien: ");
        String id = scanner.nextLine();

        System.out.print("Nhap ho ten: ");
        String name = scanner.nextLine();

        double attendance = inputScore(scanner, "Diem chuyen can");

        double midterm = inputScore(scanner, "Diem giua ky");

        double finalExam = inputScore(scanner, "Diem cuoi ky");

        Student student = new Student(
                id,
                name,
                attendance,
                midterm,
                finalExam
        );

        double total = GradeCalculator.calculateFinalScore(student);

        String grade = GradeCalculator.classify(total);

        System.out.println("\n===== KET QUA =====");

        System.out.println("Ma SV : " + student.getStudentId());
        System.out.println("Ho ten: " + student.getFullName());

        System.out.printf("Diem tong ket: %.2f%n", total);

        System.out.println("Xep loai: " + grade);

        scanner.close();
    }

    private static double inputScore(Scanner scanner, String label) {

        while (true) {

            try {

                System.out.print(label + ": ");

                double score = Double.parseDouble(scanner.nextLine());

                GradeCalculator.validateScore(score, label);

                return score;

            } catch (Exception e) {

                System.out.println("Loi: " + e.getMessage());

            }

        }

    }

}