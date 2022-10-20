package edu.miu.ProductService.service.imp;

import edu.miu.ProductService.entity.Product;
import edu.miu.ProductService.exception.ProductServiceCustomException;
import edu.miu.ProductService.model.ProductRequest;
import edu.miu.ProductService.model.ProductResponse;
import edu.miu.ProductService.repository.ProductRepo;
import edu.miu.ProductService.service.ProductService;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepo productRepo;
    public long addProduct(ProductRequest productRequest){
        log.info("Adding product...");

        Product product
                = Product.builder()
                .productName(productRequest.getName())
                .quantity(productRequest.getQuantity())
                .price(productRequest.getPrice())
                .build();
        productRepo.save(product);
        log.info("Product Created");
        return product.getProductId();
    }

    @Override
    public ProductResponse getProductById(long productId){

        log.info("Getting the product for  ProductId : {} " + productId);

        Product product =
                        productRepo.findById(productId)
                                .orElseThrow(
                                        ()-> new ProductServiceCustomException("product with given id not found ", "PRODUCT_NOT_FOUND "));

        ProductResponse productResponse =new ProductResponse();
        BeanUtils.copyProperties(product, productResponse);

        return productResponse;
    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        log.info("Reduce Quantity{} for Id : {}" ,quantity, productId);
        Product product = productRepo.findById(productId)
                .orElseThrow( ()-> new ProductServiceCustomException("Product with given id not found", "PRODUCT_NOT_FOUND"));
        if(product.getQuantity() < quantity){
            throw new ProductServiceCustomException("Product does not sufficient quantity",
                    "INSUFFICIANT_QUANTITY");
        }
        product.setQuantity(product.getQuantity() - quantity);
        productRepo.save(product);
        log.info("Product quantity updated successfully");
    }
}
