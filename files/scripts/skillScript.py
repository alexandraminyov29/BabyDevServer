import sys
from flair.data import Sentence
from flair.models import SequenceTagger

# Load tagger
tagger = SequenceTagger.load("flair/ner-english")

# Make sentence
sentence = Sentence(sys.argv[1])

# Predict NER tags
tagger.predict(sentence)

# Get predicted entities with labels 'MISC' and 'ORG'
entities = [entity.text for entity in sentence.get_spans('ner') if entity.tag in ['MISC', 'ORG']]

# print the entities
for entity in entities:
    print(entity)
