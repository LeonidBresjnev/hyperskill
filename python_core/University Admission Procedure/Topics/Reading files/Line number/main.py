# read sample.txt and print the number of lines
#  write your code here

my_file = open("sample.txt")
print(len(my_file.readlines()))
my_file.close()
