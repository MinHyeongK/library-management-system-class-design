package min.librarymanagementsystem.book;

import lombok.Getter;
import min.librarymanagementsystem.member.Member;

import java.time.LocalDate;

@Getter
public class BorrowRecord {
    private final Book book;
    private final Member member;
    private final LocalDate borrowDate;
    private final LocalDate dueDate;
    private LocalDate returnDate; // null이면 미반납

    public BorrowRecord(Book book, Member member, LocalDate borrowDate, int periodDays) {
        this.book = book;
        this.member = member;
        this.borrowDate = borrowDate;
        this.dueDate = borrowDate.plusDays(periodDays);
    }

    public boolean isReturned() { return returnDate != null; }
    public boolean isOverdue(LocalDate today) {
        return !isReturned() && today.isAfter(dueDate);
    }

    public int calcLateFee(LocalDate today) {
        LocalDate base = isReturned() ? returnDate : today;
        return book.lateFee(base, dueDate);
    }

    public void markReturned(LocalDate date) {
        this.returnDate = date;
    }

    @Override public String toString() {
        return String.format("Record{book=%s, member=%s, borrow=%s, due=%s, returned=%s}",
                book.getIsbn(), member.getMemberId(), borrowDate, dueDate, returnDate);
    }
}
