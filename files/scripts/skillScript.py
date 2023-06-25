import sys
from flair.data import Sentence
from flair.models import SequenceTagger

# Load the pre-trained model in English
tagger = SequenceTagger.load("flair/ner-english")

# Make sentence from the first argument from the command line
sentence = Sentence(sys.argv[1])

# Predict NER tags using the model
tagger.predict(sentence)

# Get predicted entities with labels 'MISC' and 'ORG'
entities = [entity.text for entity in sentence.get_spans('ner') if entity.tag in ['MISC', 'ORG']]

# print the entities
for entity in entities:
    print(entity)
