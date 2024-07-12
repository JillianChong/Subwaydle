# source .venv/bin/activate

from bs4 import BeautifulSoup
from urllib.request import urlopen

def get_transfers(url): 
    page = urlopen(url)
    html = page.read().decode("utf-8") 
    soup = BeautifulSoup(html, "html.parser") 

    # Find all transfer stations
    stations = soup.find_all(attrs={'class':'mw-category-group'})

    cleaned_stations = []
    for station in stations:
        name = station.find("a").text.replace('station', '').replace('stations','').strip()
        cleaned_stations.append(name)
    
    return cleaned_stations[1:]

def write_file(url):
    '''
        name : name of file
        title : title of file
        url : url for webscraping
    '''
    f = open("transfers.txt", "w")
    stations = get_transfers(url)
    for station in stations:
        print(station, file=f)

write_file("https://en.wikipedia.org/wiki/Category:New_York_City_Subway_transfer_stations")
# stations = get_transfers("https://en.wikipedia.org/wiki/Category:New_York_City_Subway_transfer_stations")
# print(stations)