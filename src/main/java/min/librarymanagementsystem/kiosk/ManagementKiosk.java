package min.librarymanagementsystem.kiosk;

import lombok.RequiredArgsConstructor;
import min.librarymanagementsystem.book.Book;
import min.librarymanagementsystem.book.Category;
import min.librarymanagementsystem.grade.Grade;
import min.librarymanagementsystem.repository.LibraryRepository;
import min.librarymanagementsystem.member.Member;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
public class ManagementKiosk {
    private final LibraryRepository repo;
    private final Scanner sc;

    public void start() {
        while (true) {
            System.out.println("\n[관리자 메뉴] 1.회원관리 2.등급관리 3.도서관리 4.대출기록조회 0.종료 ( 0~4 입력 )");
            switch (sc.nextLine().trim()) {
                case "1" -> userManagement();
                case "2" -> gradeManagement();
                case "3" -> bookManagement();
                case "4" -> searchRecords();
                case "0" -> { return; }
                default -> System.out.println("메뉴 선택 오류");
            }
        }
    }

    private void userManagement() {
        System.out.println("회원관리: 1.등록 2.목록 3.삭제 ( 1~3 입력 )");
        switch (sc.nextLine().trim()) {
            case "1" -> {
                System.out.print("아이디: "); String memberId = sc.nextLine();
                System.out.print("비밀번호: "); String password = sc.nextLine();
                System.out.print("이름: "); String name = sc.nextLine();
                Member m = new Member(memberId, password, name, Grade.BASIC);
                repo.addMember(m);
                System.out.println("생성: " + m);
            }
            case "2" -> repo.allMembers().forEach(System.out::println);
            case "3" -> {
                System.out.print("삭제할 회원ID: "); String id = sc.nextLine();
                System.out.println(repo.removeMember(id) ? "삭제됨" : "해당 없음");
            }
        }
    }

    private void gradeManagement() {
        System.out.println("등급관리: 1.변경 2.목록 ( 1~2 입력 )");
        switch (sc.nextLine().trim()) {
            case "1" -> {
                System.out.print("회원ID: "); String memberId = sc.nextLine();
                System.out.print("등급(BASIC/SILVER/GOLD/STAFF): ");
                Grade g = Grade.valueOf(sc.nextLine().trim().toUpperCase());
                repo.findMember(memberId).ifPresentOrElse(m -> {
                    m.setGrade(g);
                    System.out.println("변경됨: " + m);
                }, () -> System.out.println("회원 없음"));
            }
            case "2" -> {
                for (Grade g : Grade.values()) {
                    System.out.printf("%s - 한도:%d, 기간:%d일%n", g, g.borrowLimit(), g.loanPeriodDays());
                }
            }
        }
    }

    private void bookManagement() {
        System.out.println("도서관리: 1.등록 2.목록 3.수정 4.삭제 ( 1~4 입력 )");
        switch (sc.nextLine().trim()) {
            case "1" -> {
                System.out.print("ISBN: "); String isbn = sc.nextLine();
                System.out.print("제목: "); String title = sc.nextLine();
                System.out.print("저자: "); String author = sc.nextLine();
                System.out.print("카테고리(FICTION,NONFICTION,SCIENCE,HISTORY,CHILDREN): ");
                Category c = Category.valueOf(sc.nextLine().trim().toUpperCase());
                System.out.print("재고: "); int stock = Integer.parseInt(sc.nextLine());
                repo.addBook(new Book(isbn, title, author, c, stock));
                System.out.println("등록 완료");
            }
            case "2" -> repo.allBooks().forEach(System.out::println);
            case "3" -> {
                System.out.print("수정할 ISBN: "); String isbn = sc.nextLine();
                repo.findBook(isbn).ifPresentOrElse(b -> {
                    System.out.print("제목(엔터시 유지): "); String t = sc.nextLine();
                    if (!t.isBlank()) b.setTitle(t);
                    System.out.print("저자(엔터시 유지): "); String a = sc.nextLine();
                    if (!a.isBlank()) b.setAuthor(a);
                    System.out.print("카테고리(엔터시 유지): "); String c = sc.nextLine();
                    if (!c.isBlank()) b.setCategory(Category.valueOf(c.toUpperCase()));
                    System.out.print("재고(엔터시 유지): "); String s = sc.nextLine();
                    if (!s.isBlank()) b.setStock(Integer.parseInt(s));
                    System.out.println("수정됨: " + b);
                }, () -> System.out.println("도서 없음"));
            }
            case "4" -> {
                System.out.print("삭제할 ISBN: "); String isbn = sc.nextLine();
                System.out.println(repo.removeBook(isbn) ? "삭제됨" : "해당 없음");
            }
        }
    }

    private void searchRecords() {
        System.out.println("대출기록 조회: 1.전체 2.연체만 ( 1~2 입력 )");
        switch (sc.nextLine().trim()) {
            case "1" -> repo.allRecords().forEach(System.out::println);
            case "2" -> {
                List<?> list = repo.overdueRecords(LocalDate.now());
                if (list.isEmpty()) System.out.println("연체 없음");
                else list.forEach(System.out::println);
            }
        }
    }
}
