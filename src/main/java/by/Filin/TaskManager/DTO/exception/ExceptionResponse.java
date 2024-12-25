package by.Filin.TaskManager.DTO.exception;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
//@AllArgsConstructor
@Builder
public class ExceptionResponse {
    String message;
    private LocalDateTime timestamp;

    public ExceptionResponse(String message, LocalDateTime timestamp) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
