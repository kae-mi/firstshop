package com.shopshop.firstshop.service;

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

    public Long addCart(CartItemDto cartItemDto, String email) {
        Member member = memberRepository.findByUsername(email);
        Cart cart = cartRepository.findByMemberId(member.getId());

        if (cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        
        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item);

        if (cartItem != null) {
            cartItem.addCount(cartItemDto.getCount());
            return cartItem.getId();
        } else {
            cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }
}