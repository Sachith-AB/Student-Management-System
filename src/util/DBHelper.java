package util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBHelper {

    // Reusable SELECT query executor
    public static ResultSet executeQuery(String sql, SQLConsumer<PreparedStatement> consumer) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            if (consumer != null) consumer.accept(stmt);
            return stmt.executeQuery(); // Caller must close ResultSet + Connection
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Reusable INSERT/UPDATE/DELETE executor
    public static int executeUpdate(String sql, SQLConsumer<PreparedStatement> consumer) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (consumer != null) consumer.accept(stmt);
            return stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Functional interface for lambda use
    @FunctionalInterface
    public interface SQLConsumer<T> {
        void accept(T t) throws SQLException;
    }

    // Mapper interface for ResultSet to object
    @FunctionalInterface
    public interface SQLMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    // Execute SELECT and return a single object
    public static <T> T executeSelectOne(String sql, SQLConsumer<PreparedStatement> consumer, SQLMapper<T> mapper) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (consumer != null) consumer.accept(stmt);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapper.map(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Execute SELECT and return a list of objects
    public static <T> List<T> executeSelectList(String sql, SQLConsumer<PreparedStatement> consumer, SQLMapper<T> mapper) {
        List<T> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (consumer != null) consumer.accept(stmt);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapper.map(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
