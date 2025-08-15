package min.librarymanagementsystem.book;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
public class Book {
    private final String isbn;
    private String title;
    private String author;
    private Category category;
    private int stock;         // 보유 권수
    private int borrowed;      // 대출 중 권수
    private boolean available; // 보유 권수 - 대출 권수 = 0 이면 false로

    public Book(String isbn, String title, String author, Category category, int stock) {
        this.isbn = Objects.requireNonNull(isbn);
        this.title = title;
        this.author = author;
        this.category = category;
        this.stock = stock;
        this.borrowed = 0;
        this.available = isAvailable();
    }

    public boolean isAvailable() { return stock - borrowed > 0; }

    public void borrow() {
        if (!isAvailable()) throw new IllegalStateException("재고 없음");
        borrowed++;
    }

    public void returnBook() {
        if (borrowed <= 0) throw new IllegalStateException("대출 중 아님");
        borrowed--;
    }

    /** 확장: 카테고리별 연체료 계산 */
    public int lateFee(LocalDate today, LocalDate dueDate) {
        if (today.isBefore(dueDate) || today.isEqual(dueDate)) return 0;
        long days = java.time.temporal.ChronoUnit.DAYS.between(dueDate, today);
        return (int) (days * category.lateFeePerDay());
    }


    @Override
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", available=" + available +
                '}';
    }

    public void setAuthor(String a) {
    }

    public void setTitle(String t) {
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
