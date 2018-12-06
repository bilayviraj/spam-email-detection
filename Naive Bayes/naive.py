from __future__ import print_function, division

import nltk
import collections
import os
import random

from nltk.metrics.scores import precision, recall, f_measure
from collections import Counter
from nltk import word_tokenize, WordNetLemmatizer
from nltk.corpus import stopwords
from nltk import NaiveBayesClassifier, classify
from nltk.probability import ELEProbDist, FreqDist
from collections import defaultdict

stoplist = stopwords.words('english')


def init_lists(folder):
    a_list = []
    file_list = os.listdir(folder)
    for a_file in file_list:
        f = open(folder + a_file, 'r')
        a_list.append(f.read())
    f.close()
    return a_list


def preprocess(sentence):
    lemmatizer = WordNetLemmatizer()
    return [lemmatizer.lemmatize(word.lower()) for word in word_tokenize(unicode(sentence, errors='ignore'))]





def get_features(text, setting):
    if setting == 'bow':
        return {word: count for word, count in Counter(preprocess(text)).items() if not word in stoplist}
    else:
        return {word: True for word in preprocess(text) if not word in stoplist}


def training(features, features1):
    train_size = int(len(features))
    test_size = int(len(features1))
    # initialise the training and test sets
    train_set, test_set = features[:train_size], features1[:test_size]
    print('Training set size = ' + str(len(train_set)) + ' emails')

    print('Test set size = ' + str(len(test_set)) + ' emails')
    # train the classifier
    classifier = NaiveBayesClassifier.train(train_set)

    return train_set, test_set, classifier


def train(labeled_featuresets, estimator=ELEProbDist):
    """
    :param labeled_featuresets: A list of classified featuresets,
        i.e., a list of tuples ``(featureset, label)``.
    """

    label_freqdist = FreqDist()
    feature_freqdist = defaultdict(FreqDist)
    feature_values = defaultdict(set)
    fnames = set()

    # Count up how many times each feature value occurred, given
    # the label and featurename.
    for featureset, label in labeled_featuresets:
        label_freqdist[label] += 1
        for fname, fval in featureset.items():
            # Increment freq(fval|label, fname)
            feature_freqdist[label, fname][fval] += 1
            # Record that fname can take the value fval.
            feature_values[fname].add(fval)
            # Keep a list of all feature names.
            fnames.add(fname)

    # If a feature didn't have a value given for an instance, then
    # we assume that it gets the implicit value 'None.'  This loop
    # counts up the number of 'missing' feature values for each
    # (label,fname) pair, and increments the count of the fval
    # 'None' by that amount.
    for label in label_freqdist:
        num_samples = label_freqdist[label]
        for fname in fnames:
            count = feature_freqdist[label, fname].N()
            # Only add a None key when necessary, i.e. if there are
            # any samples with feature 'fname' missing.
            if num_samples - count > 0:
                feature_freqdist[label, fname][None] += num_samples - count
                feature_values[fname].add(None)

    # Create the P(label) distribution
    label_probdist = estimator(label_freqdist)

    # Create the P(fval|label, fname) distribution
    feature_probdist = {}
    for ((label, fname), freqdist) in feature_freqdist.items():
        probdist = estimator(freqdist, bins=len(feature_values[fname]))
        feature_probdist[label, fname] = probdist

    return cls(label_probdist, feature_probdist)


def assess_classifier(classifier, test_set):
    refsets = collections.defaultdict(set)
    testsets = collections.defaultdict(set)
    for i, (feats, label) in enumerate(test_set):
        refsets[label].add(i)
        observed = classifier.classify(feats)
        testsets[observed].add(i)
    count = 0;
    print('Precision = ' + str(precision(refsets['spam'], testsets['spam'])))
    print('Recall = ' + str(recall(refsets['spam'], testsets['spam'])))
    print('F measure = ' + str(f_measure(refsets['spam'], testsets['spam'], alpha=0.5)))
    print('FP rate = ' + str(
        abs((len(refsets['ham']) - len(testsets['ham'])) / (len(refsets['spam']) + len(refsets['ham'])))))


if __name__ == "__main__":
    print('Naive Bayes ')
    spam = init_lists('training/spam/')
    ham = init_lists('training/ham/')
    spam1 = init_lists('testing/spam/')
    ham1 = init_lists('testing/ham/')
    all_emails = [(email, 'spam') for email in spam]
    all_emails += [(email, 'ham') for email in ham]
    print('Training size = ' + str(len(all_emails)) + ' emails')
    # random.shuffle(all_emails)
    all_emails1 = [(email, 'spam') for email in spam1]
    all_emails1 += [(email, 'ham') for email in ham1]
    print('Collecting features from dataset... ')
    # extract the features
    all_features = [(get_features(email, ''), label) for (email, label) in all_emails]
    test_features = [(get_features(email, ''), label) for (email, label) in all_emails1]

    print('Collected ' + str(len(all_features)) + 'feature sets')

    # train the classifier
    train_set, test_set, classifier = training(all_features, test_features)

    # Performance calculation
    assess_classifier(classifier, test_set)
    print("Accuracy is: " + str(classify.accuracy(classifier, test_set)))
    classifier.show_most_informative_features(20)
