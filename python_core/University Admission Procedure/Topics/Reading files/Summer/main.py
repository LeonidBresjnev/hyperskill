#  write your code here

my_file = open("data/dataset/input.txt")
counter = 0
for line in my_file:
    if "summer\n" == line:
        counter = counter +1

my_file.close()
print(counter)
