package edu.ptithcm.controller;

import java.util.List;
import edu.ptithcm.dto.request.shift.ShiftRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.info_models.ShiftInfo;
import edu.ptithcm.services.ShiftService;
import edu.ptithcm.utils.handle_exception.SafeExecutor;

public class ShiftController {

    private final ShiftService service = new ShiftService();

    public ResponseDTO<List<ShiftInfo>> getAllShifts() {
        return SafeExecutor.run(() -> service.getAllShifts());
    }

    public ResponseDTO<ShiftInfo> getShiftsById(ShiftRequestDTO req) {
        return SafeExecutor.run(() -> service.getShiftsById(req));
    }

    public ResponseDTO<List<ShiftInfo>> getShiftsByName(String keyword) {
        return SafeExecutor.run(() -> service.getShiftsByName(keyword));
    }

    public ResponseDTO<ShiftInfo> createShift(ShiftRequestDTO req) {
        return SafeExecutor.run(() -> service.createShift(req));
    }

    public ResponseDTO<ShiftInfo> updateShift(ShiftRequestDTO req) {
        return SafeExecutor.run(() -> service.updateShift(req));
    }

    public ResponseDTO<ShiftInfo> deleteShift(ShiftRequestDTO req) {
        return SafeExecutor.run(() -> service.deleteShift(req));
    }
}
