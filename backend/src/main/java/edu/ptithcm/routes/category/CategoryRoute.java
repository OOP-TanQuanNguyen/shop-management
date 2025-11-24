package edu.ptithcm.routes.category;

import java.util.List;
import java.util.Map;

import edu.ptithcm.configs.Role;
import edu.ptithcm.configs.TypeDTTP;
import edu.ptithcm.controller.CategoryController;
import edu.ptithcm.dto.request.category.CategoryRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.info_models.CategoryInfo;
import edu.ptithcm.middleware.AuthenMiddleWare;
import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.protocols.DTTPStateManager;
import edu.ptithcm.utils.ReplyUtils;

public class CategoryRoute {

    private final DTTP server;
    private final DTTPStateManager manager;
    private final CategoryController controller;

    public CategoryRoute(DTTP server, DTTPStateManager manager) {
        this.server = server;
        this.manager = manager;
        this.controller = new CategoryController();
    }

    public void register() {
        // ---------------- GET ALL ----------------
        server.on(TypeDTTP.CATEGORY_GET_ALL.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.CATEGORY_GET_ALL.getValue())) {
                    return;
                }

                ResponseDTO<List<CategoryInfo>> response = controller.getAllCategories();
                List<Map<String, Object>> categories = null;
                if (response.getData() != null) {
                    categories = response.getData().stream().map(CategoryInfo::toMap).toList();
                }

                Map<String, Object> payload = Map.of("categories", categories);
                args.reply(TypeDTTP.CATEGORY_GET_ALL.getValue(), payload, response.getStatus(), response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.CATEGORY_GET_ALL.getValue(), e);
            }
        });

        // ---------------- CREATE ----------------
        server.on(TypeDTTP.CATEGORY_CREATE.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.CATEGORY_CREATE.getValue())) {
                    return;
                }

                CategoryRequestDTO request = new CategoryRequestDTO(args.data);
                ResponseDTO<CategoryInfo> response = controller.createCategory(request);

                args.reply(TypeDTTP.CATEGORY_CREATE.getValue(),
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.CATEGORY_CREATE.getValue(), e);
            }
        });

        // ---------------- UPDATE ----------------
        server.on(TypeDTTP.CATEGORY_UPDATE.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.CATEGORY_UPDATE.getValue())) {
                    return;
                }

                CategoryRequestDTO request = new CategoryRequestDTO(args.data);
                ResponseDTO<CategoryInfo> response = controller.updateCategory(request);

                args.reply(TypeDTTP.CATEGORY_UPDATE.getValue(),
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.CATEGORY_UPDATE.getValue(), e);
            }
        });

        // ---------------- DELETE ----------------
        server.on(TypeDTTP.CATEGORY_DELETE.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.CATEGORY_DELETE.getValue())) {
                    return;
                }

                CategoryRequestDTO request = new CategoryRequestDTO(args.data);
                ResponseDTO<CategoryInfo> response = controller.deleteCategory(request);

                args.reply(TypeDTTP.CATEGORY_DELETE.getValue(), null, response.getStatus(), response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.CATEGORY_DELETE.getValue(), e);
            }
        });

        // ---------------- GET BY ID ----------------
        server.on(TypeDTTP.CATEGORY_GET_BY_ID.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.CATEGORY_GET_BY_ID.getValue())) {
                    return;
                }

                CategoryRequestDTO request = new CategoryRequestDTO(args.data);
                ResponseDTO<CategoryInfo> response = controller.getCategoriesById(request);

                args.reply(TypeDTTP.CATEGORY_GET_BY_ID.getValue(),
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.CATEGORY_GET_BY_ID.getValue(), e);
            }
        });
    }
}
