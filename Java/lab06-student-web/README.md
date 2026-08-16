# LAB 6 - 12 BÀI

1. Hello Servlet
2. Form nhập sinh viên
3. JSP + JSTL hiển thị danh sách
4. Login + Session
5. AuthFilter + Listener
6. Tìm kiếm sinh viên
7. Xóa sinh viên
8. Cập nhật sinh viên
9. Phân quyền Admin/User
10. Dashboard
11. Access Log Filter
12. Listener khởi tạo dữ liệu mẫu

## Môi trường
JDK 17, Maven, Tomcat 10.x, Jakarta Servlet 6.0, JSTL 3.x.

## Build
Chạy tại thư mục chứa pom.xml:

mvn clean package

WAR: target/lab06-student-web.war

## Deploy
Copy WAR vào webapps của Tomcat 10.x rồi mở:
http://localhost:8080/lab06-student-web/

## Tài khoản
Admin: admin / 123456
User: user / 123456
