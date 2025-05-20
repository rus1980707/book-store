package spring.bookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanWrapperImpl;

@Getter
@Setter
public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;
    private String field;
    private String fieldMatch;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.firstFieldName = constraintAnnotation.first();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object field = new BeanWrapperImpl(value).getPropertyValue(this.field);
        Object fieldMatch = new BeanWrapperImpl(value).getPropertyValue(this.fieldMatch);
        return Objects.equals(field, fieldMatch);
    }
}
