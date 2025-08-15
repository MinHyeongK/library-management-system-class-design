package min.librarymanagementsystem.repository;

import min.librarymanagementsystem.book.Book;
import min.librarymanagementsystem.book.BorrowRecord;
import min.librarymanagementsystem.member.Member;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class LibraryRepository {
    private final Map<String, Book> books = new LinkedHashMap<>();
    private final Map<String, Member> members = new LinkedHashMap<>();
    private final List<BorrowRecord> records = new ArrayList<>();

    // Book CRUD
    public void addBook(Book book) { books.put(book.getIsbn(), book); }

    public Optional<Book> findBook(String isbn) { return Optional.ofNullable(books.get(isbn)); }

    public List<Book> searchBooks(String keyword) {
        String k = keyword.toLowerCase();
        return books.values().stream().filter(b ->
                b.getIsbn().toLowerCase().contains(k) ||
                        b.getTitle().toLowerCase().contains(k) ||
                        b.getAuthor().toLowerCase().contains(k) ||
                        b.getCategory().name().toLowerCase().contains(k)
        ).collect(Collectors.toList());
    }

    public boolean removeBook(String isbn) { return books.remove(isbn) != null; }

    public Collection<Book> allBooks() { return books.values(); }

    // Member CRUD
    public void addMember(Member m) { members.put(m.getMemberId(), m); }

    public Optional<Member> findMember(String memberId) { return Optional.ofNullable(members.get(memberId)); }

    public boolean removeMember(String id) { return members.remove(id) != null; }

    public Collection<Member> allMembers() { return members.values(); }

    // Borrow / Return
    public synchronized BorrowRecord borrow(String memberId, String isbn, LocalDate today) {
        Member m = findMember(memberId).orElseThrow(() -> new NoSuchElementException("회원 없음"));
        Book b = findBook(isbn).orElseThrow(() -> new NoSuchElementException("도서 없음"));

        long curBorrowing = records.stream()
                .filter(r -> r.getMember().getMemberId().equals(memberId) && !r.isReturned())
                .count();
        if (!m.canBorrow(curBorrowing)) throw new IllegalStateException("대출 한도 초과");
        if (!b.isAvailable()) throw new IllegalStateException("도서 재고 없음");

        b.borrow();
        BorrowRecord rec = new BorrowRecord(b, m, today, m.getGrade().loanPeriodDays());
        records.add(rec);
        m.registerBook(b);
        return rec;
    }

    public synchronized int returnBook(String memberId, String isbn, LocalDate today) {
        BorrowRecord rec = records.stream()
                .filter(r -> r.getMember().getMemberId().equals(memberId)
                        && r.getBook().getIsbn().equals(isbn)
                        && !r.isReturned())
                .findFirst().orElseThrow(() -> new NoSuchElementException("대출 기록 없음"));

        rec.getBook().returnBook();
        rec.markReturned(today);
        rec.getMember().returnBook(rec.getBook());
        return rec.calcLateFee(today);
    }

    public List<BorrowRecord> recordsOfMember(String memberId) {
        return records.stream()
                .filter(r -> r.getMember().getMemberId().equals(memberId))
                .collect(Collectors.toList());
    }

    public List<BorrowRecord> overdueRecords(LocalDate today) {
        return records.stream().filter(r -> r.isOverdue(today)).collect(Collectors.toList());
    }

    public List<BorrowRecord> allRecords() { return Collections.unmodifiableList(records); }
}
