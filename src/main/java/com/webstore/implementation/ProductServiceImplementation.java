package com.webstore.implementation;

import com.webstore.exceptions.ApiException;
import com.webstore.exceptions.ResourceNotFoundException;
import com.webstore.model.Category;
import com.webstore.model.Product;
import com.webstore.payload.ProductDTO;
import com.webstore.payload.ProductResponse;
import com.webstore.repositories.CategoryRepository;
import com.webstore.repositories.ProductRepository;
import com.webstore.service.FileService;
import com.webstore.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImplementation implements ProductService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        Product product = modelMapper.map(productDTO, Product.class);

        List<Product> sameProduct = productRepository.findByName(product.getName());
        if (!sameProduct.isEmpty()) {
            throw new ApiException("Product with name " + product.getName() + " already exists");
        }

        product.setImage("default.png");
        product.setCategory(category);
        double specialPrice = product.getPrice() - (product.getPrice() * (0.01 * product.getDiscount()));

        product.setPrice(specialPrice);

        Product savedProduct = productRepository.save(product);
        ProductDTO savedProductDTO = modelMapper.map(savedProduct, ProductDTO.class);

        return savedProductDTO;
    }

    @Override
    public ProductResponse getProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pagination = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPages = productRepository.findAll(pagination);

        List<Product> productList = productPages.getContent();
        List<ProductDTO> productDTOList = productList.stream().map((product) -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());

        if (productList.isEmpty()) {
            throw new ApiException("No products exist");
        }

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOList);
        productResponse.setPageNumber(productPages.getNumber());
        productResponse.setPageSize(productPages.getSize());
        productResponse.setTotalElements(productPages.getTotalElements());
        productResponse.setTotalPages(productPages.getTotalPages());
        productResponse.setLastPage(productPages.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pagination = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPages = productRepository.findByCategoryOrderByPriceAsc(category, pagination);

        List<Product> productList = productPages.getContent();
        List<ProductDTO> productDTOList = productList.stream().map((product) -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOList);
        productResponse.setPageNumber(productPages.getNumber());
        productResponse.setPageSize(productPages.getSize());
        productResponse.setTotalElements(productPages.getTotalElements());
        productResponse.setTotalPages(productPages.getTotalPages());
        productResponse.setLastPage(productPages.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse getProductsByName(String name, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pagination = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPages = productRepository.findByNameLikeIgnoreCase('%' + name + '%', pagination);

        List<Product> productList = productPages.getContent();
        List<ProductDTO> productDTOList = productList.stream().map((product) -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());

        if (productList.isEmpty()) {
            throw new ApiException("No products exist");
        }

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOList);
        productResponse.setPageNumber(productPages.getNumber());
        productResponse.setPageSize(productPages.getSize());
        productResponse.setTotalElements(productPages.getTotalElements());
        productResponse.setTotalPages(productPages.getTotalPages());
        productResponse.setLastPage(productPages.isLast());

        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        Product productToUpdate = productRepository.findById(product.getId()).orElseThrow(() -> new ResourceNotFoundException("Product", "productId", product.getId()));

        productToUpdate.setName(product.getName());
        productToUpdate.setDescription(product.getDescription());
        productToUpdate.setQuantity(product.getQuantity());
        productToUpdate.setDiscount(product.getDiscount());
        productToUpdate.setPrice(product.getPrice());
        productToUpdate.setSpecialPrice(product.getSpecialPrice());

        Product updatedProduct = productRepository.save(productToUpdate);
        ProductDTO updatedProductDTO = modelMapper.map(updatedProduct, ProductDTO.class);

        return updatedProductDTO;
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product productToDelete = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        productRepository.delete(productToDelete);
        ProductDTO deletedProductDTO = modelMapper.map(productToDelete, ProductDTO.class);

        return deletedProductDTO;
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product productToUpdate = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        String fileName = fileService.uploadImage(image);

        productToUpdate.setImage(fileName);

        Product updatedProduct = productRepository.save(productToUpdate);
        ProductDTO updatedProductDTO = modelMapper.map(updatedProduct, ProductDTO.class);

        return updatedProductDTO;
    }
}
