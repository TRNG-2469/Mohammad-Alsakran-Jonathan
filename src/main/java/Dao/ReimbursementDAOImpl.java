package Dao;

import Model.Reimbursement;
import Utils.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReimbursementDAOImpl implements ReimbursementDAO {
    @Override
    public Reimbursement create(Reimbursement reimbursement) {
        String createSQL = "INSERT INTO reimbursements (status, amount, description, type, resolver_id,author_id) VALUES ( ?, ?, ?, ?, ?,?) RETURNING reimbursements_id";

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement prepStatement = conn.prepareStatement(createSQL)) {
            prepStatement.setString(1, reimbursement.getStatus());
            prepStatement.setBigDecimal(2, reimbursement.getAmount());
            prepStatement.setString(3, reimbursement.getDescription());
            prepStatement.setString(4, reimbursement.getType());
            prepStatement.setObject(5, reimbursement.getResolver_id());
            prepStatement.setInt(6, reimbursement.getAuthor_id());

            var rs = prepStatement.executeQuery();
            if (rs.next()) {
                reimbursement.setReimbursements_id(rs.getInt("reimbursements_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reimbursement;
    }
    @Override
    public Reimbursement findById(int id) {
        String findOneSQL = "SELECT * FROM reimbursements WHERE reimbursements_id = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement prepStatement = conn.prepareStatement(findOneSQL)) {
            prepStatement.setInt(1, id);
            var resultSet = prepStatement.executeQuery();

            if (resultSet.next()) {
                return mapRow(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void update(Reimbursement reimbursement) {
        String updateSQL = "UPDATE reimbursements SET amount = ?, description = ?, type = ? WHERE reimbursements_id = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement prepStatement = conn.prepareStatement(updateSQL)) {
            prepStatement.setBigDecimal(1, reimbursement.getAmount());
            prepStatement.setString(2, reimbursement.getDescription());
            prepStatement.setString(3, reimbursement.getType());
            prepStatement.setInt(4, reimbursement.getReimbursements_id());
            prepStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Reimbursement> findByAuthorId(int authorId) {
        String findSQL = "SELECT * FROM reimbursements WHERE author_id = ?";
        List<Reimbursement> results = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement prepStatement = conn.prepareStatement(findSQL)) {
            prepStatement.setInt(1, authorId);
            var resultSet = prepStatement.executeQuery();
            while (resultSet.next()) {
                results.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    @Override
    public List<Reimbursement> findAll() {
        String findAllSQL = "SELECT * FROM reimbursements";
        List<Reimbursement> results = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement prepStatement = conn.prepareStatement(findAllSQL);
             ResultSet resultSet = prepStatement.executeQuery()) {
            while (resultSet.next()) {
                results.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    @Override
    public void resolve(Reimbursement reimbursement) {
        String resolveSQL = "UPDATE reimbursements SET status = ?, resolver_id = ? WHERE reimbursements_id = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement prepStatement = conn.prepareStatement(resolveSQL)) {
            prepStatement.setString(1, reimbursement.getStatus());
            prepStatement.setObject(2, reimbursement.getResolver_id());
            prepStatement.setInt(3, reimbursement.getReimbursements_id());
            prepStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Reimbursement> findAllByStatusAndDepartment(String status, int departmentId) {
        // joins through users to reach department_id, since reimbursements
        // has no department column of its own, only author_id
        String findSQL = "SELECT r.* FROM reimbursements r " +
                "JOIN users u ON r.author_id = u.user_id " +
                "WHERE r.status = ? AND u.department_id = ?";
        List<Reimbursement> results = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement prepStatement = conn.prepareStatement(findSQL)) {
            prepStatement.setString(1, status);
            prepStatement.setInt(2, departmentId);
            var resultSet = prepStatement.executeQuery();
            while (resultSet.next()) {
                results.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    private Reimbursement mapRow(ResultSet rs) throws SQLException {
        Reimbursement r = new Reimbursement();
        r.setReimbursements_id(rs.getInt("reimbursements_id"));
        r.setStatus(rs.getString("status"));
        r.setAmount(rs.getBigDecimal("amount"));
        r.setDescription(rs.getString("description"));
        r.setType(rs.getString("type"));
        r.setResolver_id((Integer) rs.getObject("resolver_id"));
        r.setAuthor_id(rs.getInt("author_id"));
        return r;
    }
}
