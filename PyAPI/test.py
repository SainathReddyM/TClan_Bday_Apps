from textblob import TextBlob

data = {
    "firstName": "he is a good guy",
    "lastName": "he is not a good guy"
}

for k in data:
    print(TextBlob(data[k]).polarity)
    print(TextBlob(data[k]).sentiment)
