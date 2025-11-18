package edu.ptithcm.services;

import java.util.List;

import edu.ptithcm.dto.request.shift.ShiftRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.error.InvalidResponse;
import edu.ptithcm.dto.response.error.NotFoundResponse;
import edu.ptithcm.dto.response.success.SuccessResponse;
import edu.ptithcm.dto.response.info_models.ShiftInfo;
import edu.ptithcm.models.ShiftModel;
import edu.ptithcm.repository.Repository;
import edu.ptithcm.repository.shift.ShiftRepository;
import edu.ptithcm.utils.mapper.BaseMapper;
import edu.ptithcm.utils.mapper.MapperFactory;

public class ShiftService {

    private static final ShiftRepository shiftRepo = Repository.shift();
    private static final BaseMapper<ShiftModel, ShiftInfo> mapper = MapperFactory.shift();

    // ------------------ Lấy tất cả shift ------------------
    public ResponseDTO<List<ShiftInfo>> getAllShifts() {
        return new SuccessResponse<>(
            "Lấy toàn bộ shift thành công",
            mapper.toDTOList(shiftRepo.findAll())
        );
    }

    // ------------------ Lấy shift theo ID ------------------
    public ResponseDTO<ShiftInfo> getShiftById(ShiftRequestDTO req) {
        if (req.getShiftId() == null)
            return new InvalidResponse<>("Thiếu ID shift");

        ShiftModel shift = shiftRepo.findById(req.getShiftId());
        if (shift == null)
            return new NotFoundResponse<>("Không tìm thấy shift");

        return new SuccessResponse<>("Lấy shift thành công", mapper.toDTO(shift));
    }

    // ------------------ Tạo shift ------------------
    public ResponseDTO<ShiftInfo> createShift(ShiftRequestDTO req) {
        if (!req.validForCreate())
            return new InvalidResponse<>("Dữ liệu không hợp lệ để tạo shift");

        ShiftModel shift = new ShiftModel.Builder()
            .name(req.getName())
            .startTime(req.getStartTime())
            .endTime(req.getEndTime())
            .build();

        shiftRepo.save(shift);
        return new SuccessResponse<>("Tạo shift thành công", mapper.toDTO(shift));
    }

    // ------------------ Cập nhật shift ------------------
    public ResponseDTO<ShiftInfo> updateShift(ShiftRequestDTO req) {
        if (!req.validForUpdate())
            return new InvalidResponse<>("Dữ liệu không hợp lệ để cập nhật shift");

        ShiftModel temp = new ShiftModel.Builder()
            .id(req.getShiftId())
            .name(req.getName())
            .startTime(req.getStartTime())
            .endTime(req.getEndTime())
            .build();

        ShiftModel updated = shiftRepo.update(temp);
        if (updated == null)
            return new NotFoundResponse<>("Không tìm thấy shift để cập nhật");

        return new SuccessResponse<>("Cập nhật shift thành công", mapper.toDTO(updated));
    }

    // ------------------ Xóa shift ------------------
    public ResponseDTO<ShiftInfo> deleteShift(ShiftRequestDTO req) {
        if (req.getShiftId() == null)
            return new InvalidResponse<>("Thiếu ID shift");

        ShiftModel deleted = shiftRepo.delete(req.getShiftId());
        if (deleted == null)
            return new NotFoundResponse<>("Không tìm thấy shift để xóa");

        return new SuccessResponse<>("Xóa shift thành công", mapper.toDTO(deleted));
    }

    // ------------------ Tìm kiếm theo tên ------------------
    public ResponseDTO<List<ShiftInfo>> searchShiftsByName(String keyword) {
        if (keyword == null || keyword.isBlank())
            return new InvalidResponse<>("Thiếu từ khóa tìm kiếm");

        return new SuccessResponse<>(
            "Tìm kiếm shift thành công",
            mapper.toDTOList(shiftRepo.findByName(keyword))
        );
    }
}
