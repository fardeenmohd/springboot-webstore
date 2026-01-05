package com.webstore.implementation;

import com.webstore.exceptions.ApiException;
import com.webstore.exceptions.ResourceNotFoundException;
import com.webstore.model.Cart;
import com.webstore.model.CartItem;
import com.webstore.model.Product;
import com.webstore.payload.CartDTO;
import com.webstore.payload.ProductDTO;
import com.webstore.repositories.CartItemRepository;
import com.webstore.repositories.CartRepository;
import com.webstore.repositories.ProductRepository;
import com.webstore.service.CartService;
import com.webstore.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImplementation implements CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthUtil authUtil;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        Cart cart = createCart();

        Product product = productRepository.findById(productId).orElseThrow(() ->
                new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(
                cart.getId(),
                productId
        );

        if (cartItem != null) {
            throw new ApiException("Product " + product.getName() + " already in cart");
        }

        if (product.getQuantity() < quantity) {
            throw new ApiException("Please, make an order of the " + product.getName() + " less than or equal to " + product.getQuantity());
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setCart(cart);
        newCartItem.setProduct(product);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setPrice(product.getSpecialPrice());

        cartItemRepository.save(newCartItem);

        product.setQuantity(product.getQuantity());
        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));

        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItem> cartItems = cart.getCartItems();
        Stream<ProductDTO> productDTOStream = cartItems.stream().map(item ->
        {
            ProductDTO map = modelMapper.map(cartItem.getProduct(), ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        });
        cartDTO.setProducts(productDTOStream.toList());

        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();

        if (carts.isEmpty()) {
            new ApiException("No carts found");
        }

        List<CartDTO> cartDTOs = carts.stream().map(item -> {
            CartDTO cartDTO = modelMapper.map(item, CartDTO.class);
            List<ProductDTO> productDTOs = item.getCartItems().stream().map(cartItem -> modelMapper.map(cartItem.getProduct(), ProductDTO.class)).collect(Collectors.toList());
            cartDTO.setProducts(productDTOs);
            return cartDTO;
        }).collect(Collectors.toList());

        return cartDTOs;
    }

    private Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if (userCart != null) {
            return userCart;
        } else {
            Cart cart = new Cart();
            cart.setTotalPrice(0.0);
            cart.setUser(authUtil.loggedInUser());
            Cart newCart = cartRepository.save(cart);
            return newCart;
        }
    }
}
