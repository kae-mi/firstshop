package com.shopshop.firstshop.service;

import com.shopshop.firstshop.dto.ItemFormDto;
import com.shopshop.firstshop.dto.ItemImgDto;
import com.shopshop.firstshop.entity.Item;
import com.shopshop.firstshop.entity.ItemImg;
import com.shopshop.firstshop.repository.ItemImgRepository;
import com.shopshop.firstshop.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        //상품 등록
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        //이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            if (i == 0)
                itemImg.setRepImgYn("Y");
            else
                itemImg.setRepImgYn("N");

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return item.getId();
    }

    // 상품의 상세 정보를 조히하는 getItemDtl 메서드
    public ItemFormDto getItemDtl(Long itemId) {
        List<ItemImgDto> itemImgDtoList = itemImgRepository.findByItemIdOrderByIdAsc(itemId)
                .stream()
                .map(ItemImgDto::of)
                .toList();

        //상품 조회
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다"));

        // Dto 생성 및 반환
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    // 상품 정보를 수정하고, 관련 이미지도 함께 업데이트하는 메소드
    public void updateItem(Long itemId, ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));
        System.out.println(item.getId());
        System.out.println("머야");
        item.updateItem(itemFormDto);

        /*// 이미지 업데이트
        List<Long> itemImgIds = itemFormDto.getItemImgIds();
        for (int i = 0; i < itemImgFileList.size(); i++) {
            MultipartFile imgFile = itemImgFileList.get(i);
            Long imgId = itemImgIds.get(i);

            if(!imgFile.isEmpty()) {
                itemImgService.updateItemImg(imgId, imgFile);
            }
        }*/
    }

    // 상품 데이터를 페이징 처리해서 반환하는 메소드
    @Transactional(readOnly = true)
    public Page<Item> getItems(Pageable pageable) {

        return itemRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Item> getItemsWithMainImage(Pageable pageable) {
        Page<Item> itemsPage = itemRepository.findAll(pageable);
        
        // 각 상품의 대표 이미지 URL을 조회하여 설정
        itemsPage.getContent().forEach(item -> {
            ItemImg mainImage = itemImgRepository.findByItemIdAndRepImgYn(item.getId(), "Y");
            if (mainImage != null) {
                // 이미지 URL을 item 객체에 임시로 저장할 수 있도록 필드 추가 필요
                item.setMainImageUrl(mainImage.getImgUrl());
            }
        });
        
        return itemsPage;
    }
}
