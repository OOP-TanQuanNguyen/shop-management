package edu.ptithcm.routes.product;

import java.util.List;
import java.util.Map;

import edu.ptithcm.configs.Role;
import edu.ptithcm.configs.TypeDTTP;
import edu.ptithcm.controller.ProductController;
import edu.ptithcm.dto.request.product.ProductRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.info_models.ProductInfo;
import edu.ptithcm.middleware.AuthenMiddleWare;
import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.protocols.DTTPStateManager;
import edu.ptithcm.utils.ReplyUtils;

public class ProductRoute {
    private final DTTP server;
    private final DTTPStateManager manager;
    private final ProductController controller;

    public ProductRoute(DTTP server, DTTPStateManager manager) {
        this.server = server;
        this.manager = manager;
        this.controller = new ProductController();
    }

    public void register() {
        // ---------------- GET ALL ----------------
        server.on(TypeDTTP.PRODUCT_GET_ALL.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.PRODUCT_GET_ALL.getValue())) return;

                ResponseDTO<List<ProductInfo>> response = controller.getAllProducts();
                List<Map<String, Object>> products = null;
                if (response.getData() != null) {
                    products = response.getData()
                                       .stream()
                                       .map(ProductInfo::toMap)
                                       .toList();
                }

                Map<String, Object> payload = Map.of("products", products);
                args.reply(TypeDTTP.PRODUCT_GET_ALL.getValue(), payload, response.getStatus(), response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.PRODUCT_GET_ALL.getValue(), e);
            }
        });

        // ---------------- CREATE ----------------
        server.on(TypeDTTP.PRODUCT_CREATE.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.PRODUCT_CREATE.getValue())) return;

                ProductRequestDTO request = new ProductRequestDTO(args.data);
                ResponseDTO<ProductInfo> response = controller.createProduct(request);

                args.reply(TypeDTTP.PRODUCT_CREATE.getValue(),
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.PRODUCT_CREATE.getValue(), e);
            }
        });

        // ---------------- UPDATE ----------------
        server.on(TypeDTTP.PRODUCT_UPDATE.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.PRODUCT_UPDATE.getValue())) return;

                ProductRequestDTO request = new ProductRequestDTO(args.data);
                ResponseDTO<ProductInfo> response = controller.updateProduct(request);

                args.reply(TypeDTTP.PRODUCT_UPDATE.getValue(),
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.PRODUCT_UPDATE.getValue(), e);
            }
        });

        // ---------------- DELETE ----------------
        server.on(TypeDTTP.PRODUCT_DELETE.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.PRODUCT_DELETE.getValue())) return;

                ProductRequestDTO request = new ProductRequestDTO(args.data);
                ResponseDTO<ProductInfo> response = controller.deleteProduct(request);

                args.reply(TypeDTTP.PRODUCT_DELETE.getValue(), null, response.getStatus(), response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.PRODUCT_DELETE.getValue(), e);
            }
        });
    }
}
