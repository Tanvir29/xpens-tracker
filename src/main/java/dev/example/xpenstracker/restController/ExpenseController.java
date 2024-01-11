package dev.example.xpenstracker.restController;

import dev.example.xpenstracker.dto.ExpenseDto;
import dev.example.xpenstracker.model.enumeration.CategoryName;
import dev.example.xpenstracker.service.ExpenseService;
import dev.example.xpenstracker.service.UserService;
import dev.example.xpenstracker.util.ExpenseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FunctionalInterface
interface ExpenseOperation {
    ExpenseResponse apply(ExpenseResponse response);
}

@RestController
@RequestMapping({"/expense/", "/expense"})
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<String> postExpense(@RequestBody ExpenseDto expenseDto) {
        expenseService.postExpense(expenseDto);
        return ResponseEntity.ok("Expense posted successfully");
    }

    @GetMapping
    public List<ExpenseDto> getExpenseList() {
        return expenseService.getExpenseList();
    }

    @GetMapping({"/{id}/", "/{id}"})
    public ExpenseDto getExpenseByExpenseId(@PathVariable("id") Long expenseId) {
        return expenseService.getExpenseByExpenseId(expenseId);
    }

    @GetMapping("/user/{id}/expenseByCategory")
    public List<Object[]> getExpenseByUserIdAndAllCategory(@PathVariable("id") Long userId,
                                                           @RequestParam(value = "startDate", required = false)
                                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                           @RequestParam(value = "endDate", required = false)
                                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return expenseService.getExpenseByUserIdAndCategoriesInTimePeriod
                    (userId, startDate, endDate);
        } else {
            return expenseService.getExpenseByUserIdAndCategories(userId);
        }
    }

    @GetMapping({"/user/{userId}", "/user/{userId}/"})
    public ExpenseResponse getExpensesForUser(@PathVariable("userId") Long userId,
                                              @RequestParam(value = "categoryName", required = false)
                                              CategoryName categoryName,
                                              @RequestParam(value = "startDate", required = false)
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                              @RequestParam(value = "endDate", required = false)
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        ExpenseOperation operationWithTimePeriod = response -> {
            response.setExpenseList
                    (expenseService.getExpenseListInTimePeriodForUsers(userId, startDate, endDate));
            response.setTotalExpense
                    (expenseService.getTotalExpenseForUserBetweenTimePeriod(userId, startDate, endDate));
            return response;
        };

        ExpenseOperation operationWithoutTimePeriod = response -> {
            response.setExpenseList
                    (expenseService.getExpenseListForUsers(userId));
            response.setTotalExpense(
                    expenseService.getTotalExpenseForUser(userId));
            return response;
        };

        ExpenseOperation operationWithCategoryAndTimePeriod = response -> {
            response.setExpenseList
                    (expenseService.getExpenseListByCategoryInTimePeriodForUsers
                            (userId, categoryName, startDate, endDate));
            response.setTotalExpense
                    (expenseService.getTotalExpenseForUserByCategoryInTimePeriod
                            (userId, categoryName, startDate, endDate));
            return response;
        };

        ExpenseOperation operationWithCategoryWithoutTimePeriod = response -> {
            response.setExpenseList(
                    expenseService.getExpenseListByUserIdAndCategory(userId, categoryName));
            response.setTotalExpense
                    (expenseService.getTotalExpenseForUserByCategory(userId, categoryName));
            return response;
        };

        Map<String, ExpenseOperation> operations = new HashMap<>();
        operations.put("category_time", operationWithCategoryAndTimePeriod);
        operations.put("category_no_time", operationWithCategoryWithoutTimePeriod);
        operations.put("no_category_time", operationWithTimePeriod);
        operations.put("no_category_no_time", operationWithoutTimePeriod);

        String key = (categoryName != null ? "category_" : "no_category_")
                + (startDate != null && endDate != null ? "time" : "no_time");

        ExpenseResponse expenseResponse = operations.get(key).apply(new ExpenseResponse());
        return expenseResponse;
    }

    @DeleteMapping({"/{id}/", "/{id}"})
    public String deleteExpenseByExpenseId(@PathVariable("id") Long expenseId) {
        expenseService.deleteExpenseByExpenseId(expenseId);
        return "Expense deleted";
    }

    @PutMapping({"/{id}/", "/{id}"})
    public ResponseEntity<String> updateExpenseByExpenseId(@PathVariable("id") Long expenseId, @RequestBody ExpenseDto expenseDto) {
        expenseService.updateExpenseByExpenseId(expenseId, expenseDto);
        return ResponseEntity.ok("Expense updated");
    }
}
