package com.shopshop.firstshop.service;

import com.shopshop.firstshop.entity.ItemImg;
import com.shopshop.firstshop.repository.ItemImgRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        // 파일 업로드
        if (!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            imgUrl = "/images/item/" + imgName;
        }

        // 상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }

    // 상품 수정시 이미지도 수정하는 메소드
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
        if (itemImgFile.isEmpty()) {
            return;
        }

        // 기존 이미지 정보 조회
        ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                .orElseThrow(() -> new EntityNotFoundException("이미지를 찾을 수 없습니다."));

        // 기존 이미지 파일이 있다면 삭제하기
        String imgName = savedItemImg.getImgName();
        if (imgName != null && !imgName.isEmpty()) {
            fileService.deleteFile(itemImgLocation + "/" + imgName);
        }

        // 새 파일 업로드
        String originalFilename = itemImgFile.getOriginalFilename();
        String newImgName = fileService.uploadFile(itemImgLocation, originalFilename, itemImgFile.getBytes());
        String newImgUrl = "/images/item/" + newImgName;

        // 데이터베이스에 이미지 정보 업데이트
        savedItemImg.updateItemImg(originalFilename, newImgName, newImgUrl);
    }


}
