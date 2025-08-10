package com.study.demo.backend.domain.cart.repository;

import com.study.demo.backend.domain.cart.entity.CartMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartMenuRepository extends JpaRepository<CartMenu, Long> {
    List<CartMenu> findByCartId(Long cartId);

    Optional<CartMenu> findByCartIdAndMenuId(Long cartId, Long menuId);

    @Query("""
           select m.store.id
           from CartMenu cm
           join cm.menu m
           where cm.cart.id = :cartId
           group by m.store.id
           """)
    List<Long> findDistinctStoreIdsByCartId(@Param("cartId") Long cartId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from CartMenu cm where cm.cart.id = :cartId")
    void deleteByCartId(@Param("cartId") Long cartId);

    @Query("""
       select cm
       from CartMenu cm
       join fetch cm.menu m
       join fetch m.store s
       where cm.cart.id = :cartId
       """)
    List<CartMenu> findByCartIdWithMenuAndStore(@Param("cartId") Long cartId);
}
