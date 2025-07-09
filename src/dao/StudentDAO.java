package dao;

import util.DBHelper;
import util.DBQueryBuilder;
import model.Student;
import java.util.List;

public class StudentDAO {

    private final DBHelper.SQLMapper<Student> mapper = rs -> new Student(
        rs.getInt("id"),
        rs.getString("name"),
        rs.getString("email"),
        rs.getString("course"),
        rs.getInt("age")
    );

    private static final String TABLE = "students";
    private static final String[] FIELDS = {"name", "email", "course", "age"};

    public boolean addStudent(Student student) {
        String sql = DBQueryBuilder.insert(TABLE, FIELDS);
        return DBHelper.executeUpdate(sql, stmt -> {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            stmt.setString(3, student.getCourse());
            stmt.setInt(4, student.getAge());
        }) > 0;
    }

    public boolean updateStudent(Student student) {
        String sql = DBQueryBuilder.update(TABLE, FIELDS, "id = ?");
        return DBHelper.executeUpdate(sql, stmt -> {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            stmt.setString(3, student.getCourse());
            stmt.setInt(4, student.getAge());
            stmt.setInt(5, student.getId());
        }) > 0;
    }

    public boolean deleteStudent(int id) {
        String sql = DBQueryBuilder.delete(TABLE, "id = ?");
        return DBHelper.executeUpdate(sql, stmt -> stmt.setInt(1, id)) > 0;
    }

    public Student getById(int id) {
        String sql = DBQueryBuilder.selectBy(TABLE, "id = ?");
        return DBHelper.executeSelectOne(sql, stmt -> stmt.setInt(1, id), mapper);
    }

    public List<Student> getAll() {
        String sql = DBQueryBuilder.selectAll(TABLE);
        return DBHelper.executeSelectList(sql, stmt -> {}, mapper);
    }

    public List<Student> getByStringField(String field, String value) {
        String sql = DBQueryBuilder.selectBy(TABLE, field + " = ?");
        return DBHelper.executeSelectList(sql, stmt -> stmt.setString(1, value), mapper);
    }

    public List<Student> getByIntField(String field, int value) {
        String sql = DBQueryBuilder.selectBy(TABLE, field + " = ?");
        return DBHelper.executeSelectList(sql, stmt -> stmt.setInt(1, value), mapper);
    }
}