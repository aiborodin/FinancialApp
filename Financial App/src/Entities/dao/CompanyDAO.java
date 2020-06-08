package Entities.dao;

import Entities.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CompanyDAO {

    private static final FinStatementDAO finstatementDAO = new FinStatementDAO();

    private final String readCompaniesQuery = "SELECT * FROM companies;";

    public ArrayList<Company> getCompanies() {

        ArrayList<Company> companies = new ArrayList<>();

        try (Connection connection = SQLiteManager.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(readCompaniesQuery)) {
                try (ResultSet companiesData = statement.executeQuery()) {
                    int regCode;
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    while (companiesData.next()) {
                        regCode = companiesData.getInt(1);
                        companies.add(new Company(
                                        regCode,
                                        companiesData.getString("name"),
                                        companiesData.getString("director_name"),
                                        LocalDate.parse(companiesData.getString("director_birth_date"), formatter)
                                )
                        );
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }

        companies.forEach(
                company -> company.setFinStatements(finstatementDAO.readStatements(company.getRegistrationCode()))
        );

        return companies;
    }

}
