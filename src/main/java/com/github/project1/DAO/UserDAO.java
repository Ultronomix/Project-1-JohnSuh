package com.github.project1.DAO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.github.project1.POJO.Role;
import com.github.project1.POJO.User;
import com.github.project1.common.datasource.ConnectionFactory;
import com.github.project1.common.exceptions.DataSourceException;

public class UserDAO {

    private final String baseSelect = "SELECT eu.user_id, eu.username, eu.email, ui.password, eu.givenName, eu.surname, eu.role_id, eur.role " +
                                      "FROM ers_users eu " +
                                      "JOIN ers_users_roles eur " +
                                      "ON eu.role_id = eur.role ";
    
    public List<User> getAllUsers() {

        List<User> allUsers = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(baseSelect);

            allUsers = mapResultSet(rs);

        } catch (SQLException e) {
            System.err.println("Something went wrong when communicating with the database");
            e.printStackTrace();
        }

        return allUsers;
    }
    
    public Optional<User> findUserById(UUID userId) {

        String sql = baseSelect + "WHERE eu.user_id = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, userId);
            ResultSet rs = pstmt.executeQuery();
            return mapResultSet(rs).stream().findFirst();
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataSourceException(e);
        }
    }

    public Optional<User> findUserByUsername(String username) {

        String sql = baseSelect + "WHERE eu.username = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, username);
            ResultSet rs = pstmt.executeQuery();
            return mapResultSet(rs).stream().findFirst();
            
        } catch (SQLException e) {
          throw new DataSourceException(e);
        }
    }

    public boolean isUsernameTaken(String username) {
        return findUserByUsername(username).isPresent();
    }

    public Optional<User> findUserByEmail(String email) {

        String sql = baseSelect + "WHERE eu.email = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, email);
            ResultSet rs = pstmt.executeQuery();
            return mapResultSet(rs).stream().findFirst();
            
        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }

    public boolean isEmailTaken(String email) {
        return findUserByEmail(email).isPresent();
    }

    public Optional<User> findUserByUsernameAndPassword(String username, String password) {

        String sql = baseSelect + "WHERE eu.username = ? AND eu.password = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, username);
            pstmt.setObject(2, password);
            ResultSet rs = pstmt.executeQuery();
            return mapResultSet(rs).stream().findFirst();
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataSourceException(e);
        }
    }

    public String save(User user) {
        String sql = "INSERT INTO ers_users (username, email, password, given_name, surname, role_id) " +
                     "VALUES (?, ?, ?, ?, ?, '')";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql, new String[] {"user_id"});
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getGivenName());
            pstmt.setString(5, user.getSurname());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            user.setUserId(rs.getString("user_id"));

        } catch (SQLException e) {
            log("ERROR", e.getMessage());
        }

        log("INFO", "Successfully persisted new user with id: " + user.getUserId());

        return user.getUserId();
    }

    private List<User> mapResultSet(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            User user = new User();
            user.setUserId(rs.getString("user_id"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("******"));
            user.setGivenName(rs.getString("given_name"));
            user.setSurname(rs.getString("surname"));
            user.setRole(new Role(rs.getString("role_id"), rs.getString("role")));
            users.add(user);
        }
        return users;
    }

    public void log(String level, String message) {
        try {
            File logFile = new File("logs/app.log");
            logFile.createNewFile();
            BufferedWriter logWriter = new BufferedWriter(new FileWriter(logFile));
            logWriter.write(String.format("[%s] at %s logged: [%s] %s\n", Thread.currentThread().getName(), LocalDate.now(), level.toUpperCase(), message));
            logWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
    }
}
