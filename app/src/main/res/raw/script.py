import csv

# Step 2: Create the data in the form of a list of lists (rows)
data = [
    ["Name", "Email", "Hashed_Password"],
    ["John", "john@gmail.com", "e10adc3949ba59abbe56e057f20f883e"]
    # Add more rows here if needed
]

# Step 3: Write the data to the CSV file
filename = "user_data.csv"
with open(filename, mode='w', newline='') as file:
    writer = csv.writer(file)

    # Write each row from the 'data' list to the CSV file
    for row in data:
        writer.writerow(row)