package com.shopshop.firstshop.entity;

import com.shopshop.firstshop.constant.ItemSellStatus;
import com.shopshop.firstshop.dto.ItemDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
public class Item {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 50) // NULL 불가능, 이름 길이 50 제한
    private String itemName;

    @Column(nullable = false)
    private String itemCategory;

    @Column(nullable = false) // NULL 불가능
    private int price;

    @Column(nullable = false) // NULL 불가능
    private int stockNumber;

    @Lob
    @Column(nullable = false)
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

    void updateItem(ItemDto itemDto) {
        this.itemName = itemDto.getItemName();
        this.itemCategory = itemDto.getItemCategory();
        this.price = itemDto.getPrice();
        this.stockNumber = itemDto.getStockNumber();
        this.itemDetail = itemDto.getItemDetail();
        this.itemSellStatus = itemDto.getItemSellStatus();
    }
}
