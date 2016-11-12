import requests

url = "https://api.seatgeek.com/2/taxonomies"

r = requests.get(url)
j = r.json()
tax = j["taxonomies"]

name_arr = []

for i in range(len(tax)):
    str1 = tax[i]["name"].lower()
    str2 = str1.replace(" ", "_")
    name_arr.append(str2)
    print(str2)
