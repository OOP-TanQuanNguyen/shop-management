package edu.ptithcm.routes.employee;

import java.util.List;
import java.util.Map;

import edu.ptithcm.configs.Role;
import edu.ptithcm.configs.TypeDTTP;
import edu.ptithcm.controller.EmployeeController;
import edu.ptithcm.dto.request.employee.EmployeeRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.info_models.EmployeeInfo;
import edu.ptithcm.middleware.AuthenMiddleWare;
import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.protocols.DTTPStateManager;
import edu.ptithcm.utils.ReplyUtils;


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
        server.on(TypeDTTP.EMPLOYEE_GET_ALL.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.EMPLOYEE_GET_ALL.getValue())) return;

                ResponseDTO<List<EmployeeInfo>> response = controller.getAllEmployees();
                List<Map<String, Object>> employees = null;
                if (response.getData() != null) {
                    employees = response.getData()
                                        .stream()
                                        .map(EmployeeInfo::toMap)
                                        .toList();
                }

                Map<String, Object> payload = Map.of("employees", employees);
                args.reply(TypeDTTP.EMPLOYEE_GET_ALL.getValue(), payload, response.getStatus(), response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.EMPLOYEE_GET_ALL.getValue(), e);
            }
        });

        // ---------------- GET ACTIVE ----------------
        server.on(TypeDTTP.EMPLOYEE_GET_ACTIVE.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.EMPLOYEE_GET_ACTIVE.getValue())) return;

                ResponseDTO<List<EmployeeInfo>> response = controller.getAllEmployeesActive();
                List<Map<String, Object>> employees = null;
                if (response.getData() != null) {
                    employees = response.getData()
                                        .stream()
                                        .map(EmployeeInfo::toMap)
                                        .toList();
                }

                Map<String, Object> payload = Map.of("employees", employees);
                args.reply(TypeDTTP.EMPLOYEE_GET_ACTIVE.getValue(), payload, response.getStatus(), response.getMessage());

            }catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.EMPLOYEE_GET_ACTIVE.getValue(), e);
            }
        });

        // ---------------- CREATE ----------------
        server.on(TypeDTTP.EMPLOYEE_CREATE.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.EMPLOYEE_CREATE.getValue())) return;

                EmployeeRequestDTO request = new EmployeeRequestDTO(args.data);
                ResponseDTO<EmployeeInfo> response = controller.createEmployee(request);

                args.reply(TypeDTTP.EMPLOYEE_CREATE.getValue(),
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.EMPLOYEE_CREATE.getValue(), e);
            }
        });

        // ---------------- UPDATE ----------------
        server.on(TypeDTTP.EMPLOYEE_UPDATE.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.EMPLOYEE_UPDATE.getValue())) return;

                EmployeeRequestDTO request = new EmployeeRequestDTO(args.data);
                ResponseDTO<EmployeeInfo> response = controller.updateEmployee(request);

                args.reply(TypeDTTP.EMPLOYEE_UPDATE.getValue(),
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.EMPLOYEE_UPDATE.getValue(), e);
            }
        });

        // ---------------- DELETE ----------------
        server.on(TypeDTTP.EMPLOYEE_DELETE.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.EMPLOYEE_DELETE.getValue())) return;

                EmployeeRequestDTO request = new EmployeeRequestDTO(args.data);
                ResponseDTO<EmployeeInfo> response = controller.deleteEmployee(request);

                args.reply(TypeDTTP.EMPLOYEE_DELETE.getValue(), null, response.getStatus(), response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.EMPLOYEE_DELETE.getValue(), e);
            }
        });

        // ---------------- FILTER ----------------

        server.on(TypeDTTP.EMPLOYEE_FILTER.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.EMPLOYEE_FILTER.getValue())) return;

                Map<String, Object> filters = (Map<String, Object>)args.data;
                ResponseDTO<List<EmployeeInfo>> response = controller.filterEmployees(filters);
                List<Map<String, Object>> employees = null;
                if (response.getData() != null) {
                    employees = response.getData()
                                        .stream()
                                        .map(EmployeeInfo::toMap)
                                        .toList();
                }

                Map<String, Object> payload = Map.of("employees", employees);
                args.reply(TypeDTTP.EMPLOYEE_FILTER.getValue(), payload, response.getStatus(), response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.EMPLOYEE_FILTER.getValue(), e);
            }
        });
    }
}
