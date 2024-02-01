package dev.example.xpenstracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity

public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long amount;

    @NotNull
    //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE, fallbackPatterns = { "d/M/yy", "dd.MM.yyyy" })
    private LocalDate expenseDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserInfo userInfo;

    @Enumerated(EnumType.STRING)
    private CategoryName categoryName;

    public Expense() {
    }

    public Expense(long amount, LocalDate expenseDate, UserInfo userInfo, CategoryName categoryName) {
        this.amount = amount;
        this.expenseDate = expenseDate;
        this.userInfo = userInfo;
        this.categoryName = categoryName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public CategoryName getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(CategoryName categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", amount=" + amount +
                ", expenseDate=" + expenseDate +
                ", userInfo=" + userInfo +
                ", categoryName=" + categoryName +
                '}';
    }
}
