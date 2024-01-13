package dev.example.xpenstracker.model;

import dev.example.xpenstracker.model.enumeration.CategoryName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "EXPENSE")

public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EXPENSE_ID")
    private Long expenseId;

    @NotNull(message = "Provide the spent amount")
    @Column(name = "AMOUNT", nullable = false)
    private Double amount;

    @Column(name = "EXPENSE_DATE", nullable = false)
    @NotNull(message = "Provide valid date in yyyy-mm-dd format")
    private LocalDate expenseDate;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST
    )
    @JoinColumn(name = "userId")
    //@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})//to suppress serialization for lazy fetch
    private UserInfo userInfo;

    @Column(name = "CATEGORY_NAME", nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryName categoryName;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "EXPENSE_CATEGORY_ID", referencedColumnName = "categoryId")
//    private ExpenseCategory expenseCategory;


}
