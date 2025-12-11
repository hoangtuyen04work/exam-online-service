package edu.exam_online.exam_online_system.dto.email;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sender {
    private String name;
    private String email;
}