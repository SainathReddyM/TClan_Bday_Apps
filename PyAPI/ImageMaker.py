#!/usr/bin/env python3
# -*- coding: utf-8 -*-

from PIL import Image, ImageDraw, ImageFont
import base64
from io import BytesIO
import textwrap
import os
import properties
import random


def wrapText(text):
    max_length = 54
    text = textwrap.wrap(text, max_length)
    return text

# prints wish on the card.


def putWishOnCard(wisher, wish, base, yCoordinate, wishnumber, font_path):
    wish = wrapText(wish)
    text_size = 35
    txt = Image.new("RGBA", base.size, (255, 0, 0, 0))
    fnt = ImageFont.truetype(properties.font_path, text_size)
    d = ImageDraw.Draw(txt)
    for i in wish:
        d.text((500, yCoordinate), i, font=fnt, fill=(255, 255, 255))
        yCoordinate += text_size+10
    fnt = ImageFont.truetype(properties.font_path_for_name, 80)
    d.text((1200, yCoordinate), "-"+wisher,
           font=fnt, fill=(255, 255, 255, 255))
    yCoordinate = 10+(wishnumber*256)
    out = Image.alpha_composite(base, txt)
    return (out, yCoordinate)

# prints footer text on the greeting card.


def putFooter(base, footer_fontpath):
    font_text = "Cheers!!Team ThoughtClan :)"
    text_size = 50
    x, y = 600, 1540
    txt = Image.new("RGBA", base.size, (255, 0, 0, 0))
    fnt = ImageFont.truetype(footer_fontpath, text_size)
    d = ImageDraw.Draw(txt)
    d.text((x, y), font_text, font=fnt, fill=(255, 255, 255))
    out = Image.alpha_composite(base, txt)
    return out

# prints name at the bottom of the greeting card just above the footer text


def putName(greetingCard, eventType, firstName):
    if(eventType == "WORK_ANNIVERSARY"):
        message = "Happy work anniversary "+firstName+"!!"
    else:
        message = "Happy Birthday "+firstName+"!!"
    text_size = 70
    x, y = 500, 1400
    text = Image.new("RGBA", greetingCard.size, (255, 0, 0, 0))
    font = ImageFont.truetype(properties.footer_fontpath, text_size)
    textImage = ImageDraw.Draw(text)
    textImage.text((x, y), message, font=font, fill=(255, 255, 255))
    greetingCard = Image.alpha_composite(greetingCard, text)
    return greetingCard


def fillEmptySpaceOnCard(greetingCard, eventType, firstName, wishNumber, yCoordinate):
    if(eventType == "WORK_ANNIVERSARY"):
        line1 = "Happy work anniversary"
    else:
        line1 = "Happy Birthday"
    line2 = firstName+"!!"
    text_size = 150
    x, y = 100, 878
    text = Image.new("RGBA", greetingCard.size, (255, 0, 0, 0))
    font = ImageFont.truetype(properties.footer_fontpath, text_size)
    textImage = ImageDraw.Draw(text)
    textImage.text((x, y), line1, font=font, fill=(255, 255, 255))
    greetingCard = Image.alpha_composite(greetingCard, text)
    textImage.text((420, 1058), line2, font=font, fill=(255, 255, 255))
    greetingCard = Image.alpha_composite(greetingCard, text)
    return greetingCard


# Prints greeting on card in case there are no wishes to be printed on the card.
def fillBlankCard(greetingCard, eventType, firstName):
    line1 = "Happy"
    line3 = firstName
    text = Image.new("RGBA", greetingCard.size, (255, 0, 0, 0))
    font = ImageFont.truetype(properties.footer_fontpath, 200)
    textImage = ImageDraw.Draw(text)
    textImage.text((700, 300), line1, font=font, fill=(255, 255, 255))
    greetingCard = Image.alpha_composite(greetingCard, text)
    if(eventType == "WORK_ANNIVERSARY"):
        line2 = "work anniversary"
        textImage.text((200, 600), line2, font=font, fill=(255, 255, 255))
        greetingCard = Image.alpha_composite(greetingCard, text)
    else:
        line2 = "Birthday"
        textImage.text((550, 600), line2, font=font, fill=(255, 255, 255))
        greetingCard = Image.alpha_composite(greetingCard, text)
    textImage.text((600, 900), line3, font=font, fill=(255, 255, 255))
    greetingCard = Image.alpha_composite(greetingCard, text)
    return greetingCard


def generateCard(eventType, firstName, topFiveWishes):
    blankCardPath = pickBlankCard(eventType)
    greetingCard = Image.open(blankCardPath).convert("RGBA")
    yCoordinate = 10
    wishNumber = 1
    for wisher in topFiveWishes:
        greetingCard, yCoordinate = putWishOnCard(
            wisher, topFiveWishes[wisher], greetingCard, yCoordinate, wishNumber, properties.font_path)
        wishNumber += 1
    if(wishNumber == 1):
        greetingCard = fillBlankCard(greetingCard, eventType, firstName)
    elif(wishNumber <= 4):
        greetingCard = fillEmptySpaceOnCard(
            greetingCard, eventType, firstName, wishNumber, yCoordinate)
    else:
        greetingCard = putName(greetingCard, eventType, firstName)
    greetingCard = putFooter(greetingCard, properties.footer_fontpath)
    greetingCard = greetingCard.convert("RGB")
    buffered = BytesIO()
    greetingCard.save(buffered, format="JPEG")
    base64EncodedGreetingCard = base64.b64encode(buffered.getvalue())
    return base64EncodedGreetingCard


def pickBlankCard(eventType):
    if(eventType == "WORK_ANNIVERSARY"):
        blankCardsList = os.listdir(properties.work_anniversary_blank_cards)
        blankCardsList.remove('.DS_Store')
        blankCard = random.choice(blankCardsList)
        return properties.work_anniversary_blank_cards+blankCard
    else:
        blankCardsList = os.listdir(properties.birthday_blank_cards)
        blankCardsList.remove('.DS_Store')
        blankCard = random.choice(blankCardsList)
        return properties.birthday_blank_cards+blankCard
