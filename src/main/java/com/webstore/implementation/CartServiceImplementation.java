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
import jakarta.transaction.Transactional;
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

        if (product.getQuantity() == 0) {
            throw new ApiException("Product " + product.getName() + " is out of stock");
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

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(emailId, cartId);

        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        cart.getCartItems().forEach(c -> c.getProduct().setQuantity(c.getQuantity()));

        List<ProductDTO> products = cart.getCartItems().stream()
                .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());
        cartDTO.setProducts(products);

        return cartDTO;
    }

    @Override
    @Transactional
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {
        String email = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByEmail(email);
        Long cartId = userCart.getId();

        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepository.findById(productId).orElseThrow(() ->
                new ResourceNotFoundException("Product", "productId", productId));

        if (product.getQuantity() == 0) {
            throw new ApiException("Product " + product.getName() + " is out of stock");
        }

        if (product.getQuantity() < quantity) {
            throw new ApiException("Please, make an order of the " + product.getName() + " less than or equal to " + product.getQuantity());
        }

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null) {
            throw new ApiException("Product " + product.getName() + " is out of stock");
        }

        int newQuantity = cartItem.getQuantity() + quantity;

        if (newQuantity < 0) {
            throw new ApiException("Quantity cannot be negative");
        }

        if (newQuantity == 0) {
            deleteProductFromCart(cartId, productId);
        } else {
            cartItem.setPrice(product.getSpecialPrice());
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setDiscount(product.getDiscount());
            cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getPrice() * quantity));
            cartRepository.save(cart);
        }

        CartItem updatedItem = cartItemRepository.save(cartItem);
        if (updatedItem.getQuantity() == 0) {
            cartItemRepository.deleteById(updatedItem.getId());
        }

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

    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

        if (cartItem == null) {
            throw new ApiException("Product not found in cart");
        }

        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getPrice() * cartItem.getQuantity()));
        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);

        return "Product " + cartItem.getProduct().getName() + " removed from cart";
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
