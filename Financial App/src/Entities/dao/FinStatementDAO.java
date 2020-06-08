package Entities.dao;

import Entities.FinStatement;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class FinStatementDAO {

    private final String readStatementsQuery = "SELECT * FROM 'table' WHERE company_code = ?;";

    private ArrayList<String> finStatementTables;

    public FinStatementDAO() {

        finStatementTables = new ArrayList<>();

        try (Connection conn = SQLiteManager.getConnection()) {
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getTables(null, null, "%", null);
            String tableName;
            while (rs.next()) {
                tableName = rs.getString(3);
                if (tableName.matches("[12][09][0-9][0-9]_fin_statement.*")) {
                    finStatementTables.add(tableName);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public HashMap<Integer, FinStatement> readStatements(int companyCode) {

        HashMap<Integer, FinStatement> companyStatements = new HashMap<>(); // year - FinStatement

        try (Connection conn = SQLiteManager.getConnection()) {
            finStatementTables.forEach(tableName -> {
                try (PreparedStatement preparedStatement = conn.prepareStatement(readStatementsQuery.replace("table", tableName))) {
                    preparedStatement.setInt(1, companyCode);
                    try (ResultSet finStatementsData = preparedStatement.executeQuery()) {
                        while (finStatementsData.next()) {
                            companyStatements.put(
                                    Integer.parseInt(tableName.split("_")[0]),
                                    new FinStatement(
                                            finStatementsData.getInt("income"),
                                            finStatementsData.getInt("net_profit"),
                                            finStatementsData.getInt("fin_expenses"),
                                            finStatementsData.getInt("cash_and_equiv"),
                                            finStatementsData.getInt("cur_assets"),
                                            finStatementsData.getInt("loans_sum"),
                                            finStatementsData.getBoolean("policy")
                                    )
                            );
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return companyStatements;
    }
}
