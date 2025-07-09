package util;

public class DBQueryBuilder {

    public static String insert(String table, String[] fields) {
        StringBuilder sb = new StringBuilder("INSERT INTO " + table + " (");
        for (int i = 0; i < fields.length; i++) {
            sb.append(fields[i]);
            if (i < fields.length - 1) sb.append(", ");
        }
        sb.append(") VALUES (");
        for (int i = 0; i < fields.length; i++) {
            sb.append("?");
            if (i < fields.length - 1) sb.append(", ");
        }
        sb.append(")");
        return sb.toString();
    }

    public static String update(String table, String[] fields, String condition) {
        StringBuilder sb = new StringBuilder("UPDATE " + table + " SET ");
        for (int i = 0; i < fields.length; i++) {
            sb.append(fields[i]).append(" = ?");
            if (i < fields.length - 1) sb.append(", ");
        }
        sb.append(" WHERE ").append(condition);
        return sb.toString();
    }

    public static String delete(String table, String condition) {
        return "DELETE FROM " + table + " WHERE " + condition;
    }

    public static String selectBy(String table, String condition) {
        return "SELECT * FROM " + table + " WHERE " + condition;
    }

    public static String selectAll(String table) {
        return "SELECT * FROM " + table;
    }
}
