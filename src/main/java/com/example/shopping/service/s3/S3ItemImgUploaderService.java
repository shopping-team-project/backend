package com.example.shopping.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.shopping.domain.Item.ItemImgDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Log4j2
@RequiredArgsConstructor
@Service
public class S3ItemImgUploaderService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    public List<ItemImgDTO> upload(String fileType, List<MultipartFile> multipartFiles) throws IOException {
        List<ItemImgDTO> s3files = new ArrayList<>();

        String uploadFilePath = fileType + "/" + getFolderName();

        for (MultipartFile multipartFile : multipartFiles) {
            String oriFileName = multipartFile.getOriginalFilename();
            String uploadFileName = getUuidFileName(oriFileName);
            String uploadFileUrl = "";

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());

            try (InputStream inputStream = multipartFile.getInputStream()) {
                // ex) 구분/년/월/일/파일.확장자
                String keyName = uploadFilePath + "/" + uploadFileName;

                // S3에 폴더 및 파일 업로드
                amazonS3.putObject(
                        new PutObjectRequest(bucket, keyName, inputStream, objectMetadata));

                // S3에 업로드한 폴더 및 파일 URL
                uploadFileUrl = amazonS3.getUrl(bucket, keyName).toString();
            } catch (IOException e) {
                e.printStackTrace();
                log.error("Filed upload failed", e);
            }

            s3files.add(
                    ItemImgDTO.builder()
                            .oriImgName(oriFileName)
                            .uploadImgName(uploadFileName)
                            .uploadImgPath(uploadFilePath)
                            .uploadImgUrl(uploadFileUrl)
                            .build());
        }
        return s3files;
    }

    // S3에 업로드된 파일 삭제
    public String deleteFile(String uploadFilePath, String uuidFileName) {
        String result = "success";

        try {
            // ex) 구분/년/월/일/파일.확장자
            String keyName = uploadFilePath + "/" + uuidFileName;
            boolean isObjectExist = amazonS3.doesObjectExist(bucket, keyName);

            if (isObjectExist) {
                amazonS3.deleteObject(bucket, keyName);
            } else {
                result = "file not found";
            }
        } catch (AmazonS3Exception e) {
            // S3에서 파일 삭제 실패
            result = "S3 file deletion failed: " + e.getMessage();
            log.error("S3 file deletion failed", e);
        } catch (Exception e) {
            // 기타 예외 처리
            result = "file deletion failed: " + e.getMessage();
            log.error("File deletion failed", e);
        }
        return result;
    }


    // UUID 파일명 반환
    private String getUuidFileName(String oriFileName) {
        String ext = oriFileName.substring(oriFileName.indexOf(".") + 1);
        return UUID.randomUUID().toString() + "." + ext;
    }

    // 년/월/일 폴더명 반환
    private String getFolderName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String str = sdf.format(date);
        return str.replace("-", "/");
    }
}
