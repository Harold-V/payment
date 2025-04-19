package tech.xirius.payment.infrastructure.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PsePaymentRequest {

    private String userId;
    private String referenceCode;
    private String description;
    private BigDecimal amount;

    private String fullName;
    private String email;
    private String contactPhone;
    private String dniNumber;
    private String dniType;

    private String street1;
    private String city;
    private String state;
    private String country;
    private String postalCode;

    private String bankCode;
    private String userType;

    private String deviceSessionId;
    private String ipAddress;
    private String cookie;
    private String userAgent;
}
