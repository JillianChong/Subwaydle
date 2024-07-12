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
        names = station.find_all("a")
        for name in names:
            name = name.text
            name = name.replace('station', '')
            name = name.replace('stations','')
            name = name.replace('Street', 'St')
            name = name.replace('Avenue', 'Av')
            name = name.strip()
            cleaned_stations.append(name)
    
    return cleaned_stations[1:]

def write_file(url):
    '''
        name : name of file
        title : title of file
        url : url for webscraping
    '''
    f = open("./Transfers/transfers.txt", "w")
    stations = get_transfers(url)
    for station in stations:
        print(station, file=f)

# write_file("https://en.wikipedia.org/wiki/Category:New_York_City_Subway_transfer_stations")

# Not counting the Bryant Park Times Sq transfer