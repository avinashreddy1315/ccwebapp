#!/usr/bin/env bash

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

domain=$(aws route53 list-hosted-zones --query HostedZones[0].Name --output text)
dname="${domain:0:-1}"
certificateArn=$(aws acm list-certificates --query CertificateSummaryList[0].CertificateArn --output text)


_hostedZoneID=$(aws route53 list-hosted-zones --query HostedZones[0].Id --output text)
echo "zoneID  " ${_hostedZoneID}
hostedZoneId=${_hostedZoneID:12:${#_hostedZoneID}}

echo "zoneID11  " $hostedZoneId

aws cloudformation create-stack \
--stack-name ${applicationStackName} \
--template-body file://csye6225-cf-auto-scaling-application.json \
--parameters \
ParameterKey=networkStackName,ParameterValue=$networkStackName \
ParameterKey=AMI,ParameterValue=$ami \
ParameterKey=s3bucketName,ParameterValue=$bucket \
ParameterKey=S3CodeBucket,ParameterValue=$S3bucket \
ParameterKey=domainName,ParameterValue=$dname \
ParameterKey=hostedZoneID,ParameterValue="${_hostedZoneID:12:${#_hostedZoneID}}" \
ParameterKey=certificateARN,ParameterValue=$certificateArn

echo "Waiting on ${applicationStackName} for create completion..."

aws cloudformation wait stack-create-complete --stack-name ${applicationStackName}
aws cloudformation describe-stacks --stack-name ${applicationStackName}
