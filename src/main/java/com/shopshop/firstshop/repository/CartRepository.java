package com.shopshop.firstshop.repository;

import com.shopshop.firstshop.entity.Cart;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

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
        return em.createQuery("select c from Cart c where c.member.id = :memberId", Cart.class)
                .setParameter("memberId", memberId)
                .getSingleResult();
    }

    public void delete(Cart cart) {
        em.remove(cart);
    }
}

