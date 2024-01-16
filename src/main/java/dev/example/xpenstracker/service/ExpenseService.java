package dev.example.xpenstracker.service;

import dev.example.xpenstracker.dto.ExpenseDto;
import dev.example.xpenstracker.dto.mapper.ExpenseDtoMapper;
import dev.example.xpenstracker.model.Expense;
import dev.example.xpenstracker.model.UserInfo;
import dev.example.xpenstracker.model.CategoryName;
import dev.example.xpenstracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final ExpenseDtoMapper expenseDtoMapper;
    private final UserService userService;
    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository, ExpenseDtoMapper expenseDtoMapper, UserService userService) {
        this.expenseRepository = expenseRepository;
        this.expenseDtoMapper = expenseDtoMapper;
        this.userService = userService;
    }


    public void postExpense(ExpenseDto expenseDto) {
        UserInfo userInfo = userService.getUserById(expenseDto.getUserId());
        Expense expense = new Expense(expenseDto.getAmount(),
                                        expenseDto.getExpenseDate(),
                                        userInfo,
                                        expenseDto.getCategoryName());
        expenseRepository.save(expense);
    }

    public List<ExpenseDto> getExpenseList() {
        for (Expense e : expenseRepository.findAll()){
            System.out.println(e.toString());
        };

        return expenseRepository.findAll()
                .stream()
                .map(expenseDtoMapper)
                .collect(Collectors.toList());
    }

    public List<ExpenseDto> getExpenseListForUsers(Long userInfoId) {
        return expenseRepository.findByUserInfoId(userInfoId)
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
        return expenseRepository.findByUserInfoIdAndCategoryName(userId, categoryName)
                .stream()
                .map(expenseDtoMapper)
                .collect(Collectors.toList());
    }

    public long getTotalExpenseForUser(Long userInfoId) {
        return expenseRepository.getTotalExpenseForUser(userInfoId);
    }

    public long getTotalExpenseForUserBetweenTimePeriod(Long userInfoId, LocalDate start, LocalDate end) {
        return expenseRepository.getTotalExpenseForUserBetweenTimePeriod(userInfoId, start, end);
    }

    public List<ExpenseDto> getExpenseListInTimePeriodForUsers(Long userInfoId, LocalDate start, LocalDate end) {
        return expenseRepository.findByUserInfoIdAndExpenseDateBetween(userInfoId, start, end)
                .stream()
                .map(expenseDtoMapper)
                .collect(Collectors.toList());

    }

    public List<ExpenseDto> getExpenseListByCategoryInTimePeriodForUsers
            (Long userId, CategoryName categoryName, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findByUserInfoIdAndCategoryNameAndExpenseDateBetween
                        (userId, categoryName, startDate, endDate)
                .stream()
                .map(expenseDtoMapper)
                .collect(Collectors.toList());
    }

    public long getTotalExpenseForUserByCategoryInTimePeriod
            (Long userId, CategoryName categoryName, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.getTotalExpenseForUserByCategoryInTimePeriod
                (userId, categoryName, startDate, endDate);
    }

    public long getTotalExpenseForUserByCategory(Long userId, CategoryName categoryName) {
        return expenseRepository.getTotalExpenseForUserByCategory(userId, categoryName);
    }

    public List<Object[]> getExpenseByUserIdAndCategoriesInTimePeriod
            (Long userId, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.getExpenseByUserIdAndCategoriesInTimePeriod
                (userId, startDate, endDate);
    }

    public List<Object[]> getExpenseByUserIdAndCategories(Long userId) {
        return expenseRepository.getExpenseByUserIdAndCategories(userId);
    }

    public void updateExpenseByExpenseId(ExpenseDto expenseDto) {
        Expense existingExpense = expenseRepository.findById(expenseDto.getId()).get();

        existingExpense.setAmount(expenseDto.getAmount());
        existingExpense.setExpenseDate(expenseDto.getExpenseDate());
        existingExpense.setCategoryName(expenseDto.getCategoryName());

        expenseRepository.save(existingExpense);
    }
}
