package dev.example.xpenstracker.service;

import dev.example.xpenstracker.dto.ExpenseDto;
import dev.example.xpenstracker.dto.mapper.ExpenseDtoMapper;
import dev.example.xpenstracker.model.Expense;
import dev.example.xpenstracker.model.UserInfo;
import dev.example.xpenstracker.model.CategoryName;
import dev.example.xpenstracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Page<Expense> getExpenseList(int totalPage, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortField).ascending():
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(totalPage, pageSize, sort);
        return expenseRepository.findAll(pageable);
    }

    public List<Expense> userExpenseList(Long userInfoId) {
        return expenseRepository.findByUserInfoId(userInfoId);
    }

    public void deleteExpenseByExpenseId(Long expenseId) {
        expenseRepository.deleteById(expenseId);
    }

    public ExpenseDto getExpenseByExpenseId(Long expenseId) {
        return expenseRepository.findById(expenseId).
                map(expenseDtoMapper).get();
    }

    public List<Expense> expenseListForSpecificCategory(Long userId, CategoryName categoryName) {
        return expenseRepository.findByUserInfoIdAndCategoryName(userId, categoryName);
    }

    public long totalExpense(Long userInfoId) {
        return expenseRepository.getTotalExpenseForUser(userInfoId);
    }

    public long totalExpenseInTimePeriod(Long userInfoId, LocalDate start, LocalDate end) {
        return expenseRepository.getTotalExpenseForUserBetweenTimePeriod(userInfoId, start, end);
    }

    public List<Expense> expenseListInTimePeriod(Long userInfoId, LocalDate start, LocalDate end) {
        return expenseRepository.findByUserInfoIdAndExpenseDateBetween(userInfoId, start, end);
    }

    public List<Expense> expenseListForSpecificCategoryInTimePeriod
            (Long userId, CategoryName categoryName, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findByUserInfoIdAndCategoryNameAndExpenseDateBetween
                        (userId, categoryName, startDate, endDate);
    }

    public long totalExpenseForSpecificCategoryInTimePeriod
            (Long userId, CategoryName categoryName, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.getTotalExpenseForUserByCategoryInTimePeriod
                (userId, categoryName, startDate, endDate);
    }

    public long totalExpenseForSpecificCategory(Long userId, CategoryName categoryName) {
        return expenseRepository.getTotalExpenseForUserByCategory(userId, categoryName);
    }

    public List<Object[]> categoryWiseUserExpensesInTimePeriod
            (Long userId, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.getExpenseByUserIdAndCategoriesInTimePeriod
                (userId, startDate, endDate);
    }

    public List<Object[]> categoryWiseUserExpenses(Long userId) {
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
