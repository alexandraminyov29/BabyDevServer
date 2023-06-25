import sys
import spacy

# Load the pre-trained model in Romanian language
nlp = spacy.load("ro_core_news_sm")
# Assign to input_text the first argument from the command line
input_text = sys.argv[1]
# Text processing using the model
doc = nlp(input_text)
# Identify the entities
for ent in doc.ents:
# Check for geopolitical entity
    if ent.label_ == "GPE":
        print(ent.text)