package edu.ptithcm.services;

import java.util.List;
import edu.ptithcm.dto.request.inventory.InventoryRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.error.InvalidResponse;
import edu.ptithcm.dto.response.error.NotFoundResponse;
import edu.ptithcm.dto.response.success.SuccessResponse;
import edu.ptithcm.dto.response.info_models.InventoryInfo;
import edu.ptithcm.models.BranchModel;
import edu.ptithcm.models.InventoryModel;
import edu.ptithcm.models.ProductModel;
import edu.ptithcm.repository.branch.BranchRepository;
import edu.ptithcm.repository.inventory.InventoryRepository;
import edu.ptithcm.repository.product.ProductRepository;
import edu.ptithcm.repository.Repository;
import edu.ptithcm.utils.mapper.BaseMapper;
import edu.ptithcm.utils.mapper.MapperFactory;

public class InventoryService {

    private static final InventoryRepository inventoryRepo = Repository.inventory();
    private static final BranchRepository branchRepo = Repository.branch();
    private static final ProductRepository productRepo = Repository.product();
    private static final BaseMapper<InventoryModel, InventoryInfo> mapper = MapperFactory.inventory();

    // Lấy tất cả
    public ResponseDTO<List<InventoryInfo>> getAllInventories() throws RuntimeException {
        List<InventoryModel> inventories = inventoryRepo.findAll();
        inventories.forEach(inv -> {
            if (inv.getBranch() == null) inv.setBranch(new BranchModel());
            if (inv.getProduct() == null) inv.setProduct(new ProductModel());
        });
        return new SuccessResponse<>("Lấy toàn bộ kho thành công", mapper.toDTOList(inventories));
    }

    // Tạo mới
    public ResponseDTO<InventoryInfo> createInventory(InventoryRequestDTO req) throws RuntimeException {
        if (!req.validForCreate()) return new InvalidResponse<>("Dữ liệu không hợp lệ");
        BranchModel branch = null; 
        ProductModel product = null;
        try { 
            branch = branchRepo.findById(Integer.valueOf(req.getBranchId())); 
        } catch (NumberFormatException ignored) {} 
        product = productRepo.findById(req.getProductId());

        if (branch == null) return new NotFoundResponse<>("Chi nhánh không tồn tại");
        if (product == null) return new NotFoundResponse<>("Sản phẩm không tồn tại");

        InventoryModel existing = inventoryRepo.findByBranchAndProduct(branch.getId(), product.getId());
        if (existing != null) return new InvalidResponse<>("Sản phẩm đã tồn tại trong kho");

        InventoryModel inventory = new InventoryModel();
        inventory.setBranch(branch);
        inventory.setProduct(product);
        inventory.setQuantity(req.getQuantity() != null ? req.getQuantity() : 0);

        inventoryRepo.save(inventory);

        return new SuccessResponse<>("Tạo kho thành công", mapper.toDTO(inventory));
    }

    // Cập nhật
    public ResponseDTO<InventoryInfo> updateInventory(InventoryRequestDTO req) throws RuntimeException {
        if (!req.validForUpdate())
            return new InvalidResponse<>("Dữ liệu không hợp lệ");

        InventoryModel inventory = inventoryRepo.findById(req.getId());
        if (inventory == null)
            return new NotFoundResponse<>("Kho không tồn tại");

        if (req.getBranchId() != null) {
            BranchModel br = null; 
            try { 
                br = branchRepo.findById(Integer.valueOf(req.getBranchId())); 
            } catch (NumberFormatException ignored) {} 
            if (br == null) 
                return new InvalidResponse<>("Chi nhánh không hợp lệ"); 
            inventory.setBranch(br);
        }

        if (req.getProductId() != null) {
            ProductModel p = productRepo.findById(req.getProductId());
            if (p == null) return new InvalidResponse<>("Sản phẩm không hợp lệ");
            inventory.setProduct(p);
        }

        if (inventory.getBranch() != null && inventory.getProduct() != null) { 
            List<InventoryModel> branchInventories = inventoryRepo.findByBranch(inventory.getBranch().getId()); 
            if (branchInventories != null) { 
                boolean exists = branchInventories.stream() 
                .anyMatch(inv -> inv.getProduct() != null && 
                inv.getProduct().getId().equals(inventory.getProduct().getId()) && 
                !inv.getId().equals(inventory.getId())); 
            if (exists) 
                return new InvalidResponse<>("Sản phẩm đã tồn tại trong kho của chi nhánh này"); 
            } 
        }
        
        if (req.getQuantity() != null) {
            inventory.setQuantity(req.getQuantity());
        }

        InventoryModel updated = inventoryRepo.update(inventory);
        return new SuccessResponse<>("Cập nhật kho thành công", mapper.toDTO(updated));
    }

    // Xóa
    public ResponseDTO<InventoryInfo> deleteInventory(InventoryRequestDTO req) throws RuntimeException {
        if (!req.validForDelete()) return new InvalidResponse<>("Thiếu ID kho");

        InventoryModel deleted = inventoryRepo.delete(req.getId());
        if (deleted == null) return new NotFoundResponse<>("Kho không tồn tại");

        return new SuccessResponse<>("Xóa kho thành công", mapper.toDTO(deleted));
    }

    // Lấy theo chi nhánh
    public ResponseDTO<List<InventoryInfo>> getInventoriesByBranch(Integer branchId) throws RuntimeException {
        return new SuccessResponse<>(
                "Lấy kho theo chi nhánh thành công",
                mapper.toDTOList(inventoryRepo.findByBranch(branchId))
        );
    }
}
