package dev.example.xpenstracker.controller;

import dev.example.xpenstracker.controller.util.ExpenseResponse;
import dev.example.xpenstracker.dto.ExpenseDto;
import dev.example.xpenstracker.model.CategoryName;
import dev.example.xpenstracker.model.Expense;
import dev.example.xpenstracker.service.ExpenseService;
import dev.example.xpenstracker.service.util.Pagination;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FunctionalInterface
interface ExpenseOperation {
    ExpenseResponse apply(ExpenseResponse response);
}

@Controller
public class ExpenseController {

    private final ExpenseService expenseService;
    private final Pagination pagination;

    public ExpenseController(ExpenseService expenseService, Pagination pagination) {
        this.expenseService = expenseService;
        this.pagination = pagination;
    }


    @ModelAttribute("categories")
    public CategoryName[] categories(){
        return CategoryName.values();
    }

    @PostMapping("/user/{id}/saveExpense/")
    public String saveExpense(@PathVariable Long id, @Valid @ModelAttribute("expense") ExpenseDto expenseDto){
        expenseService.postExpense(expenseDto);
        return "redirect:/user/" + id;
    }

    @PostMapping("/user/{id}/updateExpense/")
    public String updateExpense(@ModelAttribute("expense") ExpenseDto expenseDto,
                                @PathVariable Long id){
        expenseService.updateExpenseByExpenseId(expenseDto);
        return "redirect:/user/" + id + "/expenseList/";
    }
    @GetMapping("/user/{id}/newExpenseForm/")
    public String showNewExpenseForm(@PathVariable Long id, Model model){
        ExpenseDto expenseDto = new ExpenseDto();
        expenseDto.setUserId(id);
        model.addAttribute("expense", expenseDto);
        model.addAttribute("userInfoId", id);
        return "newExpense";
    }
    @GetMapping("/user/{userId}/expenseList/{id}/updateExpense/")
    public String updateExpenseForm(@PathVariable Long id,
                                    @PathVariable Long userId,
                                    Model model) {
        ExpenseDto expenseDto = getExpenseByExpenseId(id);
        model.addAttribute("expense", expenseDto);
        model.addAttribute("userInfoId", userId);
        return "updateExpense";
    }

    @GetMapping("/expense/")
    public String viewExpenseList(@RequestParam(defaultValue = "0") int pageNo,
                                  @RequestParam(defaultValue = "10") int pageSize,
                                  @RequestParam(defaultValue = "id") String sortField,
                                  @RequestParam(defaultValue = "ASC") String sortDirection,
                                  Model model){
        Pageable pageable = pagination.getPaginationMode(pageNo,pageSize, sortField, sortDirection);
        Page<Expense> page  = expenseService.getExpenseList(pageable);
        List<Expense> expenses = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("listExpenses", expenses);
        model.addAttribute("sortBy", sortField);
        model.addAttribute("sortOrder", sortDirection);
        return "expenseList";
    }

    public ExpenseDto getExpenseByExpenseId(@PathVariable Long id) {
        return expenseService.getExpenseByExpenseId(id);
    }

    @GetMapping("/user/{id}/expenseByCategory/")
    public String getCategoryWiseUserExpenses(@PathVariable Long id,
                                              @RequestParam(value = "startDate", required = false)
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                              @RequestParam(value = "endDate", required = false)
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                              Model model) {

        List<Object[]> expenseResults;
        if (startDate != null && endDate != null) {
            expenseResults = expenseService.categoryWiseUserExpensesInTimePeriod
                    (id, startDate, endDate);
        } else {
            expenseResults = expenseService.categoryWiseUserExpenses(id);
        }
        model.addAttribute("expenseResults", expenseResults);
        return "userExpenseByCategory";
    }

    @GetMapping("/user/{id}/expenseList/")
    public String getUserExpenses(@PathVariable Long id,
                                  @RequestParam(value = "categoryName", required = false)
                                  CategoryName categoryName,
                                  @RequestParam(value = "startDate", required = false)
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                  @RequestParam(value = "endDate", required = false)
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                  @RequestParam(defaultValue = "0") int pageNo,
                                  @RequestParam(defaultValue = "10") int pageSize,
                                  @RequestParam(defaultValue = "id") String sortField,
                                  @RequestParam(defaultValue = "ASC") String sortDirection,
                                  Model model) {

        Pageable pageable = pagination.getPaginationMode(pageNo,pageSize, sortField, sortDirection);
        ExpenseOperation operationWithTimePeriod = response -> {
            response.setExpenseList
                    (expenseService.expenseListInTimePeriod(id, startDate, endDate,
                            pageable));
            response.setTotalExpense
                    (expenseService.totalExpenseInTimePeriod(id, startDate, endDate));
            return response;
        };

        ExpenseOperation operationWithoutTimePeriod = response -> {
            response.setExpenseList
                    (expenseService.userExpenseList(id, pageable));
            response.setTotalExpense(
                    expenseService.totalExpense(id));
            return response;
        };

        ExpenseOperation operationWithCategoryAndTimePeriod = response -> {
            response.setExpenseList
                    (expenseService.expenseListForSpecificCategoryInTimePeriod
                            (id, categoryName, startDate, endDate,
                                    pageable));
            response.setTotalExpense
                    (expenseService.totalExpenseForSpecificCategoryInTimePeriod
                            (id, categoryName, startDate, endDate));
            return response;
        };

        ExpenseOperation operationWithCategoryWithoutTimePeriod = response -> {
            response.setExpenseList(
                    expenseService.expenseListForSpecificCategory(id, categoryName,
                            pageable));
            response.setTotalExpense
                    (expenseService.totalExpenseForSpecificCategory(id,categoryName));
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
        model.addAttribute("expenseResponse", expenseResponse);
        model.addAttribute("userInfoId", id);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages",expenseResponse.getExpenseList().getTotalPages());
        model.addAttribute("sortBy", sortField);
        model.addAttribute("sortOrder", sortDirection);
        return "userExpenseList";
    }

    @GetMapping("/user/{userId}/expenseList/{id}/deleteExpense/")
    public String deleteExpenseByExpenseId(@PathVariable Long userId,
                                           @PathVariable Long id,
                                           Model model) {
        expenseService.deleteExpenseByExpenseId(id);
        return "redirect:/user/" + userId + "/expenseList/";
    }
}
