my_file = open("sums.txt")
for line in my_file:
    print(int(line.split()[0]) + int(line.split()[1]))
my_file.close()
