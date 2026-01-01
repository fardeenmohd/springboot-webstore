package com.webstore.service;

import com.webstore.model.Product;
import com.webstore.payload.ProductDTO;
import com.webstore.payload.ProductResponse;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, Product product);

    ProductResponse getProducts();

    ProductResponse getProductsByCategory(Long categoryId);

    ProductResponse getProductsByName(String name);

    ProductDTO updateProduct(Product product);

    ProductDTO deleteProduct(Long productId);
}
