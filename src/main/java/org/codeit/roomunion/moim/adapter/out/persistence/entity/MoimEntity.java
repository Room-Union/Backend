package org.codeit.roomunion.moim.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.codeit.roomunion.moim.domain.model.Moim;
import org.codeit.roomunion.moim.domain.model.enums.MoimCategory;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "moim")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoimEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(name = "moim_image")
    private String moimImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MoimCategory category;

    @Column(nullable = false)
    @ColumnDefault("1")
    private int maxMemberCount;

    @OneToMany(mappedBy = "moim", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MoimMemberEntity> moimMembers = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "moim_platform_urls", joinColumns = @JoinColumn(name = "moim_id"))
    @Column(name = "platform_url", length = 500)
    private List<String> platformUrls = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;


    public Moim toDomain(Long hostUserId) {
        return Moim.of(
            this.getId(),
            this.getName(),
            this.getDescription(),
            this.getMoimImage(),
            this.getCategory(),
            this.getMaxMemberCount(),
            hostUserId,
            this.getPlatformUrls(),
            this.getCreatedAt()
        );
    }


}
