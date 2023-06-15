package com.babydev.app.helper;

import java.util.Arrays;
import java.util.List;

/**
 * Class that contains all constants for bd. (Constants List)
 * 
 * @author pmarila
 */
public class Cstl {

	public static short minimumPasswordLength = 8;

	public static List<Character> recipientAllowedCharacters = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
			't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '!', '#', '$', '%', '&', '\'', '*', '+', '-',
			'/', '=', '?', '^', '_', '`', '{', '|', '}', '.');

	public static List<Character> domainAllowedCharacters = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
			'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '-');

	public static String[] allowedEndpoints = { "/api/auth/**", "/api/users/recruiters/request", "api/users/favorite",
			// JobController
			"/api/jobs/all", "/api/jobs/search" };

	public static List<String> europassTemplate = Arrays.asList("Curriculum", "Vitae", "Replace", "with", "First",
			"name(s)", "Surname(s)", "PERSONAL", "INFORMATION", "Replace", "with", "First", "name(s)", "Surname(s)",
			"[All", "CV", "headings", "are", "optional.", "Remove", "any", "empty", "headings.", "Replace", "with",
			"house", "number,", "street", "name,", "city,", "postcode,", "country", "Replace", "with", "telephone",
			"number", "Replace", "with", "mobile", "number", "State", "e-mail", "address", "State", "personal",
			"website(s)", "Replace", "with", "type", "of", "IM", "service", "Replace", "with", "messaging",
			"account(s)", "Sex", "Enter", "sex", "|", "Date", "of", "birth", "dd/mm/yyyy", "|", "Nationality", "Enter",
			"nationality/-ies", "JOB", "APPLIED", "FOR", "POSITION", "Replace", "with", "job", "applied", "for", "/",
			"position", "/", "preferred", "job", "/", "studies", "applied", "PREFERRED", "JOB", "STUDIES", "APPLIED",
			"FOR", "for", "/", "personal", "statement", "(delete", "non", "relevant", "headings", "in", "left",
			"column)", "PERSONAL", "STATEMENT", "[Add", "separate", "entries", "for", "each", "experience.", "Start",
			"from", "the", "most", "recent.]", "Replace", "with", "dates", "(from", "-", "to)", "Replace", "with",
			"occupation", "or", "position", "held", "Replace", "with", "employer’s", "name", "and", "locality", "(if",
			"relevant,", "full", "address", "and", "website)", "Replace", "with", "main", "activities", "and",
			"responsibilities", "Business", "or", "sector", "Replace", "with", "type", "of", "business", "or", "sector",
			"[Add", "separate", "entries", "for", "each", "course.", "Start", "from", "the", "most", "recent.]",
			"Replace", "with", "dates", "(from", "-", "to)", "Replace", "with", "qualification", "awarded", "Replace",
			"with", "EQF", "(or", "other)", "level", "if", "relevant", "Replace", "with", "education", "or", "training",
			"organisation’s", "name", "and", "locality", "(if", "relevant,", "country)", "Replace", "with", "a", "list",
			"of", "principal", "subjects", "covered", "or", "skills", "acquired", "PERSONAL", "SKILLS", "[Remove",
			"any", "headings", "left", "empty.]", "Mother", "tongue(s)", "Replace", "with", "mother", "tongue(s)",
			"Other", "language(s)", "UNDERSTANDING", "SPEAKING", "WRITING", "Listening", "Reading", "Spoken",
			"interaction", "Spoken", "production", "Replace", "with", "language", "Enter", "level", "Enter", "level",
			"Enter", "level", "Enter", "level", "Enter", "level", "Replace", "with", "name", "of", "language",
			"certificate.", "Enter", "level", "if", "known.", "Replace", "with", "language", "Enter", "level", "Enter",
			"level", "Enter", "level", "Enter", "level", "Enter", "level", "Replace", "with", "name", "of", "language",
			"certificate.", "Enter", "level", "if", "known.", "Levels:", "A1/A2:", "Basic", "user", "-", "B1/B2:",
			"Independent", "user", "-", "C1/C2", "Proficient", "user", "Common", "European", "Framework", "of",
			"Reference", "for", "Languages", "Replace", "with", "your", "communication", "skills.", "Specify", "in",
			"what", "context", "they", "were", "acquired.", "Example:", "good", "communication", "skills", "gained",
			"through", "my", "experience", "as", "sales", "manager", "Replace", "with", "your", "organisational", "/",
			"managerial", "skills.", "Specify", "in", "what", "context", "they", "were", "acquired.", "Example:",
			"leadership", "(currently", "responsible", "for", "a", "team", "of", "10", "people)", "©", "European",
			"Union,", "2002-2015", "|", "europass.cedefop.europa.eu", "Page", "1", "/", "3", "Curriculum", "Vitae",
			"Replace", "with", "First", "name(s)", "Surname(s)", "Replace", "with", "any", "job-related", "skills",
			"not", "listed", "elsewhere.", "Specify", "in", "what", "context", "they", "were", "acquired.", "Example:",
			"good", "command", "of", "quality", "control", "processes", "(currently", "responsible", "for", "quality",
			"audit)", "Digital", "competence", "EDUCATION", "AND");

	public static String[] jobPositions = { "Junior", "Senior", "Associate", "Lead", "Principal", "Entry-level",
			"Mid-level", "Experienced", "Certified", "Freelance", "Part-time", "Full-time", "Remote", "Contract",
			"Intern", "Manager", "Coordinator", "Specialist", "Analyst", "Engineer", "Developer", "Designer",
			"Consultant", "Assistant", "Administrator", "Executive" };

	public static String[] jobTitles = { "Salesperson", "Technician", "Supervisor", "Operator", 
			"Developer", "UI/UX", "systems", "DevOps", "Data", "Network",
			"Quality", "Software", "Embedded", "Machine", "Engineer" };

}
