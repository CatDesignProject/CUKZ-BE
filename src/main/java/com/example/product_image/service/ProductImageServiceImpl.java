package com.example.product_image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.product_image.dto.response.ProductImageResponse;
import com.example.product_image.entity.ProductImage;
import com.example.product_image.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client amazonS3Client;
    private final ProductImageRepository productImageRepository;

    @Override
    public List<ProductImageResponse> fileUpload(List<MultipartFile> files) {
        try {
            List<ProductImageResponse> productImageResponses = new ArrayList<>();

            for (MultipartFile file : files) {
                String fileName = createStoreFileName(file.getOriginalFilename());//파일 원본 파일명

                //파일 확장자를 통해 Content-Type 유추
                String contentType = getContentTypeFromFileName(fileName);

                uploadToS3Server(file, contentType, fileName);

                //이미지 실제 url 얻어옴
                String imageUrl = getPath(fileName);

                //ProductImage 엔티티 저장
                ProductImage productImage = productImageRepository.save(
                        ProductImage.builder()
                                .uploadFileName(file.getOriginalFilename())
                                .storeFileName(fileName)
                                .imageUrl(imageUrl)
                                .build()
                );

                productImageResponses.add(new ProductImageResponse(imageUrl, productImage.getId()));
            }

            //이미지 실제 url과 해당 url의 원래 파일명, 서버에서 관리하는 파일명을 필드로 하는 엔티티의 Id값 반환
            return productImageResponses;

        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패");
        }
    }

    @Override
    public String getPath(String imagePath) {
        return amazonS3Client.getUrl(bucket, imagePath).toString();
    }

    private void uploadToS3Server(MultipartFile file, String contentType, String fileName) throws IOException {
        ObjectMetadata metadata= new ObjectMetadata();
        metadata.setContentType(contentType);
        metadata.setContentLength(file.getSize());
        amazonS3Client.putObject(bucket, fileName, file.getInputStream(),metadata);
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(pos + 1);
        return ext;
    }

    private String getContentTypeFromFileName(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1);

        // 간단한 매핑 로직을 통해 Content-Type 결정 (필요에 따라 더 확장 가능)
        switch (extension.toLowerCase()) {
            case "png":
                return "image/png";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "gif":
                return "image/gif";
            default:
                return "application/octet-stream"; // 기본적으로 바이너리 데이터로 처리
            //멀티파트는 임시로 파일을 디스크에 쓰기때문에, 업로드 할 파일이 많거나 용량이 크면 사용하지 않는게 좋습니다
            //octet stream 추천드려요
        }
    }

}
