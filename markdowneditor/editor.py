# print("""
# # John Lennon
# or ***John Winston Ono Lennon*** was one of *The Beatles*.
# Here are the songs he wrote I like the most:
#
# - Imagine
# - Norwegian Wood
# - Come Together
# - In My Life
# - ~~Hey Jude~~ (that was *McCartney*)
# """)

text = ""
while True:
    print("Choose a formatter:", end="")
    action = input().lower()
    if action == "!help":
        print("""Available formatters: plain bold italic link header inline-code ordered-list unordered-list new-line
Special commands: !help !done""")
    elif action == "!done":
        f = open('output.md', 'w')
        f.write(text)
        f.close()
        break
    elif action == "header":
        while True:
            level = int(input("Level:"))
            if 1 <= level <= 6:
                text += '#' * level + ' ' + input('Text:') + '\n'
                print(text)
                break
            else:
                print("The level should be within the range of 1 to 6")
    elif action == 'plain':
        text += input('Text:')
        print(text)
    elif action == 'bold':
        text += '**' + input('Text:') + '**'
        print(text)
    elif action == 'italic':
        text += '*' + input('Text:') + '*'
        print(text)
    elif action == 'inline-code':
        text += '`' + input('Text') + '`'
        print(text)
    elif action == 'link':
        text += '[' + input('Label:') + ']' + '(' + input('Url:') + ')'
        print(text)
    elif action == 'new-line':
        text += '\n'
        print(text)
    elif action == 'unordered-list':
        while True:
            n = int(input('Number of rows:'))
            if n > 0:
                for i in range(n):
                    text += '- ' + input('Row #' + str(i+1) + ' :') + '\n'
                print(text)
                break
            else:
                print("The number of rows should be greater than zero")
    elif action == 'ordered-list':
        while True:
            n = int(input('Number of rows:'))
            if n > 0:
                for i in range(n):
                    text += str(i+1) + '. ' + input('Row #' + str(i+1) + ' :') + '\n'
                print(text)
                break
            else:
                print("The number of rows should be greater than zero")
    else:
        print("Unknown formatting type or command", end="")
