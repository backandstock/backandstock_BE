package com.project.minibacktesting_be.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket; // S3 버킷 이름

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));
        return upload(uploadFile, dirName);
    }

    // S3 파일 업로드
    private String upload(File uploadFile, String dirName){
        // S3에 저장할 파일이름
        // UUID를 저장하는 이유
        // 1. 파일명이 중복으로 발생할수 있음
        // 2. S3가 외부에 모두 오픈된 경우, UUID가 없으면 파일이름으로 쉽게 S3에 저장된 파일에 접근할 수 있기때문
        String fileName = dirName + "/" + UUID.randomUUID() + uploadFile.getName();
        // S3에 Url 업로드
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    // S3에 url 업로드 메소드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 파일 업로드하기
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" +
                file.getOriginalFilename());
//         위에서 지정한 경로에 File이 생성됨. 경로가 정확하지않으면 생성 불가
        if(convertFile.createNewFile()) {
            try(FileOutputStream fos = new FileOutputStream(convertFile)){
                // FileOutputStream data를 파일에 byte 스트림으로 저장하기위해서
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    // 로컬 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if(targetFile.delete()){
            log.info("File delete success");
        }else{
            log.info("File delete fail");
        }
    }

    // S3 저장된 이미지 지우기
    public void deleteFile(String fileUrl) throws UnsupportedEncodingException {
        // decode를 한것과, 하지않은것의 문자열 차이는 안보이지만
        // decode하지않으면 해당 source변수로 deleteObject시에 삭제되지 않는다.
        String source = URLDecoder.decode(fileUrl.replace("https://jsm-bucket.s3.ap-northeast-2.amazonaws.com/", ""), "UTF-8");
        amazonS3Client.deleteObject(bucket, source);
    }
}
