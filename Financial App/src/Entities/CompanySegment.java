package Entities;

public enum CompanySegment {

    SMALL("< 90 mln. UAH"),
    MEDIUM("from 90 to 400 mln. UAH"),
    LARGE("> 400 mln. UAH");

    String incomeSize;

    CompanySegment(String incomeSize) {
        this.incomeSize = incomeSize;
    }

    public static CompanySegment getSegment(int income) {
        if (income < 90000) {
            return SMALL;
        }
        if (income <= 400000) {
            return MEDIUM;
        }
        return LARGE;
    }


    @Override
    public String toString() {
        return incomeSize;
    }
}
