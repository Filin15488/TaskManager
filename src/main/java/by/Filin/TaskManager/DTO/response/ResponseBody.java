package by.Filin.TaskManager.DTO.response;


import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
public class ResponseBody {
    String message;
    private LocalDateTime timestamp;

    public ResponseBody(String message, LocalDateTime timestamp) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
