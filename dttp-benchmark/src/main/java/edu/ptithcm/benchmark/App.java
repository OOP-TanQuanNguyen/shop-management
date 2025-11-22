package edu.ptithcm.benchmark;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import edu.ptithcm.benchmark.protocols.DTTP;
import edu.ptithcm.benchmark.protocols.DTTP.DTTPArgs;

public class App {

    static int START = 1;
    static int END = 1001;
    public static void main(String[] args) {
        try {
            DTTP client = new DTTP("localhost", 2025);

            // LISTENER: LOGIN RESPONSE
            client.on("LOGIN", (DTTPArgs res) -> {
                if (!handleLogin(res)) {
                    System.out.println("Login Fail - Reason: " + res.message);
                    return;
                }

                App.spamCreateEmployee(client);
            });

            // LISTENER: CREATE EMPLOYEE RESPONSE
            client.on("EMPLOYEE_CREATE", (DTTPArgs res) -> {
                if (handleEmployeeCreate(res)) {
                    System.out.println("Created user!");
                } else {
                    System.out.println("ERROR: " + res.message);
                }
            });

            client.listen();

            // LOGIN FIRST
            login(client);   // no error, wrapped

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // =====================================================================
    // LOGIN
    // =====================================================================

    static void login(DTTP client) {
        try {
            client.send("LOGIN", null, "REQUEST", "");
        } catch (IOException e) {
            System.out.println("ERROR sending LOGIN: " + e.getMessage());
        }
    }

    static boolean handleLogin(DTTPArgs res) {
        return "SUCCESS".equals(res.status);
    }

    // =====================================================================
    // CREATE EMPLOYEE
    // =====================================================================

    static void createEmployee(DTTP client, Map<String, Object> data) {
        try {
            client.send("EMPLOYEE_CREATE", data, "REQUEST", "");
        } catch (IOException e) {
            System.out.println("ERROR sending EMPLOYEE_CREATE: " + e.getMessage());
        }
    }

    static boolean handleEmployeeCreate(DTTPArgs res) {
        return "CREATED".equals(res.status);
    }

    // =====================================================================
    // EMPLOYEE DATA BUILDER
    // =====================================================================

    static Map<String, Object> buildEmployeeData() {
        Map<String, Object> data = new HashMap<>();

        data.put("employee_id", UUID.randomUUID().toString());
        data.put("username", randomUsername());
        data.put("password", "123456");
        data.put("name", "Benchmark User");
        data.put("phone", "0123456789");
        data.put("role", "STAFF");           // enum
        return data;
    }

    static String randomUsername() {
        return "user" + System.currentTimeMillis();
    }

    static void spamCreateEmployee(DTTP client){
        for (int i = START ;i < END ; i++){
            Map<String,Object> data = buildEmployeeData();
            createEmployee(client, data);
        }
    }
}
