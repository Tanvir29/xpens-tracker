package dev.example.xpenstracker.controller.util;

import dev.example.xpenstracker.dto.ExpenseDto;
import dev.example.xpenstracker.model.Expense;
import org.springframework.data.domain.Page;

import java.util.List;

public class ExpenseResponse {
    private Page<Expense> expenseList;
    private long totalExpense;

    public ExpenseResponse() {
    }

    public ExpenseResponse(Page<Expense> expenseList, long totalExpense) {
        this.expenseList = expenseList;
        this.totalExpense = totalExpense;
    }

    public Page<Expense> getExpenseList() {
        return expenseList;
    }

    public void setExpenseList(Page<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    public long getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(long totalExpense) {
        this.totalExpense = totalExpense;
    }
}
