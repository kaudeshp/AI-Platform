from flask import Flask, abort, request, Response
import signal
import threading
import time
import os
import requests

import json
import random

import spacy
import newspaper
from newspaper import Article

import en_core_web_sm

app = Flask(__name__)
shuttingDown = False


@app.route("/")
def hello():
		return "Hello World!"


@app.route("/news_stockdata")
def liveness():

	articleLink = "https://www.cnbc.com/2018/08/30/microsoft-will-require-partners-suppliers-to-offer-paid-family-leave.html"
	
	print(" Loading modules ")
	nlp = spacy.load('en_core_web_sm')

	print(" Retrieving the article ")
	article = Article(articleLink)
	article.download()
	article.parse()
	print(" Parsed article ")

	text = article.text
	print(" Printed article text ")

	print(" nlp on summary ")
	summary = article.summary
	doc = nlp(summary)

	for token in doc:
		print(token.text, token.pos, token.dep_)

	print(" Retrieved tokens from doc ")

	stockName = "AAPL"
	stockData = getStockData(articleLink, stockName)

	return Response(json.dumps(stockData), mimetype='application/json')

def getStockData(articleLink, stockName):

	stockData = {
		"articleProcessed": articleLink,
		"stock": stockName,
        "score": str(random.randint(51, 100)),
        "history": [
			82,
            84,
            81,
            53,
            74,
            94,
            65
		]
	}

	return stockData

if __name__ == '__main__':
    app.run(debug=True, port=port)
