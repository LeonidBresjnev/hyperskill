import math

import numpy as np
from sklearn.datasets import load_breast_cancer
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.linear_model import LogisticRegression


class CustomLogisticRegression:

    def __init__(self, fit_intercept=True, l_rate=0.01, n_epoch=1000):
        self.fit_intercept = fit_intercept
        self.l_rate = l_rate
        self.n_epoch = n_epoch
        self.coef_ = None

    @staticmethod
    def sigmoid(t):
        return 1 / (1 + math.exp(-t))

    def predict_proba(self, row, coef_):
        t = (coef_[0] if self.fit_intercept else 0) + np.dot(row, coef_[(1 if self.fit_intercept else 0):])
        return self.sigmoid(t)

    def fit_mse(self, x_train, y_train):
        self.coef_ = np.zeros(x.shape[1] + 1 if self.fit_intercept else 0)

        mse0 = [(self.predict_proba(row, self.coef_) - y_train['target'].values[k]) ** 2 for k, row in
                enumerate(x_train)]

        for _ in range(self.n_epoch):
            for k, row in enumerate(x_train):
                y_hat = self.predict_proba(row, self.coef_)
                yhat_compl = y_hat * (1 - y_hat)
                for j in range(4):
                    self.coef_[j] = self.coef_[j] - self.l_rate * (y_hat - y_train['target'].values[k]) * yhat_compl * (
                        1 if j == 0 and self.fit_intercept else row[j - (1 if self.fit_intercept else 0)])
        mse1 = [(self.predict_proba(row, self.coef_) - y_train['target'].values[k]) ** 2 for k, row in
                enumerate(x_train)]

        return (mse0, mse1)

    def fit_log_loss(self, x_train, y_train):
        self.coef_ = np.zeros(x_train.shape[1] + 1 if self.fit_intercept else 0)

        loss0 = [y_train['target'].values[k] * math.log(self.predict_proba(row, self.coef_)) + (
                    1 - y_train['target'].values[k]) * math.log(
            1 - self.predict_proba(row, self.coef_)) for k, row in enumerate(x_train)]

        for _ in range(self.n_epoch):
            for k, row in enumerate(x_train):
                y_hat = self.predict_proba(row, self.coef_)
                for j in range(x_train.shape[1] + 1 if self.fit_intercept else 0):
                    self.coef_[j] = self.coef_[j] - self.l_rate * (y_hat - y_train['target'].values[k]) * (
                        1 if j == 0 and self.fit_intercept else row[j - (1 if self.fit_intercept else 0)]) / \
                                    x_train.shape[0]

        loss1 = [y_train['target'].values[k] * math.log(self.predict_proba(row, self.coef_)) + (
                    1 - y_train['target'].values[k]) * math.log(
            1 - self.predict_proba(row, self.coef_)) for k, row in enumerate(x_train)]
        return (loss0, loss1)

    def predict(self, x_test, cut_off=0.5):
        return [1 if self.predict_proba(row, self.coef_) > cut_off else 0 for row in x_test]

    def accuracy(self, x_test, y_test, cut_off=0.5):
        predictions = self.predict(x_test, cut_off)
        accuracy = 0
        for i in range(len(predictions)):
            accuracy += 1 if y_test['target'].values[i] == int(predictions[i]) else 0
        return accuracy / len(predictions)



data = load_breast_cancer(as_frame=True).frame

x = data[['worst concave points', 'worst perimeter', 'worst radius']]
y = data[['target']]
x = StandardScaler().fit_transform(x)

x_train, x_test, y_train, y_test = train_test_split(x, y, train_size=0.8, random_state=43)

model = CustomLogisticRegression(True, 0.01, 1000)

mse = model.fit_mse(x_train, y_train)
accuracy_mse = model.accuracy(x_test, y_test)

logloss = model.fit_log_loss(x_train, y_train)
accuracy_logloss = model.accuracy(x_test, y_test)

# print(accuracy_mse, accuracy_logloss)
model = LogisticRegression(max_iter=200)
model.fit(x_train, y_train['target'])

answerlist = {'mse_accuracy': accuracy_mse, 'logloss_accuracy': accuracy_logloss,
              'sklearn_accuracy': model.score(x_test, y_test['target']), "mse_error_first": mse[0],
              "mse_error_last": mse[1], "logloss_error_first": logloss[0], "logloss_error_last": logloss[1]}
print(answerlist)

print('\nAnswers to the questions:\n1) {a1}\n2) {a2}\n3) {a3}\n4) {a4}\n5) {a5}\n6) {a6}'.format(
     a1=0.0001, a2=0, a3=0.00153, a4=0.006,
     a5="expanded", a6="expanded")
)
