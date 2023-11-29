package com.ssf.labtest.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constant {
	public static final List<String> unitsOfMeasurement = new ArrayList<>(
			Arrays.asList("Cells/cumm", "Grams (g)", "Grams per decilitre (g/dL)", "Grams per litre (g/L)",
					"International units per litre (IU/L)", "International units per millilitre (IU/mL)",
					"Micrograms (mcg)", "Micrograms per decilitre(mcg/dL)", "Milligrams per decilitre(mg/dL)"));
    public static final String XUSER_ID_HEADER = "x-user-id";
    public static final String COGNITO_USERNAME_CLAIM = "cognito:username";
    public static final String COLLECTION_NAME = "LabTest";

    public static enum TestType {
        MAINTEST("main test"),
        SUBTEST("sub test");
        public final String value;

        TestType(String value) {
            this.value = value;
        }
    }

    public static enum TestGroup {
        PATHOLOGY("Pathology"),
        BIOCHEMISTRY("Biochemistry"),
        MICROBIOLOGY("Microbiology"),
        SEROLOGY("Serology"),
        BACTERIOLOGY("Bacteriology"),
    	HEMATOLOGY("Hematology");
        public final String value;

        TestGroup(String value) {
            this.value = value;
        }
    }

    public static enum SampleType {
        URINE("urine"),
        BLOOD("blood"),
    	SPUTUM("sputum"),
    	STOOL("stool"),
    	WATER("water");
        public final String value;

        SampleType(String value) {
            this.value = value;
        }
    }

    public static enum TestMethod { 
        ELISA("ELISA"),
        SERUM("Serum"),
        CELLCOUNTER("Cell Counter"),
        IMMUNOASSAYS("Immunoassays"),
        MANUAL("Manual");
        public final String value;

        TestMethod(String value) {
            this.value = value;
        }
    }

    public static enum ResultType {
        SINGLE("single"),
        PROFILE("profile");
        public final String value;

        ResultType(String value) {
            this.value = value;
        }
    }

    public static enum Status {
        ACTIVE("active"),
        INACTIVE("inactive");
        public final String value;

        Status(String value) {
            this.value = value;
        }
    }

    public static enum Gender {
        MALE("male"),
        FEMALE("female"),
        BOTH("both"),
        OTHERS("others");
        public final String value;

        Gender(String value) {
            this.value = value;
        }
    }

    public static enum Period {
        YEAR("year"),
        MONTH("month"),
        DAY("day"),
        MIN("min");
        public final String value;

        Period(String value) {
            this.value = value;
        }
    }

    public static enum TurnAroundUnit {
        MINUTES("minutes"),
        HOURS("hours"),
        DAYS("days");
        public final String value;

        TurnAroundUnit(String value) {
            this.value = value;
        }
    }

    public static enum ResultFormat {
        NUMERICAL("numerical"),
        SINGLELINE("single line"),
        MULTILINE("multiline");
        public final String value;

        ResultFormat(String value) {
            this.value = value;
        }
    }

    public static enum ReferenceIndicators {
        EQUALS("="),
        GREATERTHAN(">"),
        GREATERTHANEQUALTO(">="),
        LESSERTHAN("<"),
        LESSERTHANEQUALTO("<="),
        BETWEEN("between");
        public final String value;

        ReferenceIndicators(String value) {
            this.value = value;
        }
    }
}
