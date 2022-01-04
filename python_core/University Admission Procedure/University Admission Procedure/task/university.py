with open("applicants.txt") as applicants:
    m = int(input())
    grades = []
    for line in applicants:
        splitline = line.split(" ")
        grades.append(
            [splitline[0] + " " + splitline[1], int(splitline[2]), int(splitline[3]), int(splitline[4]), int(splitline[5]),
             int(splitline[6]), splitline[7], splitline[8], splitline[9].strip()])

accepted = {"Physics": [], "Chemistry": [], "Mathematics": [], "Engineering": [], "Biotech": []}
topics = ["Physics", "Chemistry", "Mathematics", "Engineering", "Biotech"]


def weigtedaverage(z, w):
    average = float(w[0] * z[1] + w[1] * z[2] + w[2] * z[3] + w[3] * z[4])
    return average


W = [[0.5, 0, 0.5, 0],
     [0, 1, 0, 0],
     [0, 0, 1, 0],
     [0, 0, 0.5, 0.5],
     [0.5, 0.5, 0, 0]]


def priority(input, remaining, wish):
    for i in range(len(topics)):
        input = sorted(input, key=lambda z: (-max(weigtedaverage(z, W[i]), z[5]), z[5 + wish], z[0]))
        for x in input:
            if x[5 + wish] == topics[i]:
                if len(accepted[topics[i]]) < m:
                    accepted[topics[i]].append(x)
                else:
                    remaining.append(x)


left = []
priority(grades, left, 1)
left2 = []
priority(left, left2, 2)
priority(left2, left, 3)


for x in sorted(accepted.keys()):
    k = topics.index(x)
    with open(x + ".txt", mode="wt") as myfile:
        students = sorted(accepted[x], key=lambda z: (-max(weigtedaverage(z, W[k]), z[5]), z[0]))
        for y in students:
            myfile.writelines(y[0] + " " + str(max(weigtedaverage(y, W[k]), y[5])) + "\n")
        myfile.writelines("\n")
