package com.shopshop.firstshop.dto;

import com.shopshop.firstshop.constant.ItemSellStatus;
import com.shopshop.firstshop.entity.Item;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemListDto {
    private Long id;
    private String itemName;
    private String itemCategory;
    private int price;
    private ItemSellStatus itemSellStatus;
    private String mainImageUrl;

    public ItemListDto(Item item) {
        this.id = item.getId();
        this.itemName = item.getItemName();
        this.itemCategory = item.getItemCategory();
        this.price = item.getPrice();
        this.itemSellStatus = item.getItemSellStatus();
        this.mainImageUrl = item.getMainImageUrl();
    }
} 