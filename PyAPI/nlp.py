#!/usr/bin/env python3
# -*- coding: utf-8 -*-

from textblob import TextBlob

# returns top five wishes based on sentiment analysis.
# if the total number of wishes is less than 5, it will return all the wishes.


def selectBestResponses(data):
    topFiveWishes = {}
    polarities = []
    for wish in data:
        polarities.append(TextBlob(data[wish]).polarity)
    polarities.sort(reverse=True)
    topFivePolarities = polarities[:5]

    for polarity in topFivePolarities:
        for wish in data:
            if TextBlob(data[wish]).polarity == polarity:
                topFiveWishes[wish] = data[wish]
                del(data[wish])
                break
    return topFiveWishes
