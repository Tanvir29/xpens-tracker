package dev.example.xpenstracker.controller;

import dev.example.xpenstracker.dto.ExpenseDto;
import dev.example.xpenstracker.model.CategoryName;
import dev.example.xpenstracker.model.Expense;
import dev.example.xpenstracker.service.ExpenseService;
import dev.example.xpenstracker.service.UserService;
import dev.example.xpenstracker.controller.util.ExpenseResponse;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
//@RequestMapping({"/expense/", "/expense"})
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserService userService;
    @Autowired
    public ExpenseController(ExpenseService expenseService, UserService userService) {
        this.expenseService = expenseService;
        this.userService = userService;
    }

    @ModelAttribute("categories")
    public CategoryName[] categories(){
        return CategoryName.values();
    }

    @PostMapping("/expense/saveExpense")
    public String saveExpense(@Valid @ModelAttribute("expense") ExpenseDto expenseDto){
        expenseService.postExpense(expenseDto);
        return "redirect:/expense/";
    }

    @PostMapping("/expense/updateExpense")
    public String updateExpense(@ModelAttribute("expense") ExpenseDto expenseDto){
        expenseService.updateExpenseByExpenseId(expenseDto);
        return "redirect:/expense/";
    }

    @GetMapping("/expense/")
    public String viewExpenseList(@RequestParam(defaultValue = "0") int pageNo,
                                  @RequestParam(defaultValue = "8") int pageSize,
                                  @RequestParam(defaultValue = "id") String sortField,
                                  @RequestParam(defaultValue = "ASC") String sortDirection,
                                  Model model){

        Page<Expense> page  = expenseService.getExpenseList(pageNo, pageSize, sortField, sortDirection );
        List<Expense> expenses = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("listExpenses", expenses);
        model.addAttribute("sortBy", sortField);
        model.addAttribute("sortOrder", sortDirection);
        return "expenseList";
    }

    @GetMapping("/expense/newExpenseForm")
    public String showNewExpenseForm(Model model){
        ExpenseDto expenseDto = new ExpenseDto();
        model.addAttribute("expense", expenseDto);
        return "newExpense";
    }

    public ExpenseDto getExpenseByExpenseId(@PathVariable Long id) {
        return expenseService.getExpenseByExpenseId(id);
    }

    @GetMapping("/user/{id}/expenseByCategory")
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

    @GetMapping({"/user/{id}", "/user/{id}/"})
    public String getUserExpenses(@PathVariable Long id,
                                              @RequestParam(value = "categoryName", required = false)
                                              CategoryName categoryName,
                                              @RequestParam(value = "startDate", required = false)
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                              @RequestParam(value = "endDate", required = false)
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                Model model) {

        ExpenseOperation operationWithTimePeriod = response -> {
            response.setExpenseList
                    (expenseService.expenseListInTimePeriod(id, startDate, endDate));
            response.setTotalExpense
                    (expenseService.totalExpenseInTimePeriod(id, startDate, endDate));
            return response;
        };

        ExpenseOperation operationWithoutTimePeriod = response -> {
            response.setExpenseList
                    (expenseService.userExpenseList(id));
            response.setTotalExpense(
                    expenseService.totalExpense(id));
            return response;
        };

        ExpenseOperation operationWithCategoryAndTimePeriod = response -> {
            response.setExpenseList
                    (expenseService.expenseListForSpecificCategoryInTimePeriod
                            (id, categoryName, startDate, endDate));
            response.setTotalExpense
                    (expenseService.totalExpenseForSpecificCategoryInTimePeriod
                            (id, categoryName, startDate, endDate));
            return response;
        };

        ExpenseOperation operationWithCategoryWithoutTimePeriod = response -> {
            response.setExpenseList(
                    expenseService.expenseListForSpecificCategory(id, categoryName));
            response.setTotalExpense
                    (expenseService.totalExpenseForSpecificCategory(id, categoryName));
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
        return "userExpenseList";
    }

    @GetMapping("/expense/updateExpense/{id}")
    public String updateExpenseByExpenseId(@PathVariable Long id, Model model) {
        ExpenseDto expenseDto = getExpenseByExpenseId(id);
        model.addAttribute("expense", expenseDto);
        return "updateExpense";
    }

    @GetMapping("/expense/deleteExpense/{id}")
    public String deleteExpenseByExpenseId(@PathVariable Long id, Model model) {
        expenseService.deleteExpenseByExpenseId(id);
        return "redirect:/expense/";
    }
}
