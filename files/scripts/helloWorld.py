import sys
import spacy
from spacy.lang.ro.examples import sentences

nlp = spacy.load("ro_core_news_sm")

input_text = sys.argv[1]

doc = nlp(input_text)

for ent in doc.ents:
    if ent.label_ == "GPE":
        print(ent.text)