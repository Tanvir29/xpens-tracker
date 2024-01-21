package dev.example.xpenstracker.repository;

import dev.example.xpenstracker.model.Expense;
import dev.example.xpenstracker.model.CategoryName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    public List<Expense> findByUserInfoId(long userId);

    public List<Expense> findByUserInfoIdAndCategoryName
            (long userId, CategoryName categoryName);

    public List<Expense> findByUserInfoIdAndExpenseDateBetween
            (long userId, LocalDate start, LocalDate end);

    public List<Expense> findByUserInfoIdAndCategoryNameAndExpenseDateBetween
            (long userId, CategoryName categoryName, LocalDate startDate, LocalDate endDate);

    @Query("select COALESCE(SUM(e.amount), 0) as totalExpense " +
            "from Expense e " +
            "where e.userInfo.id = :userId ")

    public long getTotalExpenseForUser(@Param("userId") long userId);


    @Query("select COALESCE(SUM(e.amount), 0) as totalExpense " +
            "from Expense e " +
            "where e.userInfo.id = :userId " +
            "and e.expenseDate between :startDate and :endDate")

    public long getTotalExpenseForUserBetweenTimePeriod(@Param("userId") long userId,
                                                        @Param("startDate") LocalDate startDate,
                                                        @Param("endDate") LocalDate endDate);

    @Query("select COALESCE(SUM(e.amount), 0) as totalExpense " +
            "from Expense e " +
            "where e.userInfo.id = :userId " +
            "and e.categoryName = :categoryName " +
            "and e.expenseDate between :startDate and :endDate")

    public long getTotalExpenseForUserByCategoryInTimePeriod
            (@Param("userId") long userId,
             @Param("categoryName") CategoryName categoryName,
             @Param("startDate") LocalDate startDate,
             @Param("endDate") LocalDate endDate);

    @Query("select COALESCE(SUM(e.amount), 0) as totalExpense " +
            "from Expense e " +
            "where e.userInfo.id = :userId " +
            "and e.categoryName = :categoryName ")

    public long getTotalExpenseForUserByCategory
            (@Param("userId") long userId,
             @Param("categoryName") CategoryName categoryName);

    @Query(
            "select e.categoryName as Category, SUM(e.amount) as TotalExpense " +
            "from Expense e " +
            "where e.userInfo.id = :userId " +
            "and e.expenseDate between :startDate and :endDate " +
            "GROUP BY Category ORDER BY TotalExpense "
    )
    public List<Object[]> getExpenseByUserIdAndCategoriesInTimePeriod
            (@Param("userId") long userId,
             @Param("startDate") LocalDate startDate,
             @Param("endDate") LocalDate endDate);

@Query(
        "select e.categoryName as Category, SUM(e.amount) as TotalExpense " +
        "from Expense e " +
        "where e.userInfo.id = :userId " +
        "GROUP BY Category ORDER BY TotalExpense "
    )
    public List<Object[]> getExpenseByUserIdAndCategories(@Param("userId") long userId);

}
