package edu.ptithcm.routes.product;

import java.util.List;
import java.util.Map;

import edu.ptithcm.controller.ProductController;
import edu.ptithcm.dto.request.product.ProductRequestDTO;
import edu.ptithcm.dto.response.ProductInfo;
import edu.ptithcm.dto.response.ResponseDTO;
import edu.ptithcm.middleware.AuthenMiddleWare;
import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.protocols.DTTPStateManager;

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
        server.on("PRODUCT_GET_ALL", args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, "ADMIN", args, "PRODUCT_GET_ALL")) return;

                ResponseDTO<List<ProductInfo>> response = controller.getAllProducts();
                List<Map<String, Object>> products = null;
                if (response.getData() != null) {
                    products = response.getData()
                                       .stream()
                                       .map(ProductInfo::toMap)
                                       .toList();
                }

                Map<String, Object> payload = Map.of("products", products);
                args.reply(response.getType(), payload, response.getStatus(), response.getMessage());

            } catch (Exception e) {
                AuthenMiddleWare.replyError(args, "PRODUCT_GET_ALL", e);
            }
        });

        // ---------------- CREATE ----------------
        server.on("PRODUCT_CREATE", args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, "ADMIN", args, "PRODUCT_CREATE")) return;

                ProductRequestDTO request = new ProductRequestDTO(args.data);
                ResponseDTO<ProductInfo> response = controller.createProduct(request);

                args.reply(response.getType(),
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage());

            } catch (Exception e) {
                AuthenMiddleWare.replyError(args, "PRODUCT_CREATE", e);
            }
        });

        // ---------------- UPDATE ----------------
        server.on("PRODUCT_UPDATE", args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, "ADMIN", args, "PRODUCT_UPDATE")) return;

                ProductRequestDTO request = new ProductRequestDTO(args.data);
                ResponseDTO<ProductInfo> response = controller.updateProduct(request);

                args.reply(response.getType(),
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage());

            } catch (Exception e) {
                AuthenMiddleWare.replyError(args, "PRODUCT_UPDATE", e);
            }
        });

        // ---------------- DELETE ----------------
        server.on("PRODUCT_DELETE", args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, "ADMIN", args, "PRODUCT_DELETE")) return;

                ProductRequestDTO request = new ProductRequestDTO(args.data);
                ResponseDTO<ProductInfo> response = controller.deleteProduct(request);

                args.reply(response.getType(), null, response.getStatus(), response.getMessage());

            } catch (Exception e) {
                AuthenMiddleWare.replyError(args, "PRODUCT_DELETE", e);
            }
        });
    }
}
