package com.study.demo.backend.domain.favorite.entity.favoriteStore;

import com.study.demo.backend.domain.store.entity.Store;
import com.study.demo.backend.domain.user.entity.User;
import com.study.demo.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favorite_store", uniqueConstraints = {
        @UniqueConstraint(
                name = "uq_favorite_store_member_id_store_id",
                columnNames = {"user_id", "store_id"}
        )
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class FavoriteStore extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private FavoriteStore(User user, Store store) {
        this.user = user;
        this.store = store;
    }

    public static FavoriteStore of(User user, Store store) {
        return new FavoriteStore(user, store);
    }
}