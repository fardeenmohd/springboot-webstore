package com.webstore.repositories;

import com.webstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT ci FROM CartItem ci WHERE ci.product.id = ?2 AND ci.cart.id = ?1")
    CartItem findCartItemByProductIdAndCartId(Long id, Long productId);
}
