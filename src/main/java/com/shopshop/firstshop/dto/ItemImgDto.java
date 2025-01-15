package com.shopshop.firstshop.dto;

import com.shopshop.firstshop.entity.ItemImg;
import org.modelmapper.ModelMapper;

public class ItemImgDto {

    private Long id;

    private String imgName;

    private String origImgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgDto of(ItemImg itemImg) {
        return modelMapper.map(itemImg, ItemImgDto.class);
    }
}
