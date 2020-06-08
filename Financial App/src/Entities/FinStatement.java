package Entities;

public class FinStatement {

    private int income;
    private int profit;
    private int finExpenses;
    private int cashAndEquivalents;
    private int currentAssets;
    private int loansSum;
    private boolean bankPolicy;

    public FinStatement(int income, int profit, int finExpenses, int cashAndEquivalents, int currentAssets, int loansSum, boolean bankPolicy) {
        this.income = income;
        this.profit = profit;
        this.finExpenses = finExpenses;
        this.cashAndEquivalents = cashAndEquivalents;
        this.currentAssets = currentAssets;
        this.loansSum = loansSum;
        this.bankPolicy = bankPolicy;
    }

    public int getIncome() {
        return income;
    }

    public int getProfit() {
        return profit;
    }

    public int getFinExpenses() {
        return finExpenses;
    }

    public int getCashAndEquivalents() {
        return cashAndEquivalents;
    }

    public int getCurrentAssets() {
        return currentAssets;
    }

    public int getLoansSum() {
        return loansSum;
    }

    public boolean isUnderBankPolicy() {
        return bankPolicy;
    }
}
