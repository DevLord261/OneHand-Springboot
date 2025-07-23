package org.devlord.onehand.Services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class StorageSpaceService {
    private final AmazonS3 s3;
    private final String bucketName;

    public StorageSpaceService(AmazonS3 s3, @Value("${do.spaces.bucket}") String bucketName ) {
        this.s3 = s3;
        this.bucketName = bucketName;
    }

    public String uploadFile(String id,MultipartFile file) throws IOException {
        String key =  id+"/"+ UUID.randomUUID() + Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        PutObjectRequest request = new PutObjectRequest(bucketName, key, file.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead);

        s3.putObject(request);

        return s3.getUrl(bucketName, key).toString();
    }


}
