package com.webstore.repositories;

import com.webstore.model.Category;
import com.webstore.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategoryOrderByPriceAsc(Category category, Pageable pageable);

    Page<Product> findByNameLikeIgnoreCase(String name, Pageable pageable);

    List<Product> findByName(String name);
}
