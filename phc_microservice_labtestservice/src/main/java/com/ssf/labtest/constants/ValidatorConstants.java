package com.ssf.labtest.constants;

public class ValidatorConstants {
    public static final String NAME_EMPTY = "Name was blank";
    public static final String CODE_EMPTY = "Code was blank";
    public static final String ENUM_EMPTY = "Enum cannot be empty";
    public static final String OUTSOURCED_EMPTY = "Outsourced cannot be null or empty";

    public static final String TEST_TYPE_VALIDATION = "Invalid Enum value. Allowed values are [main test, sub test]";
    public static final String GROUP_VALIDATION = "Invalid Enum value. Allowed values are [Pathology, Biochemistry, Microbiology, Serology, Bacteriology, Hematology]";
    public static final String SAMPLE_TYPE_VALIDATION = "Invalid Enum value. Allowed values are [urine, blood, stool, water, sputum]";
    public static final String TEST_VALIDATION = "Invalid Enum value. Allowed values are [ELISA, SERUM, Cell Counter, Immunoassays, Manual]";
    public static final String RESULT_TYPE_VALIDATION = "Invalid Enum value. Allowed values are [single, profile]";
    public static final String STATUS_VALIDATION = "Invalid Enum value. Allowed values are [active, inactive]";
    public static final String GENDER_VALIDATION = "Invalid Enum value. Allowed values are [male, female, both, others]";
    public static final String PERIOD_VALIDATION = "Invalid Enum value. Allowed values are [year, month, day, min]";
    public static final String TURN_AROUND_UNIT_VALIDATION = "Invalid Enum value. Allowed values are [minutes, hours, days]";
    public static final String UNIT_OF_MEASUREMENT_EMPTY = "Unit of Measurement cannot be empty.";
    public static final String TURN_AROUND_UNIT_EMPTY = "turnAroundUnit cannot be empty when turnAroundTime is provided in the request.";
    public static final String RESULT_FORMAT_VALIDATION = "Invalid Enum value. Allowed values are [numerical, single line, multiline]";

    public static final String REQUEST_VALIDATION_FAILED = "Request Validation Failed";
    public static final String REFERENCE_INDICATOR_VALIDATION = "Invalid Enum value. Allowed values are [EQUALS, GREATERTHAN, GREATERTHANEQUALTO, LESSERTHAN, LESSERTHANEQUALTO, BETWEEN]";
}