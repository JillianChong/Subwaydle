# source .venv/bin/activate

from bs4 import BeautifulSoup
from urllib.request import urlopen

def get_lines(url): 
    page = urlopen(url) # opens url
    html = page.read().decode("utf-8") # reads the html from the page
    # creates BeautifulSoup argument, 2 arguments: HTML to be parsed & HTML parser object
    soup = BeautifulSoup(html, "html.parser") 

    # print(soup.get_text()) # gets the text

    # Function to strip non-printable characters
    def strip_non_printable(s):
        return ''.join(c for c in s if c.isprintable())

    # Find all elements with the normalized data-title
    stops = soup.find_all(lambda tag: tag.name == 'td' and strip_non_printable(tag.get('data-title', '')) == "Subway Station")

    cleaned_stops = []
    for stop in stops:
        cleaned_stops.append(stop.find("p").text)
    
    return cleaned_stops

def write_file(name, title, url):
    '''
        name : name of file
        title : title of file
        url : url for webscraping
    '''
    f = open("./Stations/" + name + ".txt", "w")
    stops = get_lines(url)
    print(title, file=f)
    for stop in stops:
        print(stop, file=f)

trains = ["1", "2", "3", "4", "5", "6", "7", "A", "B", "C", "D", "E", "F", "G", "J", "L", "M", "N", "Q", "R", "W", "Z"]

# ARE YOU SURE? WILL OVERWRITE
# for train in trains:
#     write_file(train + "_train", train + " Train", "https://new.mta.info/maps/subway-line-maps/{f}-line".format(f=train))



