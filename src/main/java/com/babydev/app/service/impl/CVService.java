package com.babydev.app.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.babydev.app.domain.dto.UserInfoDTO;
import com.babydev.app.domain.entity.CurriculumVitae;
import com.babydev.app.domain.entity.Degree;
import com.babydev.app.domain.entity.DevelopmentSkill;
import com.babydev.app.domain.entity.Education;
import com.babydev.app.domain.entity.Experience;
import com.babydev.app.domain.entity.ExperienceRating;
import com.babydev.app.domain.entity.Skill;
import com.babydev.app.domain.entity.User;
import com.babydev.app.exception.NotAuthorizedException;
import com.babydev.app.helper.Cstl;
import com.babydev.app.helper.Permissions;
import com.babydev.app.repository.CVRepository;

import jakarta.transaction.Transactional;

@Service
public class CVService {

    @Autowired
    private CVRepository cvRepository;

    @Autowired
    private FileStorageService fileStorageService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private TemplateEngine templateEngine;
    
    @Autowired
    private QualificationsService qualificationsService;

    @Transactional
    public CurriculumVitae storeCV(String token, MultipartFile file, String email) throws Exception {
    	User user = userService.getUserFromToken(token);
		final boolean isStandardUser = Permissions.isStandard(user);
		if (!user.getEmail().equals(email) || !isStandardUser) {
			throw new NotAuthorizedException();
		}
		
        String fileName = fileStorageService.storeFile(file);

        CurriculumVitae cv = CurriculumVitae.builder()
        		.id(user.getUserId())
        		.user(user)
        		.fileName(fileName)
        		.build();
        
        user.setHasCv(true);
        userService.save(user);
        
        return cvRepository.save(cv);
    }
    
