package com.example.shopping.entity.Base;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

// Auditing을 적용하기 위해서 @EntityListeners 어노테이션을 추가해야 합니다.
@EntityListeners(value = {AuditingEntityListener.class})
// 공통 매핑 정보가 필요할 때 사용하는 어노테이션으로
// 부모 클래스를 상속 받는 자식 클래스에 매핑 정보만 제공합니다.
@MappedSuperclass
@Getter
@ToString
public class BaseTimeEntity {
    // 엔티티가 생성되어 저장될 때 시간을 자동으로 저장
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regTime;

    // 엔티티 값을 변경할 때 시간을 자동으로 저장
    @LastModifiedDate
    private LocalDateTime updateTime;
}
