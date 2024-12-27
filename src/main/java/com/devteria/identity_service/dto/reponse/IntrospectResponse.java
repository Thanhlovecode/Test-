package com.devteria.identity_service.dto.reponse;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntrospectResponse {
    boolean valid;
}
