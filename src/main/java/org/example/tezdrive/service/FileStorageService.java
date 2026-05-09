package org.example.tezdrive.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png", "image/webp");

    @Value("${r2.endpoint}")
    private String endpoint;

    @Value("${r2.bucket}")
    private String bucket;

    @Value("${r2.access-key}")
    private String accessKey;

    @Value("${r2.secret-key}")
    private String secretKey;

    @Value("${r2.public-base-url}")
    private String publicBaseUrl;

    private S3Client s3;

    @PostConstruct
    private void init() {
        s3 = S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.of("auto"))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    /**
     * Uploads a file to R2 under subDir/uuid.ext
     * Returns the full public URL: https://pub-xxx.r2.dev/subDir/uuid.ext
     */
    public String store(MultipartFile file, String subDir) throws IOException {
        validateFile(file);

        String extension = getExtension(file.getOriginalFilename());
        String key = subDir + "/" + UUID.randomUUID() + "." + extension;

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .build();

        s3.putObject(request, RequestBody.fromBytes(file.getBytes()));

        return publicBaseUrl + "/" + key;
    }

    /**
     * Deletes a file from R2 given its full public URL.
     */
    public void delete(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) return;
        // strip the base URL to get the key: "avatars/uuid.jpg"
        String key = fileUrl.replace(publicBaseUrl + "/", "");
        s3.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build());
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Only JPEG, PNG and WebP images are allowed");
        }
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("File size must not exceed 10 MB");
        }
    }

    private String getExtension(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) return "jpg";
        return originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
    }
}

