import argparse
import ast
import re
import os
import sys

from collections import defaultdict


class PepAnalyzer(ast.NodeVisitor):
    def __init__(self):
        self.stats: dict[str, dict[int, list]] = {
            "variables": defaultdict(list),
            "parameters": defaultdict(list),
            "is_constant_default": defaultdict(list),
        }

    def visit_Name(self, node):
        if isinstance(node.ctx, ast.Store):
            self.stats["variables"][node.lineno].append(node.id)
        self.generic_visit(node)

    def visit_FunctionDef(self, node):
        for a in node.args.args:
            self.stats["parameters"][node.lineno].append(a.arg)
        for a in node.args.defaults:
            self.stats["is_constant_default"][node.lineno].append(isinstance(a, ast.Constant))
        self.generic_visit(node)

    def get_parameters(self, lineno: int) -> list:
        return self.stats["parameters"][lineno]

    def get_variables(self, lineno: int) -> list:
        return self.stats["variables"][lineno]

    def get_mutable_defaults(self, lineno: int) -> str:
        for param_name, is_default in zip(self.stats["parameters"][lineno], self.stats["is_constant_default"][lineno]):
            if not is_default:
                return param_name
        return ""


def analyzer(path, filename):
    file = open(filename, 'r')

    tree = ast.parse(file.read())

    pep_analyzer = PepAnalyzer()
    pep_analyzer.visit(tree)

    file.seek(0)
    lines = file.readlines()
    # print(lines)
    prevprevprev = False
    prevprev = False
    prev = False
    for i, line in enumerate(lines):
        # print(line, file = sys.stderr)
        errors = set()
        if not errors.__contains__(1) and len(line) > 79:
            print(f'{path + filename}: Line {i + 1}: S001 Too long')
            errors.add(1)
        if not errors.__contains__(2) and len(line.lstrip()) > 0 and (len(line) - len(line.lstrip())) % 4 > 0:
            print(f'{path + filename}: Line {i + 1}: S002 Indentation is not a multiple of four')
            errors.add(2)
        if not errors.__contains__(3) and re.match(r'[^#]*;\s*(#.*)?$', line) is not None:
            print(f'{path + filename}: Line {i + 1}: S003 Unnecessary semicolon after a statement')
            errors.add(3)
        if not errors.__contains__(4) and re.match(r'[^#]*\S\s?#', line) is not None:
            print(f'{path + filename}: Line {i + 1}: S004 Less than two spaces before inline comments')
            errors.add(4)
        if not errors.__contains__(5) and re.match(r'.*#.*TODO.*', line, flags=re.IGNORECASE) is not None:
            print(f'{path + filename}: Line {i + 1}: S005 TODO found')
            errors.add(5)
        if not errors.__contains__(6) and len(line.strip()) > 0 and prev and prevprev and prevprevprev:
            print(f'{path + filename}: Line {i + 1}: S006 More than two blank lines used before this line')
            errors.add(6)
        if re.match(r"^([ ]*(?:class|def) ( )+)", line):
            print(f'{path + filename}: Line {i + 1}: S007 Too many spaces after construction_name (def or class)')
            errors.add(7)
        if matches := re.match(r"^[ ]*class (?P<name>\w+)", line):
            print(matches["name"], file=sys.stderr)
            if not re.match(r"(?:[A-Z][a-z0-9]+)+", matches["name"]):
                print(f'{path + filename}: Line {i + 1}: S008 Class name {matches["name"]} should use CamelCase')
                errors.add(8)
        if matches := re.match(r"^(?:[ ]*def (?P<name>\w+))", line):
            if not re.match(r"[a-z_]+", matches["name"]):
                print(f'{path + filename}: Line {i + 1}: S009 Function name {matches["name"]} should use snake_case')
                errors.add(9)
        for parameter in pep_analyzer.get_parameters(i+1):
            if not re.match(r"[a-z_]+", parameter):
                print(f'{path + filename}: Line {i + 1}: S010 Argument name {parameter} should be snake_case')
                errors.add(10)
                break

        for variable in pep_analyzer.get_variables(i+1):
            if not re.match(r"[a-z_]+", variable):
                print(f'{path + filename}: Line {i + 1}: S011 Variable {variable} in function should be snake_case')
                errors.add(11)
                break

        if pep_analyzer.get_mutable_defaults(i+1):
            print(f'{path + filename}: Line {i + 1}: S012 The default argument value is mutable')
            errors.add(12)

        prevprevprev = prevprev
        prevprev = prev
        prev = (len(line.strip()) == 0)


args = sys.argv  # we get the list of arguments
if len(args) == 1:
    print("Print Directory is not specified")
else:
    if os.path.isfile(os.getcwd() + '/' + args[1]):
        if args[1].split(".")[-1].lower() == "py":
            analyzer('', args[1])
    else:
        for root, dirs, files in os.walk(args[1], topdown=False):
            for name in files:
                os.chdir(root)
                if name.split(".")[-1].lower() == "py":
                    analyzer(root + os.sep, name)
