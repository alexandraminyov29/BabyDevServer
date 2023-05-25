package com.babydev.app.helper;

import java.util.Arrays;
import java.util.List;

/**
 * Class that contains all constants for bd.
 * 	(Constants List) 
 * 
 * @author pmarila
 */
public class Cstl {

	
	public static short minimumPasswordLength = 8;
	
	public static List<Character> recipientAllowedCharacters = Arrays.asList(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '!', '#', '$', '%', '&', '\'', '*', '+', '-', '/', '=', '?', '^', '_', '`',
            '{', '|', '}', '.'
        );
	
    public static List<Character> domainAllowedCharacters = Arrays.asList(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '-'
        );
    
		public static String[] allowedEndpoints = { "/api/auth/**",
				"api/users/favorite",
				// JobController
				"/api/jobs/all",
				"/api/jobs/search"
		};
}
