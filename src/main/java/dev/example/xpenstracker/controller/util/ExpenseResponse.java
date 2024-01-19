package dev.example.xpenstracker.controller.util;

import dev.example.xpenstracker.dto.ExpenseDto;
import dev.example.xpenstracker.model.Expense;

import java.util.List;

public class ExpenseResponse {
    private List<Expense> expenseList;
    private long totalExpense;

    public ExpenseResponse() {
    }

    public ExpenseResponse(List<Expense> expenseList, long totalExpense) {
        this.expenseList = expenseList;
        this.totalExpense = totalExpense;
    }

    public List<Expense> getExpenseList() {
        return expenseList;
    }

    public void setExpenseList(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    public long getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(long totalExpense) {
        this.totalExpense = totalExpense;
    }
}
