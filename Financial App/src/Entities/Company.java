package Entities;

import javafx.util.Pair;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class Company {

    private int registrationCode;
    private String name;
    private String directorName;
    private LocalDate directorBirthDate;
    private CompanySegment segment;
    private HashMap<Integer, FinStatement> finStatementsByYear;

    private int lastReportYear;

    public Company(int registrationCode, String name, String directorName, LocalDate directorBirthDate) {
        this.registrationCode = registrationCode;
        this.name = name;
        this.directorName = directorName;
        this.directorBirthDate = directorBirthDate;
    }

    public void setFinStatements(HashMap<Integer, FinStatement> finStatementsByYear) {
        this.finStatementsByYear = finStatementsByYear;
        Optional<Integer> lastYear = finStatementsByYear.keySet().stream().max(Integer::compareTo);
        if (lastYear.isPresent()) {
            lastReportYear =  lastYear.get();
            this.segment = CompanySegment.getSegment(finStatementsByYear.get(lastReportYear).getIncome());
        }
    }

    public int getRegistrationCode() {
        return registrationCode;
    }

    public CompanySegment getSegment() {
        return segment;
    }

    public String getName() {
        return name;
    }

    public String getDirectorName() {
        return directorName;
    }

    public LocalDate getDirectorBirthDate() {
        return directorBirthDate;
    }

    public double calculateCreditRating(int maxLoansSum, int maxProfit) {

        double creditRating;
        FinStatement statement = getLastYearStatement();

        creditRating = statement.getLoansSum()/(double)maxLoansSum * 3;

        if (statement.getProfit() > 0){
            creditRating += statement.getProfit()/(double)maxProfit;
        }

        return creditRating;
    }

    public ArrayList<Pair<Integer, FinStatement>> getAllStatements() {
        ArrayList<Pair<Integer, FinStatement>> yearStatementPairs = new ArrayList<>(finStatementsByYear.size());
        finStatementsByYear.forEach((year, statement) -> yearStatementPairs.add(new Pair<>(year, statement)));
        return yearStatementPairs;

    }

    public FinStatement getStatement(int year) {
        return finStatementsByYear.get(year);
    }

    public FinStatement getLastYearStatement() {
        return finStatementsByYear.get(lastReportYear);
    }

    public boolean hasStatements() {
        return !(finStatementsByYear.isEmpty());
    }

}
