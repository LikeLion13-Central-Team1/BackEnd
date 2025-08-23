package com.study.demo.backend.domain.store.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.study.demo.backend.domain.user.entity.User;
import com.study.demo.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "store")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "road_address_name")
    private String roadAddressName;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "latitude", nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 9, scale = 6)
    private BigDecimal longitude;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @Column(name = "opening_time", nullable = false)
    private LocalTime openingTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @Column(name = "closing_time", nullable = false)
    private LocalTime closingTime;

    public void update(
            String name, BigDecimal latitude, BigDecimal longitude,
            LocalTime openingTime, LocalTime closingTime, String imageUrl
    ) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        if (imageUrl != null && !imageUrl.isBlank()) {
            this.imageUrl = imageUrl;
        }
    }

    public void updateOpeningTime(LocalTime now) { this.openingTime = now; }
    public void updateClosingTime(LocalTime now) { this.closingTime = now; }
}
