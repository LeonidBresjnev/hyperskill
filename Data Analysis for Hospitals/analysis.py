import pandas as pd
import matplotlib.pyplot as plt

pd.set_option('display.max_columns', 100)
general = pd.read_csv('test/general.csv')
prenatal = pd.read_csv('test/prenatal.csv')
sports = pd.read_csv('test/sports.csv')

prenatal.columns = general.columns
sports.columns = general.columns
all = pd.concat([general, prenatal, sports], ignore_index=True)
all.drop(columns=['Unnamed: 0'], inplace=True)
all.dropna(axis=0, how='all', inplace=True)
all.loc[all['hospital'] == 'prenatal', 'gender'] = 'f'
all['gender'] = all['gender'].apply(lambda x: x[0] if len(x) > 0 else None)
all['gender'] = all['gender'].apply(lambda x: 'f' if x == 'w' else x)
for x in ['bmi', 'diagnosis', 'blood_test', 'ecg', 'ultrasound', 'mri', 'xray', 'children', 'months']:
    all[x].fillna(0, inplace=True)

medians = all.pivot_table(columns='hospital', aggfunc='median', values='age')
bloodtest = all.query("blood_test == 't'").hospital.value_counts().sort_values(ascending=False)
# print("The answer to the 1st question is {}".format(all.hospital.value_counts().sort_values(ascending=False).index[0]))
# print("The answer to the 2nd question is {}".format(
#     all.query("hospital == 'general'").diagnosis.value_counts(normalize=True).stomach.round(3)))
# print("The answer to the 3rd question is {}".format(
#     all.query("hospital == 'sports'").diagnosis.value_counts(normalize=True).dislocation.round(3)))
# print("The answer to the 4th question is {}".format(medians.general[0] - medians.sports[0]))
# print("The answer to the 5th question is {}, {} blood tests".format(
#     bloodtest.index[0], bloodtest[0]))

bins = [0, 15, 35, 55, 70, 80]
all.plot(y='age', kind='hist', bins=bins)
plt.show()
diagnosescounts = all.diagnosis.value_counts()
plt.pie(diagnosescounts, labels=diagnosescounts.index, autopct='%.1f%%')
plt.show()
plt.violinplot(all.height)
plt.show()
print("""
The answer to the 1st question: 15-35
The answer to the 2nd question: pregnancy
The answer to the 3rd question: It's because Mixture of Gaussians — bimodal
""")
