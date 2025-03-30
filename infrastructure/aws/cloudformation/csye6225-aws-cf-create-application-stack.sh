#!/bin/bash

#sh csye6225-aws-cf-create-stack.sh

echo "enter your Application stack"
read applicationStackName

echo "enter your Networking Stack"
read networkStackName

echo "enter your AMI"
read ami

echo "enter your S3bucket"
read bucket

echo "enter your S3CodeBucket"
read S3bucket


aws cloudformation create-stack \
--stack-name ${applicationStackName} \
--template-body file://csye6225-cf-application.json \
--parameters \
ParameterKey=networkStackName,ParameterValue=$networkStackName \
ParameterKey=AMI,ParameterValue=$ami \
ParameterKey=s3bucketName,ParameterValue=$bucket \
ParameterKey=S3CodeBucket,ParameterValue=$S3bucket \
# ParameterKey=AttachmentsBucketName,ParameterValue=$bucket \
# Waiting for the stack to get created
echo "Waiting on ${applicationStackName} for create completion..."

aws cloudformation wait stack-create-complete --stack-name ${applicationStackName}
aws cloudformation describe-stacks --stack-name ${applicationStackName}
