package min.librarymanagementsystem.member;

import lombok.Getter;
import lombok.Setter;
import min.librarymanagementsystem.grade.Grade;
import min.librarymanagementsystem.book.Book;

import java.util.Objects;

@Getter
@Setter
public class Member {
    private final String memberId;
    private String password;
    private String name;
    private Grade grade;

    public Member(String memberId, String password, String name, Grade grade) {
        this.memberId = Objects.requireNonNull(memberId);
        this.password = Objects.requireNonNull(password);
        this.name = Objects.requireNonNull(name);
        this.grade = Objects.requireNonNull(grade);
    }

    public boolean checkPassword(String pw) {
        return this.password.equals(pw);
    }

    public boolean registerBook(Book book){
        // 도서 예약 메서드 구현
        if (!book.isAvailable()){
            System.out.println("이미 대출 중인 도서입니다.");
            return false;
        }
        book.borrow();
        System.out.println(name + "님께서 \"" + book.getTitle() + "\"을(를) 대출했습니다.");
        return true;
    }

    public boolean canBorrow(long currentBorrowingCount) {
        return currentBorrowingCount < grade.borrowLimit();
    }

    public boolean returnBook(Book book){
        // 도서 반납 메서드 구현
        if (book.isAvailable()){
            System.out.println("이미 반납된 도서입니다.");
            return false;
        }
        book.returnBook();
        System.out.println(name + "님께서 \"" + book.getTitle() + "\"을(를) 반납했습니다.");
        return true;
    }

}
