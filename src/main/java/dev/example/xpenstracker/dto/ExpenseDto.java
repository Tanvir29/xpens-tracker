package dev.example.xpenstracker.dto;

import dev.example.xpenstracker.model.enumeration.CategoryName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ExpenseDto {
    private Long expenseId;
    private Double amount;
    private LocalDate expenseDate;
    private Long userId;
    private CategoryName categoryName;
}
