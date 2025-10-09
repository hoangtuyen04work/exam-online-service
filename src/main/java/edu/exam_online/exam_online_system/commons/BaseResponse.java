package edu.exam_online.exam_online_system.commons;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@JsonIgnoreProperties({"dataOrThrow"})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    private int code;       // 0 = success, khác 0 = error
    private String message;
    private T data;

    /* =====================
     *  Convenience methods
     * ===================== */

    public boolean isSuccess() {
        return code == 0;
    }

    public T getDataOrThrow() {
        if (!isSuccess()) {
            throw new IllegalStateException("Response not success: " + message);
        }
        return data;
    }

    public T getDataOrDefault(T defaultValue) {
        return isSuccess() && data != null ? data : defaultValue;
    }

    /* =====================
     *  Static factory methods
     * ===================== */

    public static <T> BaseResponse<T> success(T data) {
        return BaseResponse.<T>builder()
                .code(0)
                .message("success")
                .data(data)
                .build();
    }

    public static <T> BaseResponse<T> success() {
        return success(null);
    }

    public static <T> BaseResponse<T> error(int code, String message) {
        return BaseResponse.<T>builder()
                .code(code)
                .message(message)
                .build();
    }

    public static <T> BaseResponse<T> of(int code, String message, T data) {
        return BaseResponse.<T>builder()
                .code(code)
                .message(message)
                .data(data)
                .build();
    }

    /* =====================
     *  Helpers for Page/List
     * ===================== */

    public static <T> BaseResponse<List<T>> fromList(List<T> list) {
        return success(list != null ? list : Collections.emptyList());
    }

    public static <T> BaseResponse<PageResponse<T>> fromPage(PageResponse<T> page) {
        return success(page);
    }

    /**
     * Map response data sang kiểu khác (hay dùng khi convert DTO).
     */
    public <R> BaseResponse<R> map(Function<T, R> mapper) {
        if (!isSuccess() || data == null) {
            return BaseResponse.of(code, message, null);
        }
        return BaseResponse.success(mapper.apply(data));
    }
}
