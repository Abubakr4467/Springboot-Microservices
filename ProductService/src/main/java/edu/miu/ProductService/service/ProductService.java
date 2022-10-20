package edu.miu.ProductService.service;

import edu.miu.ProductService.model.ProductRequest;
import edu.miu.ProductService.model.ProductResponse;

public interface ProductService {

    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long productId);

    void reduceQuantity(long productId, long quantity);
}
