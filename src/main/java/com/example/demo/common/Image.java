package com.example.demo.common;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image extends BaseEntity {
    @Column
    private String title;
    @Column(name = "is_main")
    private boolean isMain;

    @Column(nullable = false)
    private String fileName;
    @Column(nullable = false)
    private String contentType;
    private long size;
    @Lob
    @Column(length = 1000000)
    private byte[] data;
}
