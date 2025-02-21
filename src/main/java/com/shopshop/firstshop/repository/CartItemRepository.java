package com.shopshop.firstshop.repository;

import com.shopshop.firstshop.entity.CartItem;
import com.shopshop.firstshop.entity.Item;
import com.shopshop.firstshop.dto.CartDetailDto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CartItemRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(CartItem cartItem) {
        if (cartItem.getId() == null) {
            em.persist(cartItem);
        } else {
            em.merge(cartItem);
        }
    }

    public CartItem findById(Long id) {
        return em.find(CartItem.class, id);
    }

    public CartItem findByCartIdAndItemId(Long cartId, Long itemId) {
        try {
            return em.createQuery(
                "select ci from CartItem ci " +
                "where ci.cart.id = :cartId " +
                "and ci.item.id = :itemId", CartItem.class)
                .setParameter("cartId", cartId)
                .setParameter("itemId", itemId)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void delete(CartItem cartItem) {
        em.remove(cartItem);
    }

    public List<CartDetailDto> findCartDetailDtoList(Long cartId) {
        return em.createQuery(
            "select new com.shopshop.firstshop.dto.CartDetailDto(" +
                "ci.id, " +
                "ci.item.itemName, " +
                "ci.item.price, " +
                "ci.count) " +
            "from CartItem ci " +
            "where ci.cart.id = :cartId " +
            "order by ci.id desc", CartDetailDto.class)
            .setParameter("cartId", cartId)
            .getResultList();
    }
}