# source .venv/bin/activate

from bs4 import BeautifulSoup
from urllib.request import urlopen

def get_stations(url): 
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
    stops = get_stations(url)
    print(title, file=f)
    print(stops, file=f)

# write_file("1_train", "1 Train", "https://new.mta.info/maps/subway-line-maps/1-line")
# write_file("2_train", "2 Train", "https://new.mta.info/maps/subway-line-maps/2-line")
# write_file("3_train", "3 Train", "https://new.mta.info/maps/subway-line-maps/3-line")
# write_file("4_train", "4 Train", "https://new.mta.info/maps/subway-line-maps/4-line")
# write_file("5_train", "5 Train", "https://new.mta.info/maps/subway-line-maps/5-line")
# write_file("6_train", "6 Train", "https://new.mta.info/maps/subway-line-maps/6-line")
# write_file("7_train", "7 Train", "https://new.mta.info/maps/subway-line-maps/7-line")
# write_file("A_train", "A Train", "https://new.mta.info/maps/subway-line-maps/a-line")
# write_file("B_train", "B Train", "https://new.mta.info/maps/subway-line-maps/b-line")
# write_file("C_train", "C Train", "https://new.mta.info/maps/subway-line-maps/c-line")
# write_file("D_train", "D Train", "https://new.mta.info/maps/subway-line-maps/d-line")
# write_file("E_train", "E Train", "https://new.mta.info/maps/subway-line-maps/e-line")
# write_file("F_train", "F Train", "https://new.mta.info/maps/subway-line-maps/f-line")
# write_file("G_train", "G Train", "https://new.mta.info/maps/subway-line-maps/g-line")
# write_file("J_train", "J Train", "https://new.mta.info/maps/subway-line-maps/j-line")
# write_file("L_train", "L Train", "https://new.mta.info/maps/subway-line-maps/l-line")
# write_file("M_train", "M Train", "https://new.mta.info/maps/subway-line-maps/m-line")
# write_file("N_train", "N Train", "https://new.mta.info/maps/subway-line-maps/n-line")
# write_file("Q_train", "Q Train", "https://new.mta.info/maps/subway-line-maps/q-line")
# write_file("R_train", "R Train", "https://new.mta.info/maps/subway-line-maps/r-line")
# write_file("W_train", "W Train", "https://new.mta.info/maps/subway-line-maps/w-line")
# write_file("Z_train", "Z Train", "https://new.mta.info/maps/subway-line-maps/z-line")
