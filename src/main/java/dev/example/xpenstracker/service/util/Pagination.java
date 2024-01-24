package dev.example.xpenstracker.service.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class Pagination {
    public Pagination() {
    }

    public Pageable getPaginationMode(int totalPage ,int pageSize,
                                      String sortField,@NotNull String sortDirection){
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortField).ascending():
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(totalPage, pageSize, sort);
        return pageable;
    }
}
