from flask import Flask, request, send_file
from nlp import selectBestResponses
import base64
from ImageMaker import generateCard, pickBlankCard
import properties
app = Flask(__name__)


@app.route("/generate-card", methods=['POST'])
def generateGreetingCard():
    data = request.json
    eventType = data.pop("eventType")
    firstName = data.pop("for")
    topFiveWishes = selectBestResponses(data)
    base64EncodedImage = generateCard(eventType, firstName, topFiveWishes)
    return base64EncodedImage


if __name__ == "__main__":
    app.run(debug=True)
