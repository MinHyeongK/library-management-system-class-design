package min.librarymanagementsystem;

import min.librarymanagementsystem.book.Book;
import min.librarymanagementsystem.book.Category;
import min.librarymanagementsystem.grade.Grade;
import min.librarymanagementsystem.kiosk.BorrowKiosk;
import min.librarymanagementsystem.kiosk.ManagementKiosk;
import min.librarymanagementsystem.member.Member;
import min.librarymanagementsystem.repository.LibraryRepository;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LibraryRepository repo = new LibraryRepository();

        // 데모 데이터
        repo.addBook(new Book("0001", "자바의 정석", "박수짝짝", Category.SCIENCE, 3));
        repo.addBook(new Book("0002", "C의 정석", "짝짝박수", Category.SCIENCE, 0));
        repo.addBook(new Book("0003", "C++의 정석", "짝짝박사", Category.FICTION, 1));
        repo.addBook(new Book("0004", "파이썬의 정석", "박수쩝쩝", Category.FICTION, 4));
        Member admin = new Member("관리자", "1234", "박수현무기러기와두루미", Grade.STAFF);
        repo.addMember(admin);

        while (true) {
            System.out.println("=== 시작 ===");
            System.out.println("1.관리자 2.회원 0.종료 ( 0~2 입력 )");
            String select = sc.nextLine().trim().toLowerCase();
            switch (select) {
                case "1":
                    new ManagementKiosk(repo, sc).start();
                    break;
                case "2":
                    new BorrowKiosk(repo, sc).start();
                    break;
                case "0":
                    System.out.println("프로그램을 종료합니다.");
                    return; // while문 완전히 종료
                default:
                    System.out.println("잘못된 입력입니다. 다시 시도해주세요.");
                    break;
            }
        }
    }
}
