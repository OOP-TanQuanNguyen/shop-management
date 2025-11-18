package edu.ptithcm.controller;

import java.util.List;
import edu.ptithcm.dto.request.shift.ShiftRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.info_models.ShiftInfo;
import edu.ptithcm.services.ShiftService;

public class ShiftController {

    private final ShiftService service = new ShiftService();

    public ResponseDTO<List<ShiftInfo>> getAllShifts() {
        return service.getAllShifts();
    }

    public ResponseDTO<ShiftInfo> getShiftById(ShiftRequestDTO req) {
        return service.getShiftById(req);
    }

    public ResponseDTO<ShiftInfo> createShift(ShiftRequestDTO req) {
        return service.createShift(req);
    }

    public ResponseDTO<ShiftInfo> updateShift(ShiftRequestDTO req) {
        return service.updateShift(req);
    }

    public ResponseDTO<ShiftInfo> deleteShift(ShiftRequestDTO req) {
        return service.deleteShift(req);
    }
}
