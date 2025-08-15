package min.librarymanagementsystem.kiosk;

import lombok.RequiredArgsConstructor;
import min.librarymanagementsystem.book.BorrowRecord;
import min.librarymanagementsystem.grade.Grade;
import min.librarymanagementsystem.repository.LibraryRepository;
import min.librarymanagementsystem.member.Member;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
public class BorrowKiosk {

    private final LibraryRepository repo;
    private final Scanner sc;
    private Member session;

    public void start() {
        while (true) {
            System.out.println("\n[대여 키오스크] 1.회원가입 2.로그인 0.종료 ( 0~2 입력 )");
            String sel = sc.nextLine().trim();

            switch (sel) {
                case "1" -> signup();
                case "2" -> {
                    if (login()) {
                        afterLogin();
                    }
                }
                case "0" -> {
                    return; // while 종료
                }
                default -> System.out.println("잘못된 입력입니다.");
            }
        }
    }


    private void signup() {
        System.out.print("아이디: "); String memberId = sc.nextLine();
        System.out.print("비밀번호: "); String password = sc.nextLine();
        System.out.print("이름: "); String name = sc.nextLine();

        if (repo.findMember(memberId).isPresent()) {
            System.out.println("가입실패. 이미 존재하는 아이디입니다.");
            return;
        } else {
            Member m = new Member(memberId, password, name, Grade.BASIC);
            System.out.println("가입완료. 회원ID = " + m.getMemberId());
            repo.addMember(m);
        }
    }

    private boolean login() {
        System.out.print("회원 아이디: "); String memberId = sc.nextLine();
        System.out.print("비밀번호: "); String pw = sc.nextLine();
        session = repo.findMember(memberId).filter(m -> m.checkPassword(pw)).orElse(null);
        System.out.println(session == null ? "로그인 실패" : "로그인 성공: ");
        return session != null;
    }

    private void afterLogin() {
        while (true) {
            System.out.println("\n[사용자 메뉴] 1.도서검색 2.도서대출 3.반납 4.내기록 9.로그아웃");
            switch (sc.nextLine().trim()) {
                case "1" -> {
                    System.out.print("검색어: "); String q = sc.nextLine();
                    repo.searchBooks(q).forEach(System.out::println);
                }
                case "2" -> {
                    System.out.print("대출 ISBN: "); String isbn = sc.nextLine();
                    try {
                        BorrowRecord r = repo.borrow(session.getMemberId(), isbn, LocalDate.now());
                        System.out.printf("대출 완료. 반납예정일: %s%n", r.getDueDate());
                    } catch (Exception e) {
                        System.out.println("대출 실패: " + e.getMessage());
                    }
                }
                case "3" -> {
                    System.out.print("반납 ISBN: "); String isbn = sc.nextLine();
                    try {
                        int fee = repo.returnBook(session.getMemberId(), isbn, LocalDate.now());
                        if (fee > 0) System.out.printf("반납 완료(연체료 %d원).%n", fee);
                        else System.out.println("반납 완료.");
                    } catch (Exception e) {
                        System.out.println("반납 실패: " + e.getMessage());
                    }
                }
                case "4" -> {
                    List<BorrowRecord> list = repo.recordsOfMember(session.getMemberId());
                    if (list.isEmpty()) System.out.println("대출 기록 없음");
                    else list.forEach(System.out::println);
                }
                case "9" -> { session = null; System.out.println("로그아웃"); return; }
                default -> System.out.println("메뉴 선택 오류");
            }
        }
    }
}
