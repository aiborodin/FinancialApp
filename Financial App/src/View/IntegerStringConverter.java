package View;

import javafx.util.StringConverter;

class IntegerStringConverter extends StringConverter<Number> {

    @Override
    public String toString(Number value) {
        if (value == null) {
            return "";
        }

        return Integer.toString((int)value);
    }

    @Override
    public Integer fromString(String value) {
        if (value == null) {
            return null;
        }

        value = value.trim();

        if (value.length() < 1) {
            return null;
        }

        Integer result = null;

        try {
            result = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }
}