package com.shopshop.firstshop.repository;

import com.shopshop.firstshop.entity.Cart;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import jakarta.persistence.NoResultException;

@Repository
public class CartRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Cart cart) {
        if (cart.getId() == null) {
            em.persist(cart);
        } else {
            em.merge(cart);
        }
    }

    public Cart findById(Long id) {
        return em.find(Cart.class, id);
    }

    public Cart findByMemberId(Long memberId) {
        try {
            System.out.println("장바구니 없음");

            return em.createQuery(
                    "select c from Cart c where c.member.id = :memberId", Cart.class)
                    .setParameter("memberId", memberId)
                    .getSingleResult();
        } catch (NoResultException e) {
            System.out.println("장바구니 없음");
            // 장바구니가 없는 경우 null 반환
            return null;
        }
    }

    public void delete(Cart cart) {
        em.remove(cart);
    }
}

