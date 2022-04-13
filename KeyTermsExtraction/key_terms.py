from nltk.tokenize import word_tokenize
from lxml import etree
from sklearn.feature_extraction.text import TfidfVectorizer

import nltk
import string

nltk.download('averaged_perceptron_tagger')
lemmatizer = nltk.WordNetLemmatizer()
badwords = set(nltk.corpus.stopwords.words('english') + list(string.punctuation) + ['ha', 'wa', 'u', 'a'])
root = etree.parse("news.xml").getroot()
dataset = []

for news in root[0]:
    tokens = [lemmatizer.lemmatize(x) for x in word_tokenize(news[1].text.lower())]
    tokens2 = [x for x in tokens if nltk.pos_tag([x])[0][1] == "NN" and x not in badwords]

    dataset.append(' '.join(tokens2))
vectorizer = TfidfVectorizer()
tfidf_matrix = vectorizer.fit_transform(dataset)
terms = vectorizer.get_feature_names()
k = 0
for news in root[0]:
    print(news[0].text + ':')

    ranks = tfidf_matrix[k].toarray()[0]
    words = [x for x in [(i, ranks[i]) for i in range(len(ranks))] if x[1] > 0]
    words.sort(key=lambda x: (x[1], terms[x[0]]), reverse=True)

    print(*[terms[words[i][0]] for i in range(5)], end="\n" * 2)
    k += 1
