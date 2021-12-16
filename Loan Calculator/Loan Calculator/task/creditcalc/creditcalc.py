import math
from math import ceil
import argparse

parser = argparse.ArgumentParser()
parser.add_argument('--type', '--type')
parser.add_argument('--principal', '--principal')
parser.add_argument('--periods', '--periods')
parser.add_argument('--interest', '--interest')
parser.add_argument('--payment', '--payment')
args = parser.parse_args()
parameters = [args.type, args.principal, args.periods, args.interest, args.payment]
#print(parameters)

if not parameters[1] is None:
    loan_principal = float(parameters[1])
if not parameters[2] is None:
    n = float(parameters[2])
if not parameters[3] is None:
    rate = float(parameters[3]) / 1200
if not parameters[4] is None:
    payment = float(parameters[4])

if str(parameters[0]).lower() not in ("annuity", "diff"):
    print("Incorrect parameters")
elif not parameters[1] is None and loan_principal < 0:
    print("Incorrect parameters.")
elif str(parameters[0]).lower() == "annuity":
    nones = sum([1 if x is None else 0 for x in parameters])
    if not nones == 1:
        print("Incorrect parameters")
    elif parameters[2] is None:
        n = ceil(math.log(payment / (payment - rate * loan_principal), 1 + rate))
        print("It will take " + str(n // 12) + " years" + (" and " + str(n % 2) + "months" if n % 2 > 0 else "")  + " to repay this loan!")
        print("Overpayment = " + str(round(n * payment - loan_principal)))
    elif parameters[4] is None:
        print("Your monthly payment = " + str(
            ceil(loan_principal * (rate * (1 + rate) ** n) / ((1 + rate) ** n - 1))) + "!")
    elif parameters[1] is None:
        loan_principal =  math.floor(payment / ((rate * (1 + rate) ** n) / ((1 + rate) ** n - 1)))
        print("Your loan principal = " + str(loan_principal))
        print("Overpayment = " + str(round(n * payment - loan_principal)))
elif parameters[0] == "diff":
    nones = sum([1 if x is None else 0 for x in parameters])
    if not nones == 1:
        print("Incorrect parameters")
    elif parameters[4] is None:
        rest = loan_principal
        i = 1
        total = 0
        while rest > 0:
            n = int(parameters[2])
            payment = loan_principal / n + rate * (loan_principal * (1 - (i - 1) / n))
            rest -= loan_principal / n
            total += ceil(payment)
            print("Month " + str(i) + ": payment is " + str(ceil(payment)))
            i += 1
        print("\nOverpayment = ", str(ceil(total - loan_principal)))
    # elif parameters[2] is None:
    #     n = loan_principal / payment
    #     print("It will take " + str(n // 12) + " years " + (" and " + str(n % 2) + "montshs" if n % 2 > 0 else "") +"to repay this loan!")
    #     print("Overpayment = " + str(n * ))
