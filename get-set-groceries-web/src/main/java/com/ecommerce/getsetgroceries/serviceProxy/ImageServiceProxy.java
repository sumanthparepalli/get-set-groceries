package com.ecommerce.getsetgroceries.serviceProxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "fileService")
public interface ImageServiceProxy {
    @GetMapping("/base64/{id}")
    public String retreiveImageAsBase64(@PathVariable("id") String id);

    @PostMapping("/uploadFile")
    public String uploadFile(MultipartFile multipartFile);

    @PostMapping("/uploadMultipleFile")
    public String uploadFiles(MultipartFile[] multipartFile);
}
