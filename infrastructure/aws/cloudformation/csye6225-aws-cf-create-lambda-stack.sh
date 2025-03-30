	#!/bin/sh
    echo "Please provide a stack name"
    read StackName

    echo "enter domain"
    read Domain


	aws cloudformation create-stack \
    --stack-name ${StackName} \
    --template-body file://csye6225-aws-cf-create-lambda.json \
    --parameters \
    ParameterKey=Domain,ParameterValue=$Domain \

   	echo "Waiting on ${StackName} for create completion..."
    aws cloudformation wait stack-create-complete --stack-name ${StackName}
	echo "Lambda Stack creation successfully!!!"
	fnUpdate=$(aws lambda update-function-configuration --function-name MyLambdaFunction --handler LogEvent::handleRequest --runtime java8)
