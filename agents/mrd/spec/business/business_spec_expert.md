{
"id": 13,
"job": {
"sales": [
{
"jobAmt": [
3000000, 3000000, 3000000, 3000000, 3000000, 3000000, 3000000,
3000000, 3000000, 3000000, 3000000, 3000000
],
"jobCnt": 1,
"jobQty": 3000000,
"jobAmtCd": "1",
"jobTitle": "A담당",
"jobAmtRate": 0,
"jobAmtYear": [36000000, 36000000, 36000000]
},
{
"jobAmt": [
1500000, 1500000, 1500000, 1500000, 1500000, 1500000, 1500000,
1500000, 1500000, 1500000, 1500000, 1500000
],
"jobCnt": 1,
"jobQty": 1500000,
"jobAmtCd": "1",
"jobTitle": "B담당",
"jobAmtRate": 0,
"jobAmtYear": [18000000, 18000000, 18000000]
},
{
"jobAmt": [
800000, 800000, 800000, 800000, 800000, 800000, 800000, 800000,
800000, 800000, 800000, 800000
],
"jobCnt": 1,
"jobQty": 800000,
"jobAmtCd": "1",
"jobTitle": "C담당",
"jobAmtRate": 0,
"jobAmtYear": [9600000, 9600000, 9600000]
}
],
"production": []
},
"cogs": {
"title": "매출원가 분석 (통합)",
"endingInv": [0, 0, 0],
"totalCogs": [123600000, 123600000, 123600000],
"beginningInv": [0, 0, 0],
"sourceMapping": {
"labor" : "job.production",
"overhead" : "expense.production",
"materials": "cost.costY"
},
"currentPeriodCost": [123600000, 123600000, 123600000]
},
"cost": {
"costM": [
{
"costAmt": [
6000000, 6000000, 6000000, 6000000, 6000000, 6000000, 6000000,
6000000, 6000000, 6000000, 6000000, 6000000
]
},
{
"costAmt": [
2800000, 2800000, 2800000, 2800000, 2800000, 2800000, 2800000,
2800000, 2800000, 2800000, 2800000, 2800000
]
},
{
"costAmt": [
1125000, 1125000, 1125000, 1125000, 1125000, 1125000, 1125000,
1125000, 1125000, 1125000, 1125000, 1125000
]
},
{
"costAmt": [
375000, 375000, 375000, 375000, 375000, 375000, 375000, 375000,
375000, 375000, 375000, 375000
]
}
],
"costY": [
{
"costCd": "1",
"costAmt": [72000000, 72000000, 72000000],
"costRate": [40, 40, 40],
"costAmtRate": 0
},
{
"costCd": "1",
"costAmt": [33600000, 33600000, 33600000],
"costRate": [40, 40, 40],
"costAmtRate": 0
},
{
"costCd": "1",
"costAmt": [13500000, 13500000, 13500000],
"costRate": [30, 30, 30],
"costAmtRate": 0
},
{
"costCd": "1",
"costAmt": [4500000, 4500000, 4500000],
"costRate": [30, 30, 30],
"costAmtRate": 0
}
],
"costRate": [40, 40, 30, 30]
},
"loan": {
"debt": [
{
"loanRate": [3, 3, 3],
"loanTitle": "창업자금",
"loanAmtYear": [50000000, 0, 0],
"loanTitleCd": "",
"loanRateAmtYear": [1500000, 1500000, 1500000]
},
{
"loanRate": [6, 6, 6],
"loanTitle": "기타대출",
"loanAmtYear": [10000000, 0, 0],
"loanTitleCd": "",
"loanRateAmtYear": [600000, 600000, 600000]
}
],
"equity": [
{
"loanTitle": "자본금",
"loanAmtYear": [100000000, 0, 0],
"loanTitleCd": "10"
}
]
},
"mode": "expert",
"sale": {
"menu": [
{"menuQty": 30, "menuPrice": 20000, "menuTitle": "A메뉴"},
{"menuQty": 40, "menuPrice": 7000, "menuTitle": "B메뉴"},
{"menuQty": 50, "menuPrice": 3000, "menuTitle": "C메뉴"},
{"menuQty": 50, "menuPrice": 1000, "menuTitle": "D메뉴"}
],
"menuM": [
{
"menuAmt": [
15000000, 15000000, 15000000, 15000000, 15000000, 15000000, 15000000,
15000000, 15000000, 15000000, 15000000, 15000000
],
"menuQty": [750, 750, 750, 750, 750, 750, 750, 750, 750, 750, 750, 750]
},
{
"menuAmt": [
7000000, 7000000, 7000000, 7000000, 7000000, 7000000, 7000000,
7000000, 7000000, 7000000, 7000000, 7000000
],
"menuQty": [
1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000
]
},
{
"menuAmt": [
3750000, 3750000, 3750000, 3750000, 3750000, 3750000, 3750000,
3750000, 3750000, 3750000, 3750000, 3750000
],
"menuQty": [
1250, 1250, 1250, 1250, 1250, 1250, 1250, 1250, 1250, 1250, 1250, 1250
]
},
{
"menuAmt": [
1250000, 1250000, 1250000, 1250000, 1250000, 1250000, 1250000,
1250000, 1250000, 1250000, 1250000, 1250000
],
"menuQty": [
1250, 1250, 1250, 1250, 1250, 1250, 1250, 1250, 1250, 1250, 1250, 1250
]
}
],
"menuY": [
{
"menuCd": "1",
"menuAmt": [180000000, 180000000, 180000000],
"menuQty": [9000, 9000, 9000],
"menuPrice": [20000, 20000, 20000],
"menuQtyRate": 0,
"menuPriceRate": 0
},
{
"menuCd": "1",
"menuAmt": [84000000, 84000000, 84000000],
"menuQty": [12000, 12000, 12000],
"menuPrice": [7000, 7000, 7000],
"menuQtyRate": 0,
"menuPriceRate": 0
},
{
"menuCd": "1",
"menuAmt": [45000000, 45000000, 45000000],
"menuQty": [15000, 15000, 15000],
"menuPrice": [3000, 3000, 3000],
"menuQtyRate": 0,
"menuPriceRate": 0
},
{
"menuCd": "1",
"menuAmt": [15000000, 15000000, 15000000],
"menuQty": [15000, 15000, 15000],
"menuPrice": [1000, 1000, 1000],
"menuQtyRate": 0,
"menuPriceRate": 0
}
]
},
"basic": {
"workDay": 25,
"saleMode": "1",
"industryCd": "3",
"monTargetPrice": 5000000
},
"invest": {
"sales": {
"sales01": [
{
"investYn": "N",
"investYear": 0,
"investPrice": 10000000,
"investTitle": "토지",
"investAmtYear": 0,
"investTitleCd": "10"
},
{
"investYn": "Y",
"investYear": 5,
"investPrice": 40000000,
"investTitle": "시설공사비",
"investAmtYear": 666667,
"investTitleCd": "30"
},
{
"investYn": "Y",
"investYear": 5,
"investPrice": 16000000,
"investTitle": "비품구입비",
"investAmtYear": 266667,
"investTitleCd": "60"
},
{
"investYn": "Y",
"investYear": 5,
"investPrice": 2000000,
"investTitle": "기타 유형자산",
"investAmtYear": 33333,
"investTitleCd": "70"
}
],
"sales02": [
{
"investYn": "N",
"investYear": 0,
"investPrice": 50000000,
"investTitle": "영업권(권리금)",
"investAmtYear": 0,
"investTitleCd": "10"
}
],
"sales03": [
{
"investYn": "N",
"investYear": 0,
"investPrice": 50000000,
"investTitle": "임차보증금",
"investAmtYear": 0,
"investTitleCd": "10"
}
],
"sales04": [
{
"investYn": "N",
"investYear": 0,
"investPrice": 2000000,
"investTitle": "개업전 홍보비용",
"investAmtYear": 0,
"investTitleCd": "40"
}
]
},
"production": {
"production01": [],
"production02": [],
"production03": [],
"production04": []
}
},
"expense": {
"sales": [
{
"expenseAmt": [
5300000, 5300000, 5300000, 5300000, 5300000, 5300000, 5300000,
5300000, 5300000, 5300000, 5300000, 5300000
],
"expenseTVa": 100,
"expenseTVaCd": "인건비",
"expenseTitle": "급여",
"expenseTVaAmt": 63600000,
"manualMonthly": 0,
"expenseAmtYear": [63600000, 63600000, 63600000]
},
{
"expenseAmt": [
795000, 795000, 795000, 795000, 795000, 795000, 795000, 795000,
795000, 795000, 795000, 795000
],
"expenseTVa": 15,
"expenseTVaCd": "인건비",
"expenseTitle": "복리후생비",
"expenseTVaAmt": 63600000,
"manualMonthly": 0,
"expenseAmtYear": [9540000, 9540000, 9540000]
},
{
"expenseAmt": [
100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000,
100000, 100000, 100000, 100000
],
"expenseTVa": 0,
"expenseTVaCd": "월정액",
"expenseTitle": "통신비",
"expenseTVaAmt": 0,
"manualMonthly": 100000,
"expenseAmtYear": [1200000, 1200000, 1200000]
},
{
"expenseAmt": [
540000, 540000, 540000, 540000, 540000, 540000, 540000, 540000,
540000, 540000, 540000, 540000
],
"expenseTVa": 2,
"expenseTVaCd": "매출액",
"expenseTitle": "수도광열비",
"expenseTVaAmt": 324000000,
"manualMonthly": 0,
"expenseAmtYear": [6480000, 6480000, 6480000]
},
{
"expenseAmt": [
2700000, 2700000, 2700000, 2700000, 2700000, 2700000, 2700000,
2700000, 2700000, 2700000, 2700000, 2700000
],
"expenseTVa": 0,
"expenseTVaCd": "월정액",
"expenseTitle": "지급임차료",
"expenseTVaAmt": 0,
"manualMonthly": 2700000,
"expenseAmtYear": [32400000, 32400000, 32400000]
},
{
"expenseAmt": [
270000, 270000, 270000, 270000, 270000, 270000, 270000, 270000,
270000, 270000, 270000, 270000
],
"expenseTVa": 1,
"expenseTVaCd": "매출액",
"expenseTitle": "포장비",
"expenseTVaAmt": 324000000,
"manualMonthly": 0,
"expenseAmtYear": [3240000, 3240000, 3240000]
},
{
"expenseAmt": [
810000, 810000, 810000, 810000, 810000, 810000, 810000, 810000,
810000, 810000, 810000, 810000
],
"expenseTVa": 3,
"expenseTVaCd": "매출액",
"expenseTitle": "지급수수료",
"expenseTVaAmt": 324000000,
"manualMonthly": 0,
"expenseAmtYear": [9720000, 9720000, 9720000]
},
{
"expenseAmt": [
540000, 540000, 540000, 540000, 540000, 540000, 540000, 540000,
540000, 540000, 540000, 540000
],
"expenseTVa": 2,
"expenseTVaCd": "매출액",
"expenseTitle": "전력비",
"expenseTVaAmt": 324000000,
"manualMonthly": 0,
"expenseAmtYear": [6480000, 6480000, 6480000]
},
{
"expenseAmt": [
540000, 540000, 540000, 540000, 540000, 540000, 540000, 540000,
540000, 540000, 540000, 540000
],
"expenseTVa": 2,
"expenseTVaCd": "매출액",
"expenseTitle": "판매수수료",
"expenseTVaAmt": 324000000,
"manualMonthly": 0,
"expenseAmtYear": [6480000, 6480000, 6480000]
},
{
"expenseAmt": [
500000, 500000, 500000, 500000, 500000, 500000, 500000, 500000,
500000, 500000, 500000, 500000
],
"expenseTVa": 0,
"expenseTVaCd": "월정액",
"expenseTitle": "잡비",
"expenseTVaAmt": 0,
"manualMonthly": 500000,
"expenseAmtYear": [6000000, 6000000, 6000000]
}
],
"production": [
{
"expenseAmt": [
1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000,
1000000, 1000000, 1000000, 1000000, 1000000
],
"expenseTVa": 0,
"expenseTVaCd": "월정액",
"expenseTitle": "운반비",
"expenseTVaAmt": 0,
"manualMonthly": 1000000,
"expenseAmtYear": [12000000, 12000000, 12000000]
}
]
},
"summary": {
"opIncome": 4605000,
"bepRevenue": 20385000,
"ordAmtYear": [53160000, 53160000, 53160000],
"assetAmtYear": [213160000, 266320000, 319480000],
"totalRevenue": 27000000,
"equityAmtYear": [153160000, 206320000, 259480000]
}
}
