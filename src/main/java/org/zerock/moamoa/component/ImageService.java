package org.zerock.moamoa.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.moamoa.config.ImageConfig;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@Component
public class ImageService {

    private final ImageConfig imageConfig;
    private final String IMAGE_PATH;
    @Autowired
    public ImageService(ImageConfig imageConfig) {
        this.imageConfig = imageConfig;
        this.IMAGE_PATH   = imageConfig.getImageUploadPath();
    }

    /**
     * 디렉터리 존재 여부 확인 메서드
     * 디렉터리 존재하지 않는 경우 directoryPath에 맞게 생성해준다.
     * @param directoryPath 디렉터리 경로
     */
    public void makeDirectory(String directoryPath){
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it doesn't exist
        }
    }

    /**
     * 이미지 생성 메서드
     * @param directoryPath 디렉터리 경로
     * @param fileName      파일 이름
     * @param image         이미지 파일
     * @return  이미지 생성 성공 여부
     */
    public boolean uploadImage(String directoryPath, String fileName, MultipartFile image){
        if (!image.isEmpty()) {
            try {
                String savedImagePath = String.format("%s/%s", directoryPath, fileName);
                image.transferTo(new File(savedImagePath));
                System.out.println("이미지가 저장되었습니다.");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else return false;

        return true;
    }

    /**
     * 상품 이미지 저장
     * @param images 상품 이미지 배열
     * @param pid    상품 아이디
     */
    public boolean saveProductImage(MultipartFile[] images, Long pid) {
        // 경로를 기반으로 이미지 저장 등의 로직 수행
        if (images != null && images.length > 0) {
            String directoryPath = String.format("%s/product/%s", IMAGE_PATH, pid);
            makeDirectory(directoryPath);

            for (int i = 0; i < images.length; i++) {
                MultipartFile image = images[i];
                String fileName = String.format("%s_%d.jpg", pid, i);
                if(uploadImage(directoryPath, fileName, image)) return true;
                return false;
            }
        }

        return true;
    }

    /**
     * 프로필 이미지 생성
     * kse -> 테스트 안해봤음
     * @param image 프로필 이미지
     * @param uid   유저 아이디
     */
    public boolean saveProfileImage(MultipartFile image, Long uid){
        // 이미지 저장 경로 사용
        String directoryPath = String.format("%s/profile", IMAGE_PATH);
        String fileName      = String.format("%s.jpg", uid);
        makeDirectory(directoryPath);

        if(!image.isEmpty())
            return uploadImage(directoryPath, fileName, image);
        return true;
    }

}