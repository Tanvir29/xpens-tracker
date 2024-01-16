package dev.example.xpenstracker.dto;

import dev.example.xpenstracker.model.CategoryName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ExpenseDto {
    private Long id;
    private long amount;
    private LocalDate expenseDate;
    private Long userId;
    private CategoryName categoryName;
}
