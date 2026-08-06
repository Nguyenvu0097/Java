package vn.edu.eaut.lab2;

public class GradeCalculator {

    public static double calculateFinalScore(Student student) {

        return student.getAttendanceScore() * 0.1
                + student.getMidtermScore() * 0.3
                + student.getFinalScore() * 0.6;
    }

    public static String classify(double score) {

        if (score >= 8.5)
            return "A";

        if (score >= 7.0)
            return "B";

        if (score >= 5.5)
            return "C";

        if (score >= 4.0)
            return "D";

        return "F";
    }

    public static void validateScore(double score, String field) {

        if (score < 0 || score > 10) {
            throw new IllegalArgumentException(
                    field + " phải nằm trong khoảng 0 đến 10");
        }
    }

}