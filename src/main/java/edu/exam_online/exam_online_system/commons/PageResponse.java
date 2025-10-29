package edu.exam_online.exam_online_system.commons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

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

    /* =====================
     *  Convenience methods
     * ===================== */

    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public int getTotalPages() {
        if (size <= 0) return 0;
        return (int) Math.ceil((double) total / size);
    }

    /* =====================
     *  Static factory methods
     * ===================== */

    public static <T> PageResponse<T> empty(int page, int size) {
        return PageResponse.<T>builder()
                .items(Collections.emptyList())
                .page(page)
                .size(size)
                .total(0)
                .build();
    }

    public static <T> PageResponse<T> of(List<T> items, int page, int size, long total) {
        return PageResponse.<T>builder()
                .items(items != null ? items : Collections.emptyList())
                .page(page)
                .size(size)
                .total(total)
                .build();
    }

    public static <T> PageResponse<T> success(List<T> items, int page, int size, long total) {
        return of(items, page, size, total);
    }

    public static <T> PageResponse<T> success(PageResponse<T> page) {
        if (page == null) return empty(0, 0);
        return of(page.getItems(), page.getPage(), page.getSize(), page.getTotal());
    }
    public static <T> PageResponse<T> success(Page<T> page) {
        if (page == null || page.isEmpty()) {
            return empty(0, 0);
        }
        return PageResponse.<T>builder()
                .items(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .total(page.getTotalElements())
                .build();
    }

    /* =====================
     *  Mapping utilities
     * ===================== */

    public <R> PageResponse<R> map(Function<T, R> mapper) {
        List<R> mappedItems = items == null ? Collections.emptyList() :
                items.stream().map(mapper).toList();

        return PageResponse.<R>builder()
                .items(mappedItems)
                .page(page)
                .size(size)
                .total(total)
                .build();
    }

    public <R> PageResponse<R> mapList(Function<List<T>, List<R>> mapper) {
        if (items == null) {
            return empty(page, size);
        }
        List<R> mappedItems = mapper.apply(items);
        return PageResponse.<R>builder()
                .items(mappedItems)
                .page(page)
                .size(size)
                .total(total)
                .build();
    }

    /* =====================
     *  Integration helpers
     * ===================== */

    /**
     * Dễ dàng chuyển sang BaseResponse<PageResponse<T>> khi trả API.
     */
    public BaseResponse<PageResponse<T>> toBaseResponse() {
        return BaseResponse.success(this);
    }
}
