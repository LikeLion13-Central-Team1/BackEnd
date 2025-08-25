package com.study.demo.backend.global.scheduler;

import com.study.demo.backend.domain.menu.entity.Menu;
import com.study.demo.backend.domain.menu.repository.MenuRepository;
import com.study.demo.backend.domain.store.entity.Store;
import com.study.demo.backend.domain.store.repository.StoreRepository;
import org.hibernate.engine.jdbc.env.internal.LobCreationLogging;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockResetScheduler {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    
//  서비스 시연을 위한 재고 초기화 기능 설정 해제
//    // 18시 ~ 06시까지 자동으로 마감시간 판단하여 재고 0으로 30분 간격으로 처리하기
//    @Scheduled(cron = "0 */30 18-23 * * *")
//    @Scheduled(cron = "0 */30 0-5 * * *")
//    @Scheduled(cron = "0 0 6 * * *")
//    @Transactional
//    public void resetStockBasedOnClosingTime() {
//        LocalTime now = LocalTime.now();
//        log.info("마감 시간 기반 재고 리셋 스케줄러 실행: {}", now);
//
//        List<Store> allStores = storeRepository.findAll();
//
//        for (Store store : allStores) {
//            if (now.isAfter(store.getClosingTime())) {
//                log.info("{} 가게의 마감 시간이 지났습니다. 재고를 0으로 리셋합니다.", store.getName());
//
//                List<Menu> menusToReset = menuRepository.findByStore(store);
//
//                for (Menu menu : menusToReset) {
//                    menu.updateQuantity(0);
//                }
//                menuRepository.saveAll(menusToReset);
//
//                log.info("{} 가게의 메뉴 {}개의 재고가 성공적으로 리셋되었습니다.", store.getName(), menusToReset.size());
//            }
//        }
//
//        log.info("재고 리셋 스케줄러 종료");
//    }

    @Scheduled(cron = "0 */3 * * * *") // 3분 간격으로
    @Transactional
    public void updateOpenStatus() {
        LocalTime now = LocalTime.now();
        log.info("가게 오픈 상태 확인 스케줄러 실행: {}", now);

        List<Store> allStores = storeRepository.findAll();
        for (Store store : allStores) {
            boolean before = store.isOpenStatus();
            store.updateOpenStatus();

            if (before != store.isOpenStatus()) {
                log.info("가게 상태 변경: {} -> {}", store.getName(), store.isOpenStatus() ? "OPEN" : "CLOSED");
            }
        }
        log.info("가게 오픈 상태 확인 스케줄러 종료");
    }

}
