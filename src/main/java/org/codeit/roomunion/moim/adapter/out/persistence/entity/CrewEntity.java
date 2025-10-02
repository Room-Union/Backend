package org.codeit.roomunion.moim.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.codeit.roomunion.moim.domain.model.Crew;
import org.codeit.roomunion.moim.domain.model.enums.CrewCategory;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "crew")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(name = "crew_image")
    private String crewImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CrewCategory category;

    @Column(nullable = false)
    @ColumnDefault("1")
    private int maxMemberCount;

    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CrewMemberEntity> crewMembers = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "crew_platform_urls", joinColumns = @JoinColumn(name = "crew_id"))
    @Column(name = "platform_url", length = 500)
    private List<String> platformUrls = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;


    public Crew toDomain(Long hostUserId) {
        return Crew.of(
            this.getId(),
            this.getName(),
            this.getDescription(),
            this.getCrewImage(),
            this.getCategory(),
            this.getMaxMemberCount(),
            hostUserId,
            this.getPlatformUrls(),
            this.getCreatedAt()
        );
    }


}
