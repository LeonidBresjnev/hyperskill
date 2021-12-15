def findwinner():
    xwins = False
    owins = False
    for i in range(3):
        if board[3 * i] + board[3 * i + 1] + board[3 * i + 2] in ("XXX", "OOO"):
            if board[3 * i] == "X":
                xwins = True
            elif board[3 * i] == "O":
                owins = True

    for j in range(3):
        if board[j] == board[3 + j] and board[j] == board[3 * 2 + j]:
            if board[j] == "X":
                xwins = True
            elif board[j] == "O":
                owins = True

    if board[0] == board[3 + 1] and board[0] == board[3 * 2 + 2]:
        if board[0] == "X":
            xwins = True
        elif board[0] == "O":
            owins = True
    if board[2] == board[3 + 1] and board[2] == board[3 * 2]:
        if board[2] == "X":
            xwins = True
        elif board[2] == "O":
            owins = True

    if xwins and owins:
        return 9
    elif xwins:
        return 1
    elif owins:
        return -1
    else:
        return 0


def printboard():
    print("---------")
    for i in range(3):
        print("|", end='')
        for j in range(3):
            print(" " + board[3 * i + j], end='')
        print(" |")
    print("---------")


board = list((" " * 9).replace("_", " "))
turn = True

while True:
    printboard()
    while True:
        print("Enter the coordinates:")
        movestr = input().split(" ")
        if (len(movestr) == 2) and movestr[0].isnumeric() and movestr[1].isnumeric():
            i = int(movestr[0]) - 1
            j = int(movestr[1]) - 1
            if max(i, j) >= 3 or min(i, j) < 0:
                print("Coordinates should be from 1 to 3!")
            elif board[3 * i + j] != " ":
                print("This cell is occupied! Choose another one!")
            else:
                board[3 * i + j] = "X" if turn else "O"
                break
        else:
            print("You should enter numbers!")
    winner = findwinner()
    count = [sum([1 if x == "X" else 0 for x in board]), sum([1 if x == "O" else 0 for x in board])]
    if abs(count[0] - count[1]) >= 2 or winner == 9:
        print("Impossible")
        break
    elif abs(winner) == 1:
        printboard()
        print(("X" if turn else "O") + " wins")
        break
    elif count[0] + count[1] == 9:
        printboard()
        print("Draw")
        break
    turn = not turn
