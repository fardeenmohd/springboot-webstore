package com.webstore.service;

import com.webstore.payload.ProductDTO;
import com.webstore.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO productDTO);

    ProductResponse getProducts();

    ProductResponse getProductsByCategory(Long categoryId);

    ProductResponse getProductsByName(String name);

    ProductDTO updateProduct(ProductDTO productDTO);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
