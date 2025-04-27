package tech.xirius.payment.infrastructure.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO para solicitudes de pagos PSE a través de PayU.
 */
@Getter
@Setter
public class PsePaymentRequest {

    @NotNull(message = "El userId no puede ser nulo")
    private String userId;

    private String referenceCode; // Opcional, se genera si no se envía

    @NotBlank(message = "La descripción no puede estar vacía")
    private String description;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser positivo")
    private BigDecimal amount;

    @NotBlank(message = "El nombre completo es obligatorio")
    private String fullName;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;

    @NotBlank(message = "El teléfono de contacto es obligatorio")
    private String contactPhone;

    @NotBlank(message = "El número de documento es obligatorio")
    private String dniNumber;

    @NotBlank(message = "El tipo de documento es obligatorio")
    private String dniType;

    @NotBlank(message = "La dirección es obligatoria")
    private String street1;

    @NotBlank(message = "La ciudad es obligatoria")
    private String city;

    @NotBlank(message = "El departamento o estado es obligatorio")
    private String state;

    @NotBlank(message = "El país es obligatorio")
    private String country;

    @NotBlank(message = "El código postal es obligatorio")
    private String postalCode;

    @NotBlank(message = "El código del banco es obligatorio")
    private String bankCode;

    @NotBlank(message = "El tipo de usuario es obligatorio")
    private String userType;

    @NotBlank(message = "El deviceSessionId es obligatorio")
    private String deviceSessionId;

    @NotBlank(message = "La IP address es obligatoria")
    private String ipAddress;

    private String cookie;
    private String userAgent;
}
