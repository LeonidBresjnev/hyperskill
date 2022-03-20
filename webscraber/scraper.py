import requests
from bs4 import BeautifulSoup
import os

pages = int(input('Number of pages\n'))
articletype = input('Article type\n')

current_directory = os.getcwd()

for page in range(pages):
    directory_to_save = 'Page_' + str(page + 1)
    os.mkdir(directory_to_save)
    os.chdir(directory_to_save)

    response = requests.get("https://www.nature.com/nature/articles",
                            params={'year': '2020', 'sort': 'PubDate', 'page': page + 1, })

    if response.status_code == 200:
        soup = BeautifulSoup(response.content, 'html.parser')
        result = {}
        paragraphs = soup.find_all('article')
        for p in paragraphs:
            news = p.find_all('span', {'data-test': 'article.type'})
            for n in news:
                if n.text.strip() == articletype:
                    links = p.find_all('a')
                    for a in links:
                        article = requests.get("https://www.nature.com" + a.get('href'),
                                               headers={'Accept-Language': 'en-US,en;q=0.5'})
                        if article.status_code == 200:
                            articlesoup = BeautifulSoup(article.content, 'html.parser')
                            f = open(str(a.text.strip()).replace(".", "").replace("?", "").replace(" ", "_") + ".txt",
                                     "wb")
                            f.write(articlesoup.find('div', {'class': 'c-article-body'}).text.strip().encode())
                            f.close()
    os.chdir(current_directory)
