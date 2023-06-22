# spring-transaction-processing-tdd

Problem statement:         

Lets say we have multiple file formats Excel, Csv, FixedLengthFileFormat all of them have the same template with different set of data.

Data saved as per the template and will always have 7 columns.

Example of a card template is transaction_id, 'card', card_no, exp_date, cvv, amount, remarks

Example of a wallet template is transaction_id, 'wallet', wallettype, upi_id, balance, amount, remarks

We have to read the file, process the data, validate accroding to the rule set defined

Save the data to the DB. Data should be saved to different Tables according to the transactionType (card/wallet/mobile funds transfer/internetbanking).

Sample data : 

transaction id,card,card number/wallettype,expiry date/upi id,cvv/balance,amount,remarks

123424313,card,876998799879,23-01-2020,123,123.412,card processing

12346234213,card,876998799879,23-01-2020,123,123.412,card processing

1234653213,wallet,e-wallet,paytm,1237,123.412,wallet processing

1265234213,wallet,digi-wallet,paytm,1238,123.412,wallet processing
