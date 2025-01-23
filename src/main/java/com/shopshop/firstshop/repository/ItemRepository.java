package com.shopshop.firstshop.repository;

import com.shopshop.firstshop.entity.Item;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {


    List<Item> findByItemName(String itemNm);


    //상품과 대표 이미지를 함께 조회하는 쿼리 추가
    @Query("select i from Item i Left join ItemImg img on img.item.id = i.id where img.repImgYn = 'Y'")
    List<Item> findAllWithMainImage(Pageable pageable);
}
