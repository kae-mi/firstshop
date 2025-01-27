package com.shopshop.firstshop.entity;

import com.shopshop.firstshop.constant.ItemSellStatus;
import com.shopshop.firstshop.dto.ItemDto;
import com.shopshop.firstshop.dto.ItemFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.shopshop.firstshop.exception.OutOfStockException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "item") // OrderItem 엔티티와 연관관계 매핑
    private List<OrderItem> orderItems = new ArrayList<>(); // 상품은 여러 주문 상품 가능

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

    @Transient // DB에 저장되지 않는 임시 필드
    private String mainImageUrl;

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemName = itemFormDto.getItemName();
        this.itemCategory = itemFormDto.getItemCategory();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int quantity) {
        int restStock = this.stockNumber - quantity;
        if (restStock < 0) {
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량: " + this.stockNumber + ")");
        }
        this.stockNumber = restStock;
    }

    public void addStock(int quantity) {
        this.stockNumber += quantity;
    }

}
