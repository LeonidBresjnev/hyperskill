import json
import re

myjson = input()
fromjson = json.loads(myjson)
# Opening JSON file
# f = open("data.json", "r")
# fromjson = json.load(f)

# returns JSON object as
# a dictionary

bus_iderr = 0
stop_iderr = 0
stop_nameerr = 0
next_stoperr = 0
stoptypeerr = 0
a_timeerr = 0


class stop:
    allstops = {}
    allstops2 = {}
    finals = set()
    starts = set()

    def __init__(self, bus_id, stop_id, stop_name, next_stop, stop_type, a_time):
        self.bus_id = bus_id
        self.stop_id = stop_id
        self.stop_name = stop_name
        self.next_stop = next_stop
        self.stop_type = stop_type
        self.a_time = a_time

        if stop.allstops.get(self.bus_id) is None:
            stop.allstops[self.bus_id] = {}
        stop.allstops[bus_id][stop_id] = self

        if stop_type == "S":
            stop.starts.add(self.stop_name)
        if stop_type == "F":
            stop.finals.add(self.stop_name)
        if stop.allstops2.get(self.stop_id) is None:
            stop.allstops2[self.stop_id] = (self.stop_name, {})
        stop.allstops2[self.stop_id][1][self.bus_id] = self

    def printall(self):
        print("bus_id: " + str(self.bus_id) + "\nstop_id: " + str(
            self.stop_id) + "\nstop_name: " + self.stop_name + "\nnext_stop: " + str(self.next_stop) + "\n")


for i in range(len(fromjson)):
    bus_iderr = str(type(fromjson[i]['bus_id'])) != "<class 'int'>" or not bool(fromjson[i]['bus_id'])
    stop_iderr = str(type(fromjson[i]['stop_id'])) != "<class 'int'>" or not bool(fromjson[i]['stop_id'])
    stop_nameerr = not bool(
        fromjson[i]['stop_name']) or str(type(fromjson[i]['stop_name'])) != "<class 'str'>" or not bool(
        re.match(r"^[A-Z].*([R]oad|[S]treet|[B]oulevard|[A]venue|[Aa]ve\.)$",
                 str(fromjson[i]['stop_name'])))
    next_stoperr = str(type(fromjson[i]['next_stop'])) != "<class 'int'>"
    stoptypeerr = str(type(fromjson[i]['stop_type'])) != "<class 'str'>" or not bool(
        re.match("^[SOF]$|^$", str(fromjson[i]['stop_type'])))
    a_timeerr = str(type(fromjson[i]['a_time'])) != "<class 'str'>" or not bool(fromjson[i]['a_time']) or \
                not re.match("^([01][0-9]|2[0-4]|):[0-5][0-9]$", str(fromjson[i]['a_time']))
    # print(str(fromjson[i]['stop_name']) + str(type(fromjson[i]['stop_name'])) + str(stoptypeerr))"
    # print(fromjson[i]['stop_name'])
    # print(fromjson[i]['stop_name'] + str(stop_nameerr))
    # print(str(bus_iderr) + str(stop_iderr) + str(stop_nameerr) + str(next_stoperr) + str(a_timeerr))
    if not (bus_iderr or stop_iderr or stop_nameerr or next_stoperr or a_timeerr):
        newstop = stop(fromjson[i]['bus_id'],
                       fromjson[i]['stop_id'],
                       fromjson[i]['stop_name'],
                       fromjson[i]['next_stop'],
                       fromjson[i]['stop_type'],
                       fromjson[i]['a_time'])
        # newstop.printall()
# print(f"Type and required field validation: {stop_nameerr + stoptypeerr + a_timeerr} errors")
# print(f"stop_name: {stop_nameerr}\nstoptype: {stoptypeerr}\na_time:{a_timeerr}")

# print(
#    f"bus_id: {bus_iderr}\nstop_id: {stop_iderr}\nstop_name: {stop_nameerr}\nnext_stop: {next_stoperr}\nstoptype: {stoptypeerr}\na_time:{a_timeerr}")
# or fromjson[i]['stop_name'][0] != fromjson[i]['stop_name'][0].upper()
# print("Line names and number of stops:")
# for bus_id in sorted(stop.allstops):
#     print("bus_id: " + str(bus_id) + ", stops: " + str(len(stop.allstops[bus_id])))

for x in stop.allstops.keys():
    hasstart = False
    hasFinish = False
    for y in stop.allstops[x]:
        if stop.allstops[x][y].stop_type == "F":
            hasFinish = True
        if stop.allstops[x][y].stop_type == "S":
            hasstart = True

    if not hasstart or not hasFinish:
        print("There is no start or end stop for the line:", x)

# print("Start stops: " + str(len(stop.starts)) + " " + str(sorted(list(stop.starts))))

stopids = list(filter(lambda x: len(stop.allstops2[x][1].items()) >= 2, stop.allstops2.keys()))
transfers = set()
for x in stopids:
    transfers.add(stop.allstops2[x][0])
# print("Transfer: " + str(len(transfers)) + " " + str(sorted(transfers)))
# print("Finish stops: " + str(len(stop.finals)) + " " + str(sorted(list(stop.finals))))

# print(filter(stop.allstops2.items(), k -> len())
anywrong = False
# print("Arrival time test:")
# for x in sorted(stop.allstops.keys()):
#     # print()
#     firststop = True
#     for y in stop.allstops.get(x).keys():
#         # print(str(x) + " " + str(y) + " " + stop.allstops[x][y].a_time)
#         if (not firststop):
#             FMT = '%H:%M:%S'
#             tdelta = datetime.datetime.strptime(stop.allstops[x][y].a_time + ':00', FMT) - datetime.datetime.strptime(
#                 time + ':00', FMT)
#             # print(str(x) + " " + str(tdelta.total_seconds()))
#             if tdelta.total_seconds() <= 0:
#                 anywrong = True
#                 print("bus_id line " + str(x) + ": wrong time on station " + stop.allstops[x][y].stop_name)
#                 break
#         firststop = False
#         time = stop.allstops[x][y].a_time
# if not anywrong:
#     print("OK")


print("On demand stops test:")
wrongstops = set()
for x in sorted(stop.allstops.keys()):
    # print()
    firststop = True
    for y in stop.allstops.get(x).keys():
        if stop.allstops[x][y].stop_type == 'O' and stop.allstops[x][y].stop_name in transfers.union(stop.finals).union(
                stop.finals):
            # print(stop.allstops[x][y].stop_name)
            wrongstops.add(stop.allstops[x][y].stop_name)
if len(wrongstops) > 0:
    print(sorted(wrongstops))
else:
    print("OK")
