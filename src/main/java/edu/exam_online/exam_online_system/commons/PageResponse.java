package edu.exam_online.exam_online_system.commons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    private List<T> items;
    private int page;
    private int size;
    private long total;

    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }

    public static <T> PageResponse<T> empty(int page, int size) {
        return PageResponse.<T>builder()
                .items(Collections.emptyList())
                .page(page)
                .size(size)
                .total(0)
                .build();
    }

    public <R> PageResponse<R> map(Function<T, R> mapper) {
        List<R> mappedItems = items == null ? Collections.emptyList() : items.stream()
                .map(mapper)
                .toList();
        return PageResponse.<R>builder()
                .items(mappedItems)
                .page(page)
                .size(size)
                .total(total)
                .build();
    }
}
