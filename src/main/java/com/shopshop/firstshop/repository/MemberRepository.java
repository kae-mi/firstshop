package com.shopshop.firstshop.repository;

import com.shopshop.firstshop.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // findBy -> 규칙 , Username 부분은 문법이다.
    // 아래 함수르 통해 만들어지는 쿼리 -> select * from member where name = ?
    Optional<Member> findByUsername(String username);

    /*@PersistenceContext
    private EntityManager em;

    public void saveMember(Member member) {

        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public Member findByEmail(String email) {
        List<Member> member = em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList();

        // 리스트가 비어있지 않다면 첫 번째 요소 반환, 아니면 null 반환
        if (!member.isEmpty()) {
            return member.get(0);  // 첫 번째 결과를 반환
        } else {
            return null;  // 검색 결과가 없으면 null 반환
        }
    }*/
}
