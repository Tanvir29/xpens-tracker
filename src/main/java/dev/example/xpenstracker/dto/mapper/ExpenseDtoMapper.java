package dev.example.xpenstracker.dto.mapper;

import dev.example.xpenstracker.dto.ExpenseDto;
import dev.example.xpenstracker.model.Expense;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ExpenseDtoMapper implements Function<Expense, ExpenseDto> {
    @Override
    public ExpenseDto apply(Expense expense) {
        return new ExpenseDto(
                expense.getExpenseId(),
                expense.getAmount(),
                expense.getExpenseDate(),
                expense.getUserInfo().getUserId(),
                expense.getCategoryName()
        );
    }
}
