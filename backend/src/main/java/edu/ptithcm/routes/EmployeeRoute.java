package edu.ptithcm.routes;

import edu.ptithcm.controller.EmployeeController;
import edu.ptithcm.dto.RequestObject;
import edu.ptithcm.dto.ResponseObject;
import edu.ptithcm.protocols.DTTP;

import java.io.IOException;

public class EmployeeRoute {
    private final DTTP server;
    private final EmployeeController controller;

    public EmployeeRoute(DTTP server) {
        this.server = server;
        this.controller = new EmployeeController();
    }

    public void register() {
        // Lấy danh sách toàn bộ nhân viên
        server.on("EMPLOYEE_GET_ALL", args -> {
            try {
                RequestObject req = new RequestObject(args.data);
                ResponseObject res = controller.getAllEmployees(req);
                args.reply("EMPLOYEE_GET_ALL", res.toMap(), res.getStatus(), res.getMessage());
            } catch (IOException e) { e.printStackTrace(); }
        });

        // Lấy nhân viên active
        server.on("EMPLOYEE_GET_ACTIVE", args -> {
            try {
                RequestObject req = new RequestObject(args.data);
                ResponseObject res = controller.getAllEmployeesActive(req);
                args.reply("EMPLOYEE_GET_ACTIVE", res.toMap(), res.getStatus(), res.getMessage());
            } catch (IOException e) { e.printStackTrace(); }
        });

        // Tạo nhân viên mới
        server.on("EMPLOYEE_CREATE", args -> {
            try {
                RequestObject req = new RequestObject(args.data);
                ResponseObject res = controller.createEmployee(req);
                args.reply("EMPLOYEE_CREATE", res.toMap(), res.getStatus(), res.getMessage());
            } catch (IOException e) { e.printStackTrace(); }
        });

        // Cập nhật nhân viên
        server.on("EMPLOYEE_UPDATE", args -> {
            try {
                RequestObject req = new RequestObject(args.data);
                ResponseObject res = controller.updateEmployee(req);
                args.reply("EMPLOYEE_UPDATE", res.toMap(), res.getStatus(), res.getMessage());
            } catch (IOException e) { e.printStackTrace(); }
        });

        // Xóa nhân viên
        server.on("EMPLOYEE_DELETE", args -> {
            try {
                RequestObject req = new RequestObject(args.data);
                ResponseObject res = controller.deleteEmployee(req);
                args.reply("EMPLOYEE_DELETE", res.toMap(), res.getStatus(), res.getMessage());
            } catch (IOException e) { e.printStackTrace(); }
        });

        // Lọc nhân viên
        server.on("EMPLOYEE_FILTER", args -> {
            try {
                RequestObject req = new RequestObject(args.data);
                ResponseObject res = controller.filterEmployees(req);
                args.reply("EMPLOYEE_FILTER", res.toMap(), res.getStatus(), res.getMessage());
            } catch (IOException e) { e.printStackTrace(); }
        });
    }
}
