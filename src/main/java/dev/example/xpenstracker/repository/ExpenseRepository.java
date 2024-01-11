package dev.example.xpenstracker.repository;

import dev.example.xpenstracker.model.Expense;
import dev.example.xpenstracker.model.enumeration.CategoryName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    public List<Expense> findByUserInfoUserId(Long userInfoId);

    public List<Expense> findByUserInfoUserIdAndCategoryName
            (Long userId, CategoryName categoryName);

    public List<Expense> findByUserInfoUserIdAndExpenseDateBetween
            (Long userInfoId, LocalDate start, LocalDate end);

    public List<Expense> findByUserInfoUserIdAndCategoryNameAndExpenseDateBetween
            (Long userId, CategoryName categoryName, LocalDate startDate, LocalDate endDate);

    @Query(
            value = "SELECT SUM(amount) AS total_expenses FROM expense " +
                    "WHERE user_id = :userId",
            nativeQuery = true
    )
    public Long getTotalExpenseForUser(@Param("userId") Long userInfoId);

    @Query(
            value = "SELECT SUM(amount) AS total_expenses FROM expense " +
                    "WHERE user_id = :userId " +
                    "AND expense_date BETWEEN :start AND :end",
            nativeQuery = true
    )

    public Long getTotalExpenseForUserBetweenTimePeriod(@Param("userId") Long userInfoId,
                                                        @Param("start") LocalDate startDate,
                                                        @Param("end") LocalDate endDate);

    @Query(
            value = "SELECT SUM(amount) AS total_expenses FROM expense " +
                    "WHERE user_id = :userId " +
                    "AND category_name = :category " +
                    "AND expense_date BETWEEN :start AND :end",
            nativeQuery = true
    )
    public Long getTotalExpenseForUserByCategoryInTimePeriod
            (@Param("userId") Long userInfoId,
             @Param("category") String categoryName,
             @Param("start") LocalDate startDate,
             @Param("end") LocalDate endDate);

    @Query(
            value = "SELECT COALESCE(SUM(amount), 0) AS total_expenses FROM expense " +
                    "WHERE user_id = :userId " +
                    "AND category_name = :category ",
            nativeQuery = true
    )
    public Long getTotalExpenseForUserByCategory
            (@Param("userId") Long userInfoId,
             @Param("category") String categoryName);

    @Query(
            value = "SELECT COALESCE(category_name, 'total_expense') AS category_name, COALESCE(SUM(amount), 0) AS category_expense " +
                    "FROM expense " +
                    "WHERE user_id = :userId " +
                    "AND expense_date BETWEEN :startDate AND :endDate " +
                    "GROUP BY category_name " +
                    "WITH ROLLUP",
            nativeQuery = true
    )
    public List<Object[]> getExpenseByUserIdAndCategoriesInTimePeriod
            (@Param("userId") Long userId,
             @Param("startDate") LocalDate startDate,
             @Param("endDate") LocalDate endDate);

    @Query(
            value = "SELECT COALESCE(category_name, 'total_expense') AS category_name, COALESCE(SUM(amount), 0) AS category_expense " +
                    "FROM expense " +
                    "WHERE user_id = :userId " +
                    "GROUP BY category_name " +
                    "WITH ROLLUP",
            nativeQuery = true
    )
    public List<Object[]> getExpenseByUserIdAndCategories(@Param("userId") Long userId);
}
