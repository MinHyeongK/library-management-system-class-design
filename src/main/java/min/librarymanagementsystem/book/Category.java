package min.librarymanagementsystem.book;

public enum Category {
    FICTION(100),         // 하루 100원
    NONFICTION(100),
    SCIENCE(200),
    HISTORY(150),
    CHILDREN(50);

    private final int lateFeePerDay;

    Category(int lateFeePerDay) { this.lateFeePerDay = lateFeePerDay; }
    public int lateFeePerDay() { return lateFeePerDay; }
}