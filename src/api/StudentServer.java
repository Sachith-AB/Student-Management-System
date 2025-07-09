package api;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import dao.StudentDAO;
import model.Student;
import com.google.gson.Gson;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.List;

public class StudentServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        StudentDAO dao = new StudentDAO();
        Gson gson = new Gson();

        // GET all students
        server.createContext("/students", exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                List<Student> students = dao.getAll();
                String response = gson.toJson(students);
                sendJson(exchange, response);
            }
        });

        // POST to create a new student
        server.createContext("/students/create", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes());
                Student student = gson.fromJson(body, Student.class);
                boolean result = dao.addStudent(student);
                sendJson(exchange, "{\"success\": " + result + "}");
            }
        });

        // DELETE student by ID: /students/delete?id=1
        server.createContext("/students/delete", exchange -> {
            if ("DELETE".equals(exchange.getRequestMethod())) {
                String query = exchange.getRequestURI().getQuery(); // id=1
                int id = Integer.parseInt(query.split("=")[1]);
                boolean result = dao.deleteStudent(id);
                sendJson(exchange, "{\"deleted\": " + result + "}");
            }
        });

        // Start server
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on http://localhost:8080");
    }

    private static void sendJson(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
