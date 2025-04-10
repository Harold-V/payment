package tech.xirius.payment.infrastructure.persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Converter(autoApply = true)
public class ZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, OffsetDateTime> {

    @Override
    public OffsetDateTime convertToDatabaseColumn(ZonedDateTime attribute) {
        return attribute != null ? attribute.toOffsetDateTime() : null;
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(OffsetDateTime dbData) {
        return dbData != null ? dbData.atZoneSameInstant(ZoneId.systemDefault()) : null;
    }
}
