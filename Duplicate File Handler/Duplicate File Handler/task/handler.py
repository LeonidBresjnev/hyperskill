# !/usr/bin/python3
import os
import re
import sys  # first, we import the module
import hashlib

args = sys.argv  # we get the list of arguments
if len(args) == 1:
    print("Print Directory is not specified")
else:
    print(args)
    print("Enter file format:")
    ext = input()
    print("Size sorting options:\n1. Descending\n2. Ascending")
    while True:
        sort = input()
        if sort == "1" or sort == "2":
            break
        else:
            print("Wrong option")

    mydict = {}
    for root, dirs, files in os.walk('.\\' + args[1], topdown=False):
        for name in files:
            if not ext or bool(re.findall("." + ext + "$", name)):
                size = os.path.getsize(os.path.join(root, name))
                if mydict.get(size) is None:
                    mydict[size] = [os.path.join(root, name)]
                else:
                    mydict[size].append(os.path.join(root, name))

    for obj in sorted(mydict, reverse=(sort == "1")):
        print("\n" + str(obj) + " bytes")
        for x in mydict.get(obj):
            print(x)

    while True:
        print("\nCheck for duplicates?")
        checkdup = input()
        if checkdup.lower() == "yes":
            i = 0
            # myset = set()
            mydict = {}
            for root, dirs, files in os.walk('.\\' + args[1], topdown=False):
                for name in files:
                    if not ext or bool(re.findall("." + ext + "$", name)):
                        size = os.path.getsize(os.path.join(root, name))

                        with open(os.path.join(root, name), 'rb') as myFile:
                            m = hashlib.md5()
                            m.update(myFile.read())
                            x = m.hexdigest()
                        if mydict.get(size) is None:
                            mydict[size] = {x: [os.path.join(root, name)]}
                        else:
                            if mydict.get(size).get(x) is None:
                                mydict[size][x] = [os.path.join(root, name)]
                            else:
                                mydict[size][x].append(os.path.join(root, name))

            dups = set()
            for byte in mydict:
                for x in mydict.get(byte):
                    if len(mydict.get(byte).get(x)) > 1:
                        dups.add(byte)

            dict2 = {}
            for byte in sorted(dups, reverse=(sort == "1")):
                print("\n" + str(byte) + " bytes")
                for x in sorted(mydict.get(byte), reverse=False):
                    if len(mydict.get(byte).get(x)) > 1:
                        print("Hash: " + x)
                        x = sorted(mydict.get(byte).get(x), reverse=(sort == "1"))
                        for y in x:
                            i += 1
                            dict2[i] = (byte, y)
                            print(str(i) + ". " + y)

            print("Delete files?")
            while True:
                x = input().split(" ")
                formatok = True
                for j in range(len(x)):
                    if x[j] < '1' or x[j] > str(i):
                        formatok = False
                if formatok:
                    sum = 0
                    for j in range(len(x)):
                        sum += dict2[int(x[j])][0]
                        os.remove(dict2[int(x[j])][1])

                    print("Total freed up space: {sum} bytes")
                    break
                else:
                    print("Wrong format")

        elif checkdup.lower() == "no":
            break
        else:
            print("Wrong option")
