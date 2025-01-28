package com.shopshop.firstshop.repository;

import com.shopshop.firstshop.entity.Item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByItemName(String itemNm);

    //상품과 대표 이미지를 함께 조회하는 쿼리 추가
    @Query("select i from Item i Left join ItemImg img on img.item.id = i.id where img.repImgYn = 'Y'")
    List<Item> findAllWithMainImage(Pageable pageable);

    // 카테고리별 상품 조회
    Page<Item> findByItemCategory(String category, Pageable pageable);
    
    // 상품명 검색
    Page<Item> findByItemNameContaining(String keyword, Pageable pageable);
    
    // 가격 범위로 검색
    Page<Item> findByPriceBetween(int minPrice, int maxPrice, Pageable pageable);
    
    // 카테고리와 키워드로 검색
    @Query("SELECT i FROM Item i WHERE i.itemCategory = :category " +
           "AND i.itemName LIKE %:keyword%")
    Page<Item> findByCategoryAndKeyword(@Param("category") String category, 
                                      @Param("keyword") String keyword, 
                                      Pageable pageable);
}