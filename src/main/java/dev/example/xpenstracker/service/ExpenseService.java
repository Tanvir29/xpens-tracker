package dev.example.xpenstracker.service;

import dev.example.xpenstracker.dto.ExpenseDto;
import dev.example.xpenstracker.dto.mapper.ExpenseDtoMapper;
import dev.example.xpenstracker.model.Expense;
import dev.example.xpenstracker.model.UserInfo;
import dev.example.xpenstracker.model.enumeration.CategoryName;
import dev.example.xpenstracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private ExpenseDtoMapper expenseDtoMapper;
    @Autowired
    private UserService userService;

    public void postExpense(ExpenseDto expenseDto) {
        UserInfo userInfo = userService.getUserById(expenseDto.getUserId());
        Expense expense = Expense.builder().
                amount(expenseDto.getAmount()).
                expenseDate(expenseDto.getExpenseDate()).
                userInfo(userInfo).
                categoryName(expenseDto.getCategoryName()).
                build();
        expenseRepository.save(expense);
    }

    public List<ExpenseDto> getExpenseList() {
        return expenseRepository.findAll()
                .stream()
                .map(expenseDtoMapper)
                .collect(Collectors.toList());
    }

    public List<ExpenseDto> getExpenseListForUsers(Long userInfoId) {
        return expenseRepository.findByUserInfoUserId(userInfoId)
                .stream()
                .map(expenseDtoMapper)
                .collect(Collectors.toList());
    }

    public void deleteExpenseByExpenseId(Long expenseId) {
        expenseRepository.deleteById(expenseId);
    }

    public ExpenseDto getExpenseByExpenseId(Long expenseId) {
        return expenseRepository.findById(expenseId)
                .map(expenseDtoMapper)
                .get();
    }

    public List<ExpenseDto> getExpenseListByUserIdAndCategory(Long userId, CategoryName categoryName) {
        return expenseRepository.findByUserInfoUserIdAndCategoryName(userId, categoryName)
                .stream()
                .map(expenseDtoMapper)
                .collect(Collectors.toList());
    }

    public Long getTotalExpenseForUser(Long userInfoId) {
        return expenseRepository.getTotalExpenseForUser(userInfoId);
    }

    public Long getTotalExpenseForUserBetweenTimePeriod(Long userInfoId, LocalDate start, LocalDate end) {
        return expenseRepository.getTotalExpenseForUserBetweenTimePeriod(userInfoId, start, end);
    }

    public List<ExpenseDto> getExpenseListInTimePeriodForUsers(Long userInfoId, LocalDate start, LocalDate end) {
        return expenseRepository.findByUserInfoUserIdAndExpenseDateBetween(userInfoId, start, end)
                .stream()
                .map(expenseDtoMapper)
                .collect(Collectors.toList());

    }

    public List<ExpenseDto> getExpenseListByCategoryInTimePeriodForUsers
            (Long userId, CategoryName categoryName, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findByUserInfoUserIdAndCategoryNameAndExpenseDateBetween
                        (userId, categoryName, startDate, endDate)
                .stream()
                .map(expenseDtoMapper)
                .collect(Collectors.toList());
    }

    public Long getTotalExpenseForUserByCategoryInTimePeriod
            (Long userId, CategoryName categoryName, LocalDate startDate, LocalDate endDate) {
        String categoryNameString = categoryName.name();
        return expenseRepository.getTotalExpenseForUserByCategoryInTimePeriod
                (userId, categoryNameString, startDate, endDate);
    }

    public Long getTotalExpenseForUserByCategory(Long userId, CategoryName categoryName) {
        String categoryNameString = categoryName.name();
        return expenseRepository.getTotalExpenseForUserByCategory(userId, categoryNameString);
    }

    public List<Object[]> getExpenseByUserIdAndCategoriesInTimePeriod
            (Long userId, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.getExpenseByUserIdAndCategoriesInTimePeriod
                (userId, startDate, endDate);
    }

    public List<Object[]> getExpenseByUserIdAndCategories(Long userId) {
        return expenseRepository.getExpenseByUserIdAndCategories(userId);
    }

    public void updateExpenseByExpenseId(Long expenseId, ExpenseDto expenseDto) {
        Expense existingExpense = expenseRepository.findById(expenseId).get();

        existingExpense.setAmount(expenseDto.getAmount());
        existingExpense.setExpenseDate(expenseDto.getExpenseDate());
        existingExpense.setCategoryName(expenseDto.getCategoryName());

        expenseRepository.save(existingExpense);
    }
}
