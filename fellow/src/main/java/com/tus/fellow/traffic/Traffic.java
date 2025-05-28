package com.tus.fellow.traffic;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Traffic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int networkId;

    private double volume;

    private LocalDateTime timestamp;

}
