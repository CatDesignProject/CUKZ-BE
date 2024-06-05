package com.example.demandForm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DemandOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    private Long optionId;

    private String optionName;

    private int additionalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demand_form_id")
    private DemandForm demandForm;
}
