package edu.exam_online.exam_online_system.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode{
    // ===== USER =====
    REFRESH_TOKEN_FAILSE(400, "Refresh token failed", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_INVALID(400, "Refresh token invalid", HttpStatus.BAD_REQUEST),
    USER_EXISTED(400, "User already exists", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(400, "User not found", HttpStatus.NOT_FOUND),
    WRONG_PASSWORD_OR_USERID(400, "Wrong password or user ID", HttpStatus.BAD_REQUEST),
    NOT_AUTHENTICATION(401, "Not authenticated", HttpStatus.UNAUTHORIZED),
    NOT_AUTHORIZATION(403, "Access denied", HttpStatus.FORBIDDEN),
    WRONG_VERIFICATION_CODE(400, "Wrong verification code", HttpStatus.BAD_REQUEST),
    VERIFICATION_CODE_EXPIRED(400, "Verification code expired", HttpStatus.BAD_REQUEST),
    ACCOUNT_IS_INACTIVE(400, "Account is inactive", HttpStatus.BAD_REQUEST),
    ACCOUNT_IS_LOCKED(400, "Account is locked", HttpStatus.BAD_REQUEST),
    CHANGE_PASSWORD_FAILED(400, "Change password failed", HttpStatus.BAD_REQUEST),

    // ===== ROLE =====
    ROLE_NOT_FOUND(404, "Role not found", HttpStatus.NOT_FOUND),

    // ===== PRODUCT =====
    PRODUCT_NOT_FOUND(404, "Product not found", HttpStatus.NOT_FOUND),
    PRODUCT_OUT_OF_STOCK(409, "Product out of stock", HttpStatus.CONFLICT),
    PRODUCT_EXISTED(400, "Product already exists", HttpStatus.BAD_REQUEST),

    // ===== PRODUCT =====
    BRANCH_CANNOT_DELETE(404, "Branch can't delete", HttpStatus.NOT_FOUND),
    BRANCH_NOT_FOUND(404, "Branch not found", HttpStatus.NOT_FOUND),
    BRANCH_OUT_OF_STOCK(409, "Branch out of stock", HttpStatus.CONFLICT),
    BRANCH_EXISTED(400, "Branch already exists", HttpStatus.BAD_REQUEST),

    // ===== CATEGORY =====
    CATEGORY_CANNOT_DELETE(404, "Category cannot delete", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND(404, "Category not found", HttpStatus.NOT_FOUND),
    CATEGORY_OUT_OF_STOCK(409, "Category out of stock", HttpStatus.CONFLICT),
    CATEGORY_EXISTED(400, "Category already exists", HttpStatus.BAD_REQUEST),

    // ===== ORDER =====
    ORDER_NOT_FOUND(404, "Order not found", HttpStatus.NOT_FOUND),
    ORDER_INVALID_STATUS(400, "Order status invalid for this operation", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_PROCESSED(409, "Order already processed", HttpStatus.CONFLICT),

    // ===== CART =====
    CART_EMPTY(400, "Shopping cart is empty", HttpStatus.BAD_REQUEST),
    CART_ITEM_NOT_FOUND(404, "Cart item not found", HttpStatus.NOT_FOUND),

    // ===== PAYMENT =====
    PAYMENT_FAILED(500, "Payment processing failed", HttpStatus.INTERNAL_SERVER_ERROR),
    PAYMENT_METHOD_INVALID(400, "Invalid payment method", HttpStatus.BAD_REQUEST),

    // ===== INVENTORY =====
    INSUFFICIENT_STOCK(409, "Not enough stock available", HttpStatus.CONFLICT),

    // ===== VALIDATION =====
    INVALID_INPUT(400, "Invalid input data", HttpStatus.BAD_REQUEST),
    MISSING_REQUIRED_FIELD(400, "Missing required field", HttpStatus.BAD_REQUEST),

    // ===== SYSTEM =====
    INTERNAL_SERVER_ERROR(500, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    SERVICE_UNAVAILABLE(503, "Service temporarily unavailable", HttpStatus.SERVICE_UNAVAILABLE),

    // ===== GENERAL =====
    CONFLICT(409, "Conflict occurred", HttpStatus.CONFLICT),
    RESOURCE_ALREADY_EXISTS(409, "Resource already exists", HttpStatus.CONFLICT),
    RESOURCE_NOT_FOUND(404, "Requested resource not found", HttpStatus.NOT_FOUND),

    // ====== EXAM ========
    EXAM_NOT_FOUND(400, "Exam not found", HttpStatus.BAD_REQUEST),
    QUESTION_NOT_FOUND(400, "Question not found", HttpStatus.NOT_FOUND);

    @Getter
    int status;
    String message;
    HttpStatus httpStatus;

    ErrorCode(int status, String message,HttpStatus httpStatus){
        this.status = status;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
