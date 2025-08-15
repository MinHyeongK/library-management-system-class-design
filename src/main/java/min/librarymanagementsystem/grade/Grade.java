package min.librarymanagementsystem.grade;

public enum Grade {
    BASIC(3, 14),
    SILVER(5, 21),
    GOLD(7, 28),
    STAFF(20, 60);

    private final int borrowLimit;
    private final int loanPeriodDays;

    Grade(int borrowLimit, int loanPeriodDays) {
        this.borrowLimit = borrowLimit;
        this.loanPeriodDays = loanPeriodDays;
    }
    public int borrowLimit() { return borrowLimit; }
    public int loanPeriodDays() { return loanPeriodDays; }
}
