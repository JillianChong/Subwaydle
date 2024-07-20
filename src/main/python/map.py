# TEST

# import pandas as pd
# from shapely.geometry import Point
# import geopandas as gpd
# from geopandas import GeoDataFrame
# import geodatasets
# import matplotlib.pyplot as plt

# df = pd.read_csv('src/main/python/nyc-transit-subway-entrance-and-exit-data.csv', delimiter=',')

# geometry = [Point(xy) for xy in zip(df['Station Longitude'], df['Station Latitude'])]
# gdf = GeoDataFrame(df, geometry=geometry)

# world = gpd.read_file(geodatasets.data.naturalearth.land['url'])
# gdf.plot(ax=world.plot(figsize=(10,6), marker='o', color='red', markersize=15))
# plt.show()

import plotly.express as px
import plotly.graph_objects as go
import pandas as pd

df = pd.read_csv("src/misc/files/nyc-transit-subway-entrance-and-exit-data.csv", delimiter=',')

df.dropna(
    axis=0,
    how='any',
    subset=['Station Latitude', 'Station Longitude'],
    inplace=True
)

# Plotting map -- change the colors of the right points?
fig = px.scatter_mapbox(df, 
                        lat="Station Latitude", 
                        lon="Station Longitude", 
                        hover_name="Station Name", 
                        hover_data={"Station Latitude": True, "Station Longitude": True},
                        color_discrete_sequence=["red"],
                        zoom=10, 
                        height=800,
                        width=800)

# Create a line between two points
# Define the points for the line (for example, the first two points in the dataframe)
point1 = df.iloc[0]
point2 = df.iloc[10]

line = go.Scattermapbox(
    mode = "lines",
    lon = [point1["Station Longitude"], point2["Station Longitude"]],
    lat = [point1["Station Latitude"], point2["Station Latitude"]],
    marker = {'size': 10},
    line = dict(width = 4, color = 'blue')
)

fig.update_layout(mapbox_style="open-street-map")
fig.update_layout(margin={"r":0,"t":0,"l":0,"b":0})

fig.add_trace(line)
fig.show()

# TODO: CLEAN THIS DATASET!
# Write method to plot the entire route
# Label transfer stations!
# Save the image & upload to use in javascript
