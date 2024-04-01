package com.example.demandForm.entity;

import com.example.product.entity.Option;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demand_form_id")
    private DemandForm demandForm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private Option option;

    public static DemandOption toEntity(int quantity, DemandForm demandForm, Option option) {

        return DemandOption.builder()
                .quantity(quantity)
                .demandForm(demandForm)
                .option(option)
                .build();
    }
}