    @Transactional
    public CurriculumVitae generateCV(String token, String email) throws Exception {
    	User user = userService.getUserFromToken(token);
		final boolean isStandardUser = Permissions.isStandard(user);
		if (!user.getEmail().equals(email) || !isStandardUser) {
			throw new NotAuthorizedException();
		}
        
        Context context = new Context();
        UserInfoDTO userInfo = userService.getUserInfo(user);
        context.setVariable("base64Image", Base64.getEncoder().encodeToString(userInfo.getImageData()));
        context.setVariable("firstName", userInfo.getFirstName());
        context.setVariable("lastName", userInfo.getLastName());
        context.setVariable("email", userInfo.getEmail());
        context.setVariable("phoneNumber", userInfo.getPhoneNumber());
        context.setVariable("experience", userInfo.getExperience());
        context.setVariable("education", userInfo.getEducation());
        context.setVariable("skills", userInfo.getSkill());
        
        String processedHtml = templateEngine.process("resume-template", context);
        
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(processedHtml);
        renderer.layout();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        renderer.createPDF(outputStream);
        
		
        String fileStorageLocation = "files/cv";
        String fileName = UUID.randomUUID().toString() + "-" + userInfo.getFirstName() + "_" + userInfo.getLastName() + ".pdf";
        Path targetLocation = Paths.get(System.getProperty("user.dir"), fileStorageLocation).resolve(fileName);

        // Copy the content from the outputStream to the targetLocation
        Files.copy(new ByteArrayInputStream(outputStream.toByteArray()), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        outputStream.close();
        CurriculumVitae cv = CurriculumVitae.builder()
        		.id(user.getUserId())
        		.user(user)
        		.fileName(fileName)
        		.build();
        
        user.setHasCv(true);
        userService.save(user);
        
        return cvRepository.save(cv);
    }
    
    @Transactional
    public String extractDataFromEuropassPdf(String token, String email, MultipartFile file) throws NotAuthorizedException {
    	User user = userService.getUserFromToken(token);
		final boolean isStandardUser = Permissions.isStandard(user);
		if (!user.getEmail().equals(email) || !isStandardUser) {
			throw new NotAuthorizedException();
		}
		
    	StringBuilder cvText = new StringBuilder();
    	
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper() {
                @Override
                protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
                    for (TextPosition text : textPositions) {
                        float lineHeight = text.getHeight();

                        if (lineHeight > 0) {
                            cvText.append(text.getUnicode());
                        }
                    }
                }
            };

            stripper.setSortByPosition(true);
            stripper.setStartPage(0);
            stripper.setEndPage(document.getNumberOfPages());

            stripper.getText(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] cvWords = cvText.toString().split("\\s+");
        
        // take only the relevant data to fill out education, experience and skills
        List<String> relevantData = new ArrayList<String>();
        List<String> relevantDelimiters = new ArrayList<String>();
        
		for (String word : cvWords) {
			if (!Cstl.europassTemplate.contains(word)) {
				relevantData.add(word);
				if (!relevantDelimiters.contains(word)
						&& (word.equals("EXPERIENCE") || word.equals("TRAINING") || word.equals("Communication")
								|| word.equals("Organisational") || word.equals("Job-related"))) {
					relevantDelimiters.add(word);
				}
			}
		}
        
        List<List<String>> sections = new ArrayList<List<String>>();
        int beginDelimiter = 0;
        int endDelimiter = 0;
        for (int i = 0; i < relevantDelimiters.size(); i++) {
        	if (i == 0) {
        		beginDelimiter = relevantData.indexOf(relevantDelimiters.get(i));
        		endDelimiter = relevantData.indexOf(relevantDelimiters.get(i + 1));
        	} else if (i != relevantDelimiters.size() - 1) {
        		beginDelimiter = endDelimiter;
        		endDelimiter = relevantData.indexOf(relevantDelimiters.get(i + 1));
        	} else {
        		beginDelimiter = endDelimiter;
        		endDelimiter = relevantData.indexOf("SELF-ASSESSMENT");
        	}
        	sections.add(relevantData.subList(beginDelimiter, endDelimiter));
        }
        
        String stringToBeEvaluated;
        
		for (List<String> section : sections) {
			switch (section.get(0)) {
			case "EXPERIENCE":
				boolean hasPosition = false;
				boolean hasTitle = false;
				boolean hasCompanyName = false;
				boolean hasDate = false;
				boolean isUserReadyToBeSaved = false;
				StringBuilder newCompanyName = new StringBuilder();
				StringBuilder newTitle = new StringBuilder();
				Experience newExperience = new Experience();
				String[] dateParts = new String[2];
				for (int i = 1; i < section.size(); i++) {
					stringToBeEvaluated = section.get(i);
					// date case
					if (!hasDate && stringToBeEvaluated.matches(".*\\d+.*")) {
						hasPosition = false;
						hasTitle = false;
						hasCompanyName = false;
						newCompanyName = new StringBuilder();
						newTitle = new StringBuilder();
						isUserReadyToBeSaved = true;
						dateParts = stringToBeEvaluated.split("-");
						LocalDate localDate;
						String[] daysMonthsYears;
						if (dateParts[0].length() == 4) {
							localDate = LocalDate.of(Integer.parseInt(dateParts[0]), 1, 1);
						} else if (dateParts[0].length() == 7) {
							daysMonthsYears = dateParts[0].split("\\.");
							localDate = LocalDate.of(Integer.parseInt(daysMonthsYears[1]), 
									Integer.parseInt(daysMonthsYears[0]), 1);
						} else {
							daysMonthsYears = dateParts[0].split("\\.");
							localDate = LocalDate.of(Integer.parseInt(daysMonthsYears[2]), 
									Integer.parseInt(daysMonthsYears[1]), 
									Integer.parseInt(daysMonthsYears[0]));	
						}
				        
				        newExperience.setDateFrom(localDate);
				        if (dateParts.length < 2 || !dateParts[1].matches(".*\\d+.*")) {
				        	newExperience.setDateTo(null);
				        } else {
				        	if (dateParts[1].length() == 4) {
								localDate = LocalDate.of(Integer.parseInt(dateParts[1]), 1, 1);
							} else if (dateParts[1].length() == 7) {
								daysMonthsYears = dateParts[1].split("\\.");
								localDate = LocalDate.of(Integer.parseInt(daysMonthsYears[1]), 
										Integer.parseInt(daysMonthsYears[0]), 1);
							} else {
								daysMonthsYears = dateParts[1].split("\\.");
								localDate = LocalDate.of(Integer.parseInt(daysMonthsYears[2]), 
										Integer.parseInt(daysMonthsYears[1]), 
										Integer.parseInt(daysMonthsYears[0]));	
							}
				        	newExperience.setDateTo(localDate);
				        }
					} else {
						if (!hasPosition) {
						for (String jobPosition : Cstl.jobPositions) {
					        if (stringToBeEvaluated.equalsIgnoreCase(jobPosition)) {
					            newExperience.setPosition(jobPosition);
					            hasPosition = true;
					            break;
					        }
					    }
						} else {
							hasTitle = false;
							if (!hasCompanyName) {
							for (String jobTitle : Cstl.jobTitles) {
								if (stringToBeEvaluated.equalsIgnoreCase(jobTitle)) {
									hasTitle = true;
									newTitle.append(jobTitle + " ");
								}
							}
							}
							if (stringToBeEvaluated.length() == 1 
									&& stringToBeEvaluated.charAt(0) == '▪' 
									&& isUserReadyToBeSaved) {
								newExperience.setCompanyName(newCompanyName.toString());
								newExperience.setTitle(newTitle.toString());
								newExperience.setUser(user);
								qualificationsService.saveExperience(newExperience);
								
								user.getExperience().add(newExperience);
								newExperience = new Experience();
								isUserReadyToBeSaved = false;
								hasDate = false;
								
							} else if (!hasTitle) {
								hasCompanyName = true;
								newCompanyName.append(stringToBeEvaluated + " ");
							}
						}
						
					}
				}
				break;

			case "TRAINING":
				boolean hasDegree = false;
				boolean hasInstitution = false;
				hasDate = false;
				isUserReadyToBeSaved = false;
				StringBuilder newInstitution = new StringBuilder();
				StringBuilder newSubject = new StringBuilder();
				Education newEducation = new Education();
				dateParts = new String[2];
				for (int i = 1; i < section.size(); i++) {
					stringToBeEvaluated = section.get(i);
					// date case
					if (!hasDate && stringToBeEvaluated.matches(".*\\d+.*")) {
						if (!newSubject.isEmpty()) {
							newEducation.setInstitution(newInstitution.toString());
							newEducation.setSubject(newSubject.toString());
							newEducation.setUser(user);
							qualificationsService.saveEducation(newEducation);
							
							user.getEducation().add(newEducation);
							newEducation = new Education();
						
							hasDate = false;
							hasDegree = false;
							hasInstitution = false;
							newSubject = new StringBuilder();
							newInstitution = new StringBuilder();
							
						}
						try {
							dateParts = stringToBeEvaluated.split("-");
						LocalDate localDate = null;
						String[] daysMonthsYears;
						if (dateParts[0].length() == 4) {
							localDate = LocalDate.of(Integer.parseInt(dateParts[0]), 1, 1);
						} else if (dateParts[0].length() == 7) {
							daysMonthsYears = dateParts[0].split("\\.");
							localDate = LocalDate.of(Integer.parseInt(daysMonthsYears[1]), 
									Integer.parseInt(daysMonthsYears[0]), 1);
						} else if (dateParts[0].length() == 10) {
							daysMonthsYears = dateParts[0].split("\\.");
							localDate = LocalDate.of(Integer.parseInt(daysMonthsYears[2]), 
									Integer.parseInt(daysMonthsYears[1]), 
									Integer.parseInt(daysMonthsYears[0]));	
						}
				        
						if (localDate == null) {
							throw new Exception("invalid date");
						}
						
				        newEducation.setDateFrom(localDate);
				        if (dateParts.length < 2 || !dateParts[1].matches(".*\\d+.*")) {
				        	newEducation.setDateTo(null);
				        } else {
				        	if (dateParts[1].length() == 4) {
								localDate = LocalDate.of(Integer.parseInt(dateParts[1]), 1, 1);
							} else if (dateParts[1].length() == 7) {
								daysMonthsYears = dateParts[1].split("\\.");
								localDate = LocalDate.of(Integer.parseInt(daysMonthsYears[1]), 
										Integer.parseInt(daysMonthsYears[0]), 1);
							} else if (dateParts[1].length() == 10) {
								daysMonthsYears = dateParts[1].split("\\.");
								localDate = LocalDate.of(Integer.parseInt(daysMonthsYears[2]), 
										Integer.parseInt(daysMonthsYears[1]), 
										Integer.parseInt(daysMonthsYears[0]));	
							}
				        	
				        	if (localDate == null) {
								throw new Exception("invalid date");
							}
				        	
				        	newEducation.setDateTo(localDate);
				        }
						} catch (Exception e){
							e.printStackTrace();
							if (!hasInstitution) {
								newInstitution.append(stringToBeEvaluated + " ");
							} else {
								newSubject.append(stringToBeEvaluated + " ");
							}
								
						}
						
					
					} else {
						if (!hasDegree) {
						for (Degree degree : Degree.values()) {
							String degreeValue = degree.toString();
					        if (stringToBeEvaluated.equalsIgnoreCase(degreeValue) || 
					        		degreeValue.toLowerCase().contains(stringToBeEvaluated)) {
					            newEducation.setDegree(degree);
					            hasDegree = false;
					            break;
					        }
					        hasDegree = true;
					    }
						}
						if (hasDegree && !hasInstitution) {
							if (stringToBeEvaluated.length() == 1 
									&& stringToBeEvaluated.charAt(0) == '▪') {
								hasInstitution = true;
								continue;
							}
							newInstitution.append(stringToBeEvaluated + " ");
						} else if (hasInstitution) {
							newSubject.append(stringToBeEvaluated + " ");
						} 
						
					}
				}
				if (newEducation.getDegree() != null) {
					newEducation.setInstitution(newInstitution.toString());
					newEducation.setSubject(newSubject.toString());
					newEducation.setUser(user);
					qualificationsService.saveEducation(newEducation);
					
					user.getEducation().add(newEducation);
					newEducation = new Education();
				}
				break;

			case "Communication":
				insertSkillsBasedOnCvData(section, user);
				break;
			case "Organisational":
				insertSkillsBasedOnCvData(section, user);
				break;
			case "Job-related":
				insertSkillsBasedOnCvData(section, user);
				break;
			}
		}
		userService.save(user);
		return "";
	}
    
