package com.ecommerce.getsetgroceries.serviceProxy;

import feign.Headers;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "fileService")
@RibbonClient(name = "fileService")
public interface ImageServiceProxy {
    @GetMapping("/base64/{id}")
    String retrieveImageAsBase64(@PathVariable("id") String id);

    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Headers("Content-Type:multipart/form-data")
    String uploadFile(@RequestPart("image") MultipartFile image);

    @PostMapping(value = "/uploadMultipleFiles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    List<String> uploadFiles(@RequestPart("images") MultipartFile[] images);
}
