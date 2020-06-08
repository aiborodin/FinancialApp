package Entities;

import Entities.dao.CompanyDAO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class CompanyAnalyzer {

    private static final CompanyDAO companyDAO = new CompanyDAO();

    private ArrayList<Company> companies;

    public CompanyAnalyzer() {
        this.companies = companyDAO.getCompanies();
    }

    public Stream<Company> getRecommendedCompaniesBy(BankServices serviceOption, CompanySegment companySegment, boolean underPolicy) {

        Supplier<Stream<Company>> companiesSupplier = () ->
                companies.stream()
                        .filter(company -> company.getSegment() == companySegment & company.hasStatements())
                        .filter(company -> company.getLastYearStatement().isUnderBankPolicy() == underPolicy);

        switch (serviceOption) {
            case CREDITING:
                int maxLoansSum = companiesSupplier.get()
                        .mapToInt(company -> company.getLastYearStatement().getLoansSum())
                        .max()
                        .orElse(-1);
                if (maxLoansSum == -1) {
                    return Stream.empty();
                }
                int maxProfit = companiesSupplier.get()
                        .mapToInt(company -> company.getLastYearStatement().getProfit())
                        .max()
                        .getAsInt();
                return companiesSupplier.get()
                        .sorted(Comparator.comparing(c -> c.calculateCreditRating(maxLoansSum, maxProfit), Comparator.reverseOrder())
                        );
            case DEPOSITS:
                return companiesSupplier.get()
                        .filter(company -> company.getLastYearStatement().getCashAndEquivalents() > 1000)
                        .sorted(Comparator.comparing(c -> c.getLastYearStatement().getCashAndEquivalents(), Comparator.reverseOrder()));
            case FINANCIAL_SERVICES:
                return companiesSupplier.get()
                        .filter(company -> company.getLastYearStatement().getFinExpenses() > 100)
                        .sorted(Comparator.comparing(c -> c.getLastYearStatement().getFinExpenses(), Comparator.reverseOrder()));
        }

        return Stream.empty();
    }
}
