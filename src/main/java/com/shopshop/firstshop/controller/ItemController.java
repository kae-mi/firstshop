package com.shopshop.firstshop.controller;

import com.shopshop.firstshop.dto.ItemFormDto;
import com.shopshop.firstshop.dto.ItemListDto;
import com.shopshop.firstshop.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    
    @GetMapping
    public String itemList(Model model,
                          @RequestParam(name = "category", required = false) String category,
                          @RequestParam(name = "keyword", required = false) String keyword,
                          @RequestParam(name = "page", defaultValue = "0") int page,
                          @RequestParam(name = "size", defaultValue = "12") int size) {
                            
        log.info("category: {}, keyword: {}, page: {}, size: {}", category, keyword, page, size);

        List<String> allCategoriesName = itemService.getAllCategoriesName();
        model.addAttribute("allCategoriesName", allCategoriesName);
        log.info("allCategoriesName: {}", allCategoriesName);

        Pageable pageable = PageRequest.of(page, size);
        Page<ItemListDto> items;
                            
        if (category != null && keyword != null) {
            items = itemService.searchItemsByCategoryAndKeyword(category, keyword, pageable);
        } else if (category != null) {
            items = itemService.getItemsByCategory(category, pageable);
        } else if (keyword != null) {
            items = itemService.searchItems(keyword, pageable);
        } else {
            items = itemService.getAllItems(pageable);
        }
        
        model.addAttribute("items", items);
        model.addAttribute("category", category);
        model.addAttribute("keyword", keyword);
        
        return "item/itemList";
    }

    @GetMapping("/{itemId}")
    public String itemDetail(@PathVariable Long itemId, Model model) {
        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("item", itemFormDto);
            
            // 디버깅을 위한 로그 추가
            log.info("Item details: {}", itemFormDto);
            
            return "item/itemDetail";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            return "error/404";
        }
    }
} 