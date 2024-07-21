import plotly.express as px
import plotly.graph_objects as go
import pandas as pd

df = pd.read_csv("src/misc/files/out.csv", delimiter=',')

df.dropna(
    axis=0,
    how='any',
    subset=['GTFS Latitude', 'GTFS Longitude'],
    inplace=True
)

def plot_map(train1, train2, train3, start, transfer1, transfer2, end):
    # Plotting map -- change the colors of the right points?
    fig = px.scatter_mapbox(df, 
                            lat="GTFS Latitude", 
                            lon="GTFS Longitude", 
                            hover_name="Stop Name", 
                            hover_data={"GTFS Latitude": True, "GTFS Longitude": True},
                            color_discrete_sequence=["red"],
                            zoom=10, 
                            height=800,
                            width=800)
    
    fig.update_layout(mapbox_style="open-street-map")
    fig.update_layout(margin={"r":0,"t":0,"l":0,"b":0})

    # ADD: plot line
    df.set_index('Stop Name', inplace=True)
    plot_path(fig, train1, train2, train3, start, transfer1, transfer2, end)

    fig.show()

def plot_path(fig, train1, train2, train3, start, transfer1, transfer2, end):
    # Create a line between two points
    # Define the points for the line (for example, the first two points in the dataframe)
    set1 = [train1, start, transfer1]
    set2 = [train2, transfer1, transfer2]
    set3 = [train3, transfer2, end]

    path1 = return_subpath(set1)
    path2 = return_subpath(set2)
    path3 = return_subpath(set3)

    path = path1 + path2 + path3

    for i in range(len(path) - 1):
        point1 = df.loc[path[i]]
        point2 = df.loc[path[1+1]]

        line = go.Scattermapbox(
            mode = "lines",
            lon = [point1["GTFS Longitude"], point2["GTFS Longitude"]],
            lat = [point1["GTFS Latitude"], point2["GTFS Latitude"]],
            marker = {'size': 10},
            line = dict(width = 4, color = 'blue')
        )

        fig.add_trace(line)

def return_subpath(set):
    train = set[0]
    start = set[1].strip()
    end = set[2].strip()

    file = open("src/misc/stations/"+train+"_train.txt", "r")
    file.readline()

    startIndex = -1
    endIndex = -1

    path = []

    for idx, line in enumerate(file):
        line = line.strip()
        if line == start and endIndex == -1:
            path.append(line)
            startIndex = idx
            continue

        if line == end and startIndex != -1:
            path.append(line)
            break
    
        if line == end and startIndex == -1:
            path[:0] = [line]
            endIndex = idx
            continue

        if line == start and endIndex != -1:
            path[:0] = [line]
            break

        if startIndex > -1:
            path.append(line)

        if endIndex > -1:
            path[:0] = [line]

    return path


# path = return_subpath(["A" ,"Aqueduct Racetrack", "Utica Av"])
# print(path)

plot_map("L", "3", "2", 'New Lots Av', 'Junius St & Livonia Av', '135 St', 'Gun Hill Rd')
    

# TODO: CLEAN THIS DATASET!
# Write method to plot the entire route
# Label transfer stations!
# Save the image & upload to use in javascript