    private User insertSkillsBasedOnCvData(List<String> skillValues, User user) {
    	String skillToBeEvaluated;
        String skillValueFromEnum;
    	for (int i = 1; i < skillValues.size(); i++) {
			skillToBeEvaluated = skillValues.get(i);
			for (DevelopmentSkill skill : DevelopmentSkill.values()) {
				skillValueFromEnum = skill.getDisplayName();
				if (skillToBeEvaluated.equalsIgnoreCase(skillValueFromEnum) ||
						skillToBeEvaluated.contains(skillValueFromEnum) ||
						skillValueFromEnum.contains(skillToBeEvaluated)) {
					Skill newSkill = Skill.builder()
							.skillName(skill)
							.skillExperience(ExperienceRating.valueOf("FIVE"))
							.user(user)
							.build();
					
					user.getSkills().add(newSkill);
				}
			}
		}
    	return user;
    }

    public Optional<CurriculumVitae> getCVById(Long id) {
        return cvRepository.findById(id);
    }

    @Transactional
    public void deleteCV(String token, String email) throws NotAuthorizedException {
    	
    	User user = userService.getUserFromToken(token);
    	final boolean isAdmin = Permissions.isAdmin(user);
    	if (!user.getEmail().equals(email) && !isAdmin) {
			throw new NotAuthorizedException();
		}
    	
    	User userToBeUpdated = userService.getUserByEmail(email);
		CurriculumVitae cv = getCVById(userToBeUpdated.getUserId()).get();
		
		userToBeUpdated.setHasCv(false);
		
		userService.save(userToBeUpdated);
		cvRepository.delete(cv);
		
		try {
			fileStorageService.deleteFile(cv.getFileName());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	public CurriculumVitae getCVByUserEmail(String token, String email) throws NotAuthorizedException {
		User user = userService.getUserFromToken(token);
		final boolean isStandardUser = Permissions.isStandard(user);
		if (!user.getEmail().equals(email) && isStandardUser) {
			throw new NotAuthorizedException();
		}
		return getCVById(userService.getUserByEmail(email).getUserId()).get();
	}
}

