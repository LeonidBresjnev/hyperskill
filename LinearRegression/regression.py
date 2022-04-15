import math
import numpy as np
import pandas as pd
from sklearn.linear_model import LinearRegression
from sklearn.metrics import mean_squared_error, r2_score


class CustomLinearRegression:

    def __init__(self, *, fit_intercept=True):

        self.fit_intercept = fit_intercept
        self.coefficient = None
        self.intercept = None

    def fit(self, X, y):
        if self.fit_intercept:
            Z = np.column_stack([np.ones(X.shape[0]), X])
        else:
            Z = X

        ZZZ = np.matmul(np.linalg.inv(np.matmul(np.transpose(Z), Z)), np.transpose(Z))
        betahat = np.matmul(ZZZ, y)

        self.intercept = betahat[0] if self.fit_intercept else 0
        self.coefficient = np.array(betahat[0 if not self.fit_intercept else 1:(betahat.shape[0])])
        residual = np.matmul(np.subtract(np.identity(y.shape[0]), np.matmul(Z, ZZZ)), y)
        residual0 = np.empty_like(y)
        residual0.fill(y.mean())
        residual0 = np.subtract(y, residual0)
        result = {'Intercept': self.intercept, 'Coefficient': self.coefficient,
                  'R2': 1 - (np.linalg.norm(residual) / np.linalg.norm(residual0)) ** 2,
                  'RMSE': np.linalg.norm(residual) / (y.shape[0] ** (1 / 2))}
        yhat = np.matmul(Z, betahat)
        return result


data = pd.read_csv('data_stage4.csv', sep=',')
model0 = CustomLinearRegression(fit_intercept=True)
result = model0.fit(data[["f1", "f2", "f3"]].to_numpy(), data['y'].transpose().to_numpy())

model1 = LinearRegression()
model1.fit(data[["f1", "f2", "f3"]], data['y'])
predictions1 = model1.predict(data[["f1", "f2", "f3"]])
dif = {'Intercept': -result['Intercept'] + model1.intercept_, 'Coefficient': -result['Coefficient'] + model1.coef_,
       'R2': r2_score(data['y'], predictions1) - result['R2'],
       'RMSE': math.sqrt(mean_squared_error(data['y'], predictions1)) - result['RMSE']}
print(dif)