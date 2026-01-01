package com.webstore.service;

import com.webstore.model.Product;
import com.webstore.payload.ProductDTO;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, Product product);
}
