package com.shopshop.firstshop.controller.admin;

import com.shopshop.firstshop.dto.ItemFormDto;
import com.shopshop.firstshop.entity.Item;
import com.shopshop.firstshop.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final ItemService itemService;

    @GetMapping("/home")
    public String adminHome() {
        return "adminHome";
    }

    @GetMapping("/item/new")
    public String newItemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping("item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model, @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList) {
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }

        try {
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/admin/home";
    }

    // 상품 조회 매핑
    // 조히한 상품 데이터를 모델에 담아 뷰에 전달
    @GetMapping("/item/{itemId}")
    public String itemDtl(@PathVariable Long itemId, Model model) {

        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        } catch (EntityNotFoundException e) {
            // 상품이 없을 경우 에러 메시지와 빈 폼 데이터 전달
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("itemForDto", new ItemFormDto());
            return "item/editForm";
        }

        return "item/editForm";
    }

    // 상품을 업데이트하는 컨트롤러
    @PostMapping(value = "/item/{itemId}")
    public String itemUpdate(
            @PathVariable Long itemId,
            @Valid ItemFormDto itemFormDto,
            BindingResult bindingResult,
            Model model,
            @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

        // 입력값 검증
        if (bindingResult.hasErrors()) {
            log.info("1");
            return "item/editForm";
        }

        // 첫 번째 이미지가 없고, 새 상품인 경우 에러 처리
        boolean isFirstImageMissing = itemImgFileList.get(0).isEmpty();
        boolean isNewItem = itemFormDto.getId() == null;

        if (isFirstImageMissing && isNewItem) {
            model.addAttribute("errorMessage", "첫 번째 상품 이미지는 필수 입력 값입니다.");

            log.info("2");
            return "item/editForm";
        }

        try {
            // 상품 정보 저장
            itemService.updateItem(itemId, itemFormDto, itemImgFileList);
        } catch (Exception e) {
            // 예외 처리 및 에러 메시지 추가
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            log.info("3");

            return "item/editForm";
        }

        // 성공 시 메인 페이지로 리다이렉트
        return "redirect:/admin/items";
    }

    // 관리자 상품 목록 조회
    @GetMapping("/items")
    public String getItems(Model model,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "size", defaultValue = "10") int size) {
        // Page<Item> itemsPage = itemService.getItems(PageRequest.of(page, size));
        Page<Item> itemsPage = itemService.getItemsWithMainImage(PageRequest.of(page, size));
        System.out.println("총 데이터 개수: " + itemsPage.getTotalElements());
        System.out.println("총 페이지 개수: " + itemsPage.getTotalPages());
        System.out.println("url: " + itemsPage.getContent().get(0).getMainImageUrl());
        model.addAttribute("itemsPage", itemsPage);
        return "item/itemsList";
    }
    
}
