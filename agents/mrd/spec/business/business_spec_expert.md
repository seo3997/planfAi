{
"basic": {
"industryCd": "3",
"workDay": 25,
"monTargetPrice": 5000000,
"saleMode": "1"
},
"sale": {
"menu": [
{ "menuTitle": "A제품(제조)", "menuPrice": 20000, "menuQty": 30 },
{ "menuTitle": "B제품(제조)", "menuPrice": 7000, "menuQty": 40 },
{ "menuTitle": "C소모품(유통)", "menuPrice": 3000, "menuQty": 50 },
{ "menuTitle": "D소모품(유통)", "menuPrice": 1000, "menuQty": 50 }
],
"menuM": [
{ "menuQty": [750, 750, 750, 750, 750, 750, 750, 750, 750, 750, 750, 750], "menuAmt": [15000000, 15000000, 15000000, 15000000, 15000000, 15000000, 15000000, 15000000, 15000000, 15000000, 15000000, 15000000] },
{ "menuQty": [1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000], "menuAmt": [7000000, 7000000, 7000000, 7000000, 7000000, 7000000, 7000000, 7000000, 7000000, 7000000, 7000000, 7000000] }
],
"menuY": [
{ "menuQty": [9000, 9000, 9000], "menuPrice": [20000, 20000, 20000], "menuAmt": [180000000, 180000000, 180000000], "menuCd": "1", "menuQtyRate": 0, "menuPriceRate": 0 }
]
},
"cost": {
"costRate": [40, 40, 30, 30],
"costM": [
{ "costAmt": [6000000, 6000000, 6000000, 6000000, 6000000, 6000000, 6000000, 6000000, 6000000, 6000000, 6000000, 6000000] }
],
"costY": [
{ "costAmt": [72000000, 72000000, 72000000], "costRate": [40, 40, 40], "costCd": "1", "costAmtRate": 0 }
]
},
"job": {
"production": [
{
"jobTitle": "생산직 관리", "jobCnt": "1", "jobQty": 3000000,
"jobAmt": [3000000, 3000000, 3000000, 3000000, 3000000, 3000000, 3000000, 3000000, 3000000, 3000000, 3000000, 3000000],
"jobAmtYear": [36000000, 36000000, 36000000], "jobAmtCd": "1", "jobAmtRate": 0
}
],
"sales": [
{
"jobTitle": "매장영업", "jobCnt": "1", "jobQty": 1500000,
"jobAmt": [1500000, 1500000, 1500000, 1500000, 1500000, 1500000, 1500000, 1500000, 1500000, 1500000, 1500000, 1500000],
"jobAmtYear": [18000000, 18000000, 18000000], "jobAmtCd": "1", "jobAmtRate": 0
}
]
},
"invest": {
"production": {
"production01": [{ "investTitle": "제조설비공사", "investTitleCd": "30", "investPrice": 40000000, "investYear": 5, "investAmtYear": 667000, "investYn": "Y" }],
"production02": [], "production03": [], "production04": []
},
"sales": {
"sales01": [{ "investTitle": "인테리어비용", "investTitleCd": "60", "investPrice": 16000000, "investYear": 5, "investAmtYear": 267000, "investYn": "Y" }],
"sales02": [{ "investTitle": "보증금/권리금", "investTitleCd": "10", "investPrice": 100000000, "investAmtYear": 0, "investYn": "N" }],
"sales03": [], "sales04": []
}
},
"loan": {
"equity": [{ "loanTitle": "자본금", "loanTitleCd": "10", "loanAmtYear": [100000000] }],
"debt": [
{ "loanTitle": "소상공인융자", "loanTitleCd": "10", "loanAmtYear": [50000000, 0, 0], "loanRate": [3.0, 3.0, 3.0], "loanRateAmtYear": [1500000, 1500000, 1500000] }
]
},
"expense": {
"production": [
{
"expenseTitle": "공장전력비",
"expenseTVaAmt": 2700000,
"expenseTVa": 3.5,
"expenseTVaCd":"매출액",
"expenseAmt": [2700000, 2700000, 2700000, 2700000, 2700000, 2700000, 2700000, 2700000, 2700000, 2700000, 2700000, 2700000],
"expenseAmtYear": [32400000, 32400000, 32400000]
}
],
"sales": [
{
"expenseTitle": "지급임차료",
"expenseTVaAmt": 2700000,
"expenseTVa": 3.5,
"expenseTVaCd":"매출액",
"expenseAmt": [2700000, 2700000, 2700000, 2700000, 2700000, 2700000, 2700000, 2700000, 2700000, 2700000, 2700000, 2700000],
"expenseAmtYear": [32400000, 32400000, 32400000]
}
]
},
"cogs": {
"title": "매출원가 분석 (통합)",
"beginningInv": [0, 2000000, 2500000],
"endingInv": [2000000, 2500000, 3000000],
"currentPeriodCost": [140400000, 140400000, 140400000],
"totalCogs": [138400000, 139900000, 139900000],
"sourceMapping": {
"materials": "cost.costY[0].costAmt",
"labor": "job.production[0].jobAmtYear",
"overhead": "expense.production[0].expenseAmtYear"
}
},
"summary": {
"opIncome": 4605000,
"bepRevenue": 20385000,
"ordAmtYear": [
53160000,
53160000,
53160000
],
"totalRevenue": 27000000
}
}
