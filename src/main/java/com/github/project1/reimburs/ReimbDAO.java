package com.github.project1.reimburs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import com.github.project1.common.datasource.ConnectionFactory;
import com.github.project1.common.exceptions.DataSourceException;

public class ReimbDAO {

    private final String baseSelect = "SELECT er.reimb_id, er.amount, er.submitted, er.resolved, er.description, er.payment_id, er.author_id, er.resolver_id, er.status_id, er.type_id, ers.status, ert.type " +
                                      "FROM ers_reimbursements er " +
                                      "JOIN ers_reimbursement_statuses ers " +
                                      "ON er.status_id = ers.status_id " +
                                      "JOIN ers_reimbursement_types ert " +
                                      "ON er.type_id = ert.type_id ";

    public List<Reimbursements> getAllReimbs() {

        List<Reimbursements> allReimbs = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            Statement stmt = conn.createStatement();
            ResultSet rs = ((java.sql.Statement) stmt).executeQuery(baseSelect);

            allReimbs = mapResultSet(rs);

        } catch (SQLException e) {
            System.err.println("Something went wrong when connection to database.");
            e.printStackTrace();
        }

        return allReimbs;
    }

    public Optional<Reimbursements> findReimbById(String reimbId) {
        
        String sql = baseSelect + "WHERE er.reimb_id = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, reimbId);
            ResultSet rs = pstmt.executeQuery();
            return mapResultSet(rs).stream().findFirst();


        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataSourceException(e);
        }

    }

    public Optional<Reimbursements> findReimbByStatus(String status) {
        
        String sql = baseSelect + "WHERE er.status = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, status);
            ResultSet rs = pstmt.executeQuery();
            return mapResultSet(rs).stream().findFirst();


        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataSourceException(e);
        }
    }

    public Optional<Reimbursements> findReimbByType(String type) {
        
        String sql = baseSelect + "WHERE er.type = ?";
        
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, type);
            ResultSet rs = pstmt.executeQuery();
            return mapResultSet(rs).stream().findFirst();


        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataSourceException(e);
        }
    }

    public String save(Reimbursements newReimbursement) {

        String sql = "INSERT INTO ers_reimbursements (amount, description, author_id, status_id, type_id) " +
                     "VALUES (?, ?, ?, 'PENDING', ?)";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql, new String[] {"reimb_id"});
            pstmt.setInt(1, newReimbursement.getAmount());
            pstmt.setString(3, newReimbursement.getDescription());
            pstmt.setString(4, newReimbursement.getAuthorId());
            pstmt.setString(5, newReimbursement.getTypeId());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            newReimbursement.setReimbId(rs.getString("reimb_id"));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return newReimbursement.getReimbId();

    }

    private List<Reimbursements> mapResultSet(ResultSet rs) throws SQLException {

        List<Reimbursements> reimbursements = new ArrayList<>();

        while (rs.next()) {
            Reimbursements reimbursement = new Reimbursements();
            reimbursement.setReimbId(rs.getString("reimb_id"));
            reimbursement.setAmount(rs.getInt("amount"));
            reimbursement.setSubmitted(rs.getString("submitted"));
            reimbursement.setResolved(rs.getString("resolved"));
            reimbursement.setDescription(rs.getString("description"));
            reimbursement.setAuthorId("author_id");
            reimbursement.setResolverId("resolver_id");
            reimbursement.setStatusId("status_id");
            reimbursement.setTypeId("type_id");
            reimbursements.add(reimbursement);
        }

        return reimbursements;
    }

    public void updateReimb(Reimbursements reimb) {
        String sql = "UPDATE ers_reimbursements " +
                     "SET amount = ?, description = ?, status_id = ?, resolver_id = ? " +
                     "WHERE author_id = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reimb.getAmount());
            pstmt.setString(2, reimb.getDescription());
            pstmt.setString(1, reimb.getStatusId());
            pstmt.setString(2, reimb.getResolverId());
            pstmt.setString(3, reimb.getAuthorId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
