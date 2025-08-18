package com.study.demo.backend.domain.favorite.entity.favoriteMenu;

import com.study.demo.backend.domain.menu.entity.Menu;
import com.study.demo.backend.domain.user.entity.User;
import com.study.demo.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favorite_menu", uniqueConstraints = {
        @UniqueConstraint(
                name = "uq_favorite_menu_user_id_menu_id",
                columnNames = {"user_id", "menu_id"}
        )
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class FavoriteMenu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    public FavoriteMenu(User user, Menu menu) {
        this.user = user;
        this.menu = menu;
    }

    public static FavoriteMenu of(User user, Menu menu) {
        return new FavoriteMenu(user, menu);
    }
}