package com.github.project1.reimburs;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.github.project1.common.datasource.ConnectionFactory;
import com.github.project1.common.exceptions.DataSourceException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReimbDAO {

    private static Logger logger = LogManager.getLogger(ReimbDAO.class);
    
    private final String baseSelect = "SELECT er.reimb_id, er.amount, er.submitted, er.resolved, er.description, er.author_id, er.resolver_id, er.status_id, er.type_id, ers.status, ert.type, eu.user_id " +
                                      "FROM ers_reimbursements er " +
                                      "JOIN ers_reimbursement_statuses ers " +
                                      "ON er.status_id = ers.status_id " +
                                      "JOIN ers_reimbursement_types ert " +
                                      "ON er.type_id = ert.type_id " +
                                      "JOIN ers_users eu " +
                                      "ON er.author_id = eu.user_id " +
                                      "LEFT JOIN ers_users " + "ON er.resolver_id = eu.user_id ";
                                      

    public List<Reimbursements> getAllReimbs() {

        logger.info("Attempting to connect to the database at {}", LocalDateTime.now());

        //Can't search for a null resolved column, resolver column; possibly by deleting the resolver and resolved from the reimbursements constructor?
        List<Reimbursements> allReimbs = new ArrayList<>();

        String sql = baseSelect;
                                  

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            allReimbs = mapResultSet(rs);
            logger.info("Successful database connection at {}", LocalDateTime.now());

        } catch (SQLException e) {
            System.err.println("Something went wrong when connection to database.");
            e.printStackTrace();
            logger.fatal("Unsuccessful database connection at {}, error message: {}", LocalDateTime.now(), e.getMessage());
        }

        return allReimbs;
    }

    public Optional<Reimbursements> findReimbById(String reimbId) {

        logger.info("Attempting to search by reimbursement id at {}", LocalDateTime.now());
        
        String sql = baseSelect + "WHERE er.reimb_id = ? ";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, UUID.fromString(reimbId));
            ResultSet rs = pstmt.executeQuery();

            
            Optional<Reimbursements> _reimb = mapResultSet(rs).stream().findFirst();

            if (_reimb.isPresent()) {
                logger.info("Reimbursement found by reimbursement id at {}", LocalDateTime.now());
            }

            return _reimb;

        } catch (SQLException e) {
            logger.warn("Unable to process reimbursement id search at {}, error message: {}", LocalDateTime.now(), e.getMessage());
            e.printStackTrace();
            throw new DataSourceException(e);
        }

    }

    public Optional<Reimbursements> findReimbByStatus(String statusId) {
        
        logger.info("Attempting to search by reimbursement status at {}", LocalDateTime.now());

        String sql = baseSelect + "WHERE er.status_id = ? ";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            logger.info("Reimbursement found by status at {}", LocalDateTime.now());
            // JDBC Statement objects are vulnerable to SQL injection
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, statusId);
            ResultSet rs = pstmt.executeQuery();
            
            return mapResultSet(rs).stream().findFirst();


        } catch (SQLException e) {
            logger.warn("Unable to process reimbursement status search at {}, error message: {}", LocalDateTime.now(), e.getMessage());
            e.printStackTrace();
            throw new DataSourceException(e);
        }
    }

    public String save(Reimbursements newReimbursement) {

        logger.info("Attempting to persist new reimbursement at {}", LocalDateTime.now());
        String sql = "INSERT INTO ers_reimbursements (amount, description, author_id, status_id, type_id) " +
                     "VALUES (?, ?, ?, 'PENDING', ?)";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            logger.info("New reimbursement successfully persisted at {}", LocalDateTime.now());
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
            logger.warn("Unable to persist data at {}, error message: {}", LocalDateTime.now(), e.getMessage());
            e.printStackTrace();
        }

        return newReimbursement.getReimbId();

    }

    private List<Reimbursements> mapResultSet(ResultSet rs) throws SQLException {

        List<Reimbursements> reimbursements = new ArrayList<>();
        while (rs.next()) {
            logger.info("Attempting to map the result set of reimbursement info at {}", LocalDateTime.now());
            Reimbursements reimbursement = new Reimbursements();
            reimbursement.setReimbId(rs.getString("reimb_id"));
            reimbursement.setAmount(rs.getFloat("amount"));
            reimbursement.setSubmitted(rs.getTimestamp("submitted").toLocalDateTime());
            Timestamp resolvedTs = rs.getTimestamp("resolved");
            reimbursement.setResolved(resolvedTs == null ? null : resolvedTs.toLocalDateTime());
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
        logger.info("Attempting to update reimbursement info at {}", LocalDateTime.now());
        System.out.println(reimb);
        String sql = "UPDATE ers_reimbursements " +
                     "SET amount = ?, description = ?, type_id = ? " +
                     "WHERE reimb_id = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setFloat(1, reimb.getAmount());
            pstmt.setString(2, reimb.getDescription());
            pstmt.setString(3, reimb.getTypeId());
            pstmt.setObject(4, UUID.fromString(reimb.getReimbId()));
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated != 1) {
                System.out.println("Sorry we didnt actually update anything.");
            }
        } catch (SQLException e) {
            logger.warn("Unable to persist updated reimbursement at {}", LocalDateTime.now());
            e.printStackTrace();
        }
    }

    public void updateStatus(Reimbursements newStatus) {

        logger.info("Attempting to update reimbursement status at {}", LocalDateTime.now());
        String sql = "UPDATE ers_reimbursements " +
                     "SET resolved = now(), resolver_id = ?, status_id = ?  " +
                     "WHERE reimb_id = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, UUID.fromString(newStatus.getResolverId()));
            pstmt.setString(2, newStatus.getStatusId());
            pstmt.setObject(3, UUID.fromString(newStatus.getReimbId()));
            pstmt.executeUpdate();
        
        } catch (SQLException e) {
            logger.warn("Unable to persist updated reimbursement status at {}, error message: {}", LocalDateTime.now(), e.getMessage());
            e.printStackTrace();
        }
    }

}
