package edu.miu.ProductService.controller;

import edu.miu.ProductService.entity.Product;
import edu.miu.ProductService.model.ProductRequest;
import edu.miu.ProductService.model.ProductResponse;
import edu.miu.ProductService.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<Long> addProduct(@RequestBody ProductRequest productRequest){
        long productId = productService.addProduct(productRequest);
        return new ResponseEntity<>(productId, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") long productId){

        ProductResponse productResponse = productService.getProductById(productId);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PutMapping("/reduceQuantity/{id}")
    public ResponseEntity<Void> reduceQuantity (
            @PathVariable("id") long productId, @RequestParam long quantity){
        productService.reduceQuantity(productId, quantity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    {
//        "name": "iphone",
//            "price": 1100,
//            "quantity": 5
//
//    }  //POST  http://localhost:8080/product

//   PUT  http://localhost:8080/product/reduceQuantity/1?quantity=20
//  PUT  http://localhost:8080/product/reduceQuantity/1?quantity=1

}
