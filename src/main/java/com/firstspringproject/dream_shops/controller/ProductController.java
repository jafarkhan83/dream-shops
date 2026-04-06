package com.firstspringproject.dream_shops.controller;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.firstspringproject.dream_shops.dto.ProductDto;
import com.firstspringproject.dream_shops.exceptions.ProductNotFoundException;
import com.firstspringproject.dream_shops.model.Product;
import com.firstspringproject.dream_shops.request.AddProductRequest;
import com.firstspringproject.dream_shops.request.ProductUpdateRequest;
import com.firstspringproject.dream_shops.response.ApiResponse;
import com.firstspringproject.dream_shops.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> productDtos = productService.getConvertedProducts(products);
        return  ResponseEntity.ok(new ApiResponse("success", productDtos));
    }

    @GetMapping("/getProduct/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            ProductDto productDto = productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("success!", productDto));    
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct (@RequestBody AddProductRequest product) {
        try {
            Product newProduct = productService.addProduct(product);
            return ResponseEntity.ok(new ApiResponse("Product added success!", newProduct));    
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateProduct (@RequestBody ProductUpdateRequest request, @PathVariable Long id) {
        try {
            Product updateProduct = productService.updateProduct(request, id);
            ProductDto productDto = productService.convertToDto(updateProduct);
            return ResponseEntity.ok(new ApiResponse("Product updated success!", productDto));    
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteProduct (@PathVariable Long id) {
        try {
            productService.deleteProductById(id);
            return ResponseEntity.ok(new ApiResponse("Product deleted success!", id));    
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/getByBrandAndName/{brand}/{name}")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@PathVariable String brand, @PathVariable String name) {
        try {
            List<Product> products = productService.getProductsByBrandAndName(brand, name);
            if (products.isEmpty()) return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found!", null));
            List<ProductDto> productDtos = productService.getConvertedProducts(products);            
            return ResponseEntity.ok(new ApiResponse("success!", productDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/getByBrandAndCategory/{brand}/{category}")
    public ResponseEntity<ApiResponse> getProductByBrandAndCategory(@PathVariable String brand, @PathVariable String category) {
        try {
            List<Product> products = productService.getProductsByCategoryAndBrand(category, brand);
            if (products.isEmpty()) return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found!", null));
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("success!", productDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/getProductByName/{name}")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name) {
        try {
            List<Product> products = productService.getProductsByName(name);
            if (products.isEmpty()) return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found!", null));
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("success!", productDtos));    
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/getProductByBrand/{brand}")
    public ResponseEntity<ApiResponse> getProductByBrand(@PathVariable String brand) {
        try {
            List<Product> products = productService.getProductsByBrand(brand);
            if (products.isEmpty()) return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found!", null));
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("success!", productDtos));    
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/getProductByCategory/{category}")
    public ResponseEntity<ApiResponse> getProductByCategory(@PathVariable String category) {
        try {
            List<Product> products = productService.getProductsByCategory(category);
            if (products.isEmpty()) return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found!", null));
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("success!", productDtos));    
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/countByBrandAndName/{brand}/{name}")
    public ResponseEntity<ApiResponse> countProductByBrandAndName(@PathVariable String brand, @PathVariable String name) {
        try {
            Long count = productService.CountProductsByBrandAndName(brand, name);
            return ResponseEntity.ok(new ApiResponse("success!", count));    
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }   
    }

}
