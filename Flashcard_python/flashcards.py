import random
import argparse


def printlog(string):
    print(string)
    print(string, file=mylog)


def add():
    printlog("The term for card:")
    while True:
        term = input()
        if terms.__contains__(term):
            print('The term "' + term + '" already exists. Try again:')
            continue
        else:
            break
    printlog("The definition for card:")
    while True:
        definition = input()
        if defs.__contains__(definition):
            printlog('The definition "' + definition + '" already exists. Try again:')
            continue
        else:
            break
    terms.add(term)
    defs.add(definition)
    cards[term] = [definition, 0]
    cards2[definition] = term
    printlog('The pair ("' + term + '":"' + definition + '") has been added.')


def ask():
    printlog("How many times to ask?")
    n = int(input())
    for i in range(n):
        term = random.choice(list(cards.keys()))
        printlog('Print the definition of "' + term + '"')
        answer = input()
        if answer == cards[term][0]:
            printlog("Correct")
        elif defs.__contains__(answer):
            printlog("Wrong. The right answer is " + cards[term][0] + ", but your definition is correct for " + cards2[
                answer] + ".")
            cards[term][1] += 1
        else:
            printlog('Wrong. The right answer is "' + cards[term][0] + '"')
            cards[term][1] += 1


def remove():
    printlog("Which card?\n")
    term = input()
    if terms.__contains__(term):
        cards2.pop(cards[term][0])
        defs.remove(cards[term][0])
        cards.pop(term)
        terms.remove(term)
        printlog("The card has been removed.")
    else:
        printlog("Can't remove " + '"' + term + '": there is no such card.')


def fromfile(x):
    if x == 0:
        printlog("File name:")
        filename = input()
    else:
        filename = args.import_from

    try:
        my_file = open(filename, 'r')
        i = 0
        for line in my_file.readlines():
            i += 1
            data = line.split('£')
            terms.add(data[0])
            cards[data[0]] = [data[1], int(data[2])]
            cards2[data[1]] = data[0]
            defs.add(data[1])
        printlog(str(i) + " card" + ("" if i == 1 else "s") + " have been loaded.")
    except FileNotFoundError:
        printlog("File not found.")


def export(x):
    if x == 0:
        printlog("File name:")
        filename = input()
        my_file = open(filename, 'w')
    else:
        my_file = open(args.export_to, 'w')
    for key in cards:
        my_file.write(key + '£' + cards[key][0] + '£' + str(cards[key][1]) + "\n")

    printlog(str(len(terms)) + " cards have been saved")


def reset():
    for key in cards:
        cards[key][1] = 0
    printlog("Card statistics have been reset.")


def hardest():
    if len(cards) > 0:
        sortedlist = list(cards.values())
        sortedlist.sort(key=lambda x: x[1])
        hardestcard = sortedlist[-1]
        if hardestcard[1] > 0:
            printlog('The hardest card is "' + cards2[hardestcard[0]] + '". You have ' + str(hardestcard[
                                                                                                 1]) + 'errors '
                                                                                                       'answering it')
        else:
            printlog("There are no cards with errors.")
    else:
        printlog("There are no cards with errors.")


parser = argparse.ArgumentParser(description="description")
parser.add_argument("-i", "--import_from")  # optional argument
parser.add_argument("-e", "--export_to")  # optional argument
args = parser.parse_args()

cards = {}
cards2 = {}
terms = set()
defs = set()
logfile = ""
mylog = open('mylog.txt', 'w')

if args.import_from is not None:
    fromfile(1)

while True:
    printlog("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):")
    action = input()
    if action == "exit":
        printlog("Bye bye!")
        break
    elif action == "add":
        add()
    elif action == "remove":
        remove()
    elif action == "import":
        fromfile(0)
    elif action == "export":
        export(0)
    elif action == "ask":
        ask()
    elif action == "reset stats":
        reset()
    elif action == "hardest card":
        hardest()
    elif action == "log":
        printlog("File name:")
        logfile = open(input(), 'w')
        mylog.close()
        mylog = open('mylog.txt', 'r')
        for line in mylog.readlines():
            logfile.write(line)
        logfile.close()
        mylog.close()
        mylog = open('mylog.txt', 'w')
        printlog("The log has been saved.")

if args.export_to is not None:
    export(1)
