import pandas as pd

df = pd.read_csv("src/misc/files/nyc-transit-subway-entrance-and-exit-data.csv")

# Removing duplicate stations
df = df.drop_duplicates(subset='Station Name')

print(df.head())