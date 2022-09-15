package com.github.project1.reimburs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.github.project1.common.datasource.ConnectionFactory;
import com.github.project1.common.exceptions.DataSourceException;

public class ReimbDAO {

    private final String baseSelect = "SELECT er.reimb_id, er.amount, er.submitted, er.resolved, er.description, er.payment_id, er.author_id, er.resolver_id, er.status_id, er.type_id, ers.status, ert.type, eu.user_id " +
                                      "FROM ers_reimbursements er " +
                                      "JOIN ers_reimbursement_statuses ers " +
                                      "ON er.status_id = ers.status_id " +
                                      "JOIN ers_reimbursement_types ert " +
                                      "ON er.type_id = ert.type_id " +
                                      "JOIN ers_reimbursement_users eu " +
                                      "ON er.author_id = eu.user_id " +
                                      "JOIN ers_reimbursement_users " +
                                      "ON er.resolver_id = eu.user_id ";

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
            pstmt.setFloat(1, newReimbursement.getAmount());
            pstmt.setString(2, newReimbursement.getDescription());
            pstmt.setObject(3, UUID.fromString(newReimbursement.getAuthorId()));
            pstmt.setString(4, newReimbursement.getTypeId());

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
            reimbursement.setAmount(rs.getFloat("amount"));
            reimbursement.setSubmitted(rs.getTimestamp("submitted").toLocalDateTime());
            reimbursement.setResolved(rs.getTimestamp("resolved").toLocalDateTime());
            reimbursement.setDescription(rs.getString("description"));
            reimbursement.setAuthorId(rs.getString("author_id"));
            reimbursement.setResolverId(rs.getString("resolver_id"));
            reimbursement.setStatusId(rs.getString("status_id"));
            reimbursement.setTypeId(rs.getString("type_id"));
            reimbursements.add(reimbursement);
        }

        return reimbursements;
    }

    public void updateReimb(Reimbursements reimb) {
        String sql = "UPDATE ers_reimbursements " +
                     "SET amount = ?, description = ?, type_id = ? " +
                     "WHERE author_id = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setFloat(1, reimb.getAmount());
            pstmt.setString(2, reimb.getDescription());
            pstmt.setString(3,reimb.getTypeId());
            pstmt.setObject(4, UUID.fromString(reimb.getAuthorId()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
