package edu.java.scrapper.domain.jpa.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Chat {

    @Id
    private Long id;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "chat_link",
               joinColumns = {@JoinColumn(name = "chat_id")},
               inverseJoinColumns = {@JoinColumn(name = "link_id")})
    private List<Link> links = new ArrayList<>();
}
