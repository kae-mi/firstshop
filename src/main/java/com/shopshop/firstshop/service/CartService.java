package com.shopshop.firstshop.service;

import com.shopshop.firstshop.dto.CartDetailDto;
import com.shopshop.firstshop.dto.CartItemDto;
import com.shopshop.firstshop.entity.Cart;
import com.shopshop.firstshop.entity.CartItem;
import com.shopshop.firstshop.entity.Item;
import com.shopshop.firstshop.entity.Member;
import com.shopshop.firstshop.repository.CartItemRepository;
import com.shopshop.firstshop.repository.CartRepository;
import com.shopshop.firstshop.repository.ItemRepository;
import com.shopshop.firstshop.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    public void addCart(CartItemDto cartItemDto, String email) {
        Member member = memberRepository.findByUsername(email);
        Cart cart = cartRepository.findByMemberId(member.getId());

        if (cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if (cartItem != null) {
            cartItem.updateCount(cartItemDto.getCount());
        } else {
            cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
        }
    }

    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email) {
        
        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();
        
        Member member = memberRepository.findByUsername(email);
        Cart cart = cartRepository.findByMemberId(member.getId());
        
        if (cart == null) {
            return cartDetailDtoList;
        }

        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());
        return cartDetailDtoList;
    }

    // 장바구니 상품 수량 변경 로직
    public void updateQuantity(Long cartItemId, int change) {
        CartItem cartItem = cartItemRepository.findById(cartItemId);
        
        int newQuantity = cartItem.getCount() + change;

        if (newQuantity < 1) {
            throw new IllegalArgumentException("수량은 1보다 작을 수 없습니다.");
        }
        
        cartItem.updateCount(newQuantity);
    }

    // 장바구니 상품 삭제 로직
    public void removeCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId);
        
        cartItemRepository.delete(cartItem);
    }

}