package edu.ptithcm.routes.employee;

import java.util.List;
import java.util.Map;

import edu.ptithcm.controller.EmployeeController;
import edu.ptithcm.dto.request.employee.EmployeeRequestDTO;
import edu.ptithcm.dto.response.EmployeeInfo;
import edu.ptithcm.dto.response.ResponseDTO;
import edu.ptithcm.middleware.AuthenMiddleWare;
import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.protocols.DTTPStateManager;

public class EmployeeRoute {
    private final DTTP server;
    private final DTTPStateManager manager;
    private final EmployeeController controller;

    public EmployeeRoute(DTTP server, DTTPStateManager manager) {
        this.server = server;
        this.manager = manager;
        this.controller = new EmployeeController();
    }

    public void register() {
        // ---------------- GET ALL ----------------
        server.on("EMPLOYEE_GET_ALL", args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, "ADMIN", args, "EMPLOYEE_GET_ALL")) return;

                ResponseDTO<List<EmployeeInfo>> response = controller.getAllEmployees();
                List<Map<String, Object>> employees = null;
                if (response.getData() != null) {
                    employees = response.getData()
                                        .stream()
                                        .map(EmployeeInfo::toMap)
                                        .toList();
                }

                Map<String, Object> payload = Map.of("employees", employees);
                args.reply(response.getType(), payload, response.getStatus(), response.getMessage());

            } catch (Exception e) {
                AuthenMiddleWare.replyError(args, "EMPLOYEE_GET_ALL", e);
            }
        });

        // ---------------- GET ACTIVE ----------------
        server.on("EMPLOYEE_GET_ACTIVE", args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, "ADMIN", args, "EMPLOYEE_GET_ACTIVE")) return;

                ResponseDTO<List<EmployeeInfo>> response = controller.getAllEmployeesActive();
                List<Map<String, Object>> employees = null;
                if (response.getData() != null) {
                    employees = response.getData()
                                        .stream()
                                        .map(EmployeeInfo::toMap)
                                        .toList();
                }

                Map<String, Object> payload = Map.of("employees", employees);
                args.reply(response.getType(), payload, response.getStatus(), response.getMessage());

            } catch (Exception e) {
                AuthenMiddleWare.replyError(args, "EMPLOYEE_GET_ACTIVE", e);
            }
        });

        // ---------------- CREATE ----------------
        server.on("EMPLOYEE_CREATE", args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, "ADMIN", args, "EMPLOYEE_CREATE")) return;

                EmployeeRequestDTO request = new EmployeeRequestDTO(args.data);
                ResponseDTO<EmployeeInfo> response = controller.createEmployee(request);

                args.reply(response.getType(),
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage());

            } catch (Exception e) {
                AuthenMiddleWare.replyError(args, "EMPLOYEE_CREATE", e);
            }
        });

        // ---------------- UPDATE ----------------
        server.on("EMPLOYEE_UPDATE", args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, "ADMIN", args, "EMPLOYEE_UPDATE")) return;

                EmployeeRequestDTO request = new EmployeeRequestDTO(args.data);
                ResponseDTO<EmployeeInfo> response = controller.updateEmployee(request);

                args.reply(response.getType(),
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage());

            } catch (Exception e) {
                AuthenMiddleWare.replyError(args, "EMPLOYEE_UPDATE", e);
            }
        });

        // ---------------- DELETE ----------------
        server.on("EMPLOYEE_DELETE", args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, "ADMIN", args, "EMPLOYEE_DELETE")) return;

                EmployeeRequestDTO request = new EmployeeRequestDTO(args.data);
                ResponseDTO<EmployeeInfo> response = controller.deleteEmployee(request);

                args.reply(response.getType(), null, response.getStatus(), response.getMessage());

            } catch (Exception e) {
                AuthenMiddleWare.replyError(args, "EMPLOYEE_DELETE", e);
            }
        });

        // ---------------- FILTER ----------------
        server.on("EMPLOYEE_FILTER", args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, "ADMIN", args, "EMPLOYEE_FILTER")) return;

                Map<String, Object> filters = (Map<String, Object>) args.data;
                ResponseDTO<List<EmployeeInfo>> response = controller.filterEmployees(filters);
                List<Map<String, Object>> employees = null;
                if (response.getData() != null) {
                    employees = response.getData()
                                        .stream()
                                        .map(EmployeeInfo::toMap)
                                        .toList();
                }

                Map<String, Object> payload = Map.of("employees", employees);
                args.reply(response.getType(), payload, response.getStatus(), response.getMessage());

            } catch (Exception e) {
                AuthenMiddleWare.replyError(args, "EMPLOYEE_FILTER", e);
            }
        });
    }
}
