package dev.example.xpenstracker.controller.util;

import dev.example.xpenstracker.dto.ExpenseDto;

import java.util.List;

public class ExpenseResponse {
    private List<ExpenseDto> expenseList;
    private long totalExpense;

    public ExpenseResponse() {
    }

    public ExpenseResponse(List<ExpenseDto> expenseList, long totalExpense) {
        this.expenseList = expenseList;
        this.totalExpense = totalExpense;
    }

    public List<ExpenseDto> getExpenseList() {
        return expenseList;
    }

    public void setExpenseList(List<ExpenseDto> expenseList) {
        this.expenseList = expenseList;
    }

    public long getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(long totalExpense) {
        this.totalExpense = totalExpense;
    }
}
