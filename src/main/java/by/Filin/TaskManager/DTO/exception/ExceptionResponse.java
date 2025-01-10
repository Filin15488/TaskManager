package by.Filin.TaskManager.DTO.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
//@AllArgsConstructor
@Builder
public class ExceptionResponse {
    @Schema(description = "Сообщение о ошибке", example = "Not found")
    String message;
//    @Schema(description = "Дата и время возникновения ошибки", example = "Important", required = true)
    private LocalDateTime timestamp;

    public ExceptionResponse(String message, LocalDateTime timestamp) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
