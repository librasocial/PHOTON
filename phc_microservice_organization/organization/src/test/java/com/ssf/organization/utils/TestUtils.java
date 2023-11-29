package com.ssf.organization.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.xmlunit.builder.Input;


@SpringBootTest
public class TestUtils {

	//@Test
//	public void testJoltTransform() {
//	   String input = "{\n  \"rating\": {\n    \"primary\": {\n      \"value\": 3\n    },\n    \"quality\": {\n      \"value\": 3\n    }\n  }\n}";
//	   String output = "{\r\n"
//	   		+ "  \"Rating\" : 3,\r\n"
//	   		+ "  \"SecondaryRatings\" : {\r\n"
//	   		+ "    \"quality\" : {\r\n"
//	   		+ "      \"Id\" : \"quality\",\r\n"
//	   		+ "      \"Value\" : 3,\r\n"
//	   		+ "      \"Range\" : 5\r\n"
//	   		+ "    }\r\n"
//	   		+ "  },\r\n"
//	   		+ "  \"Range\" : 5\r\n"
//	   		+ "}";
//	   String result = Utils.joltTransform(input, "test");
//		assertEquals(output, result);
//	}
}
