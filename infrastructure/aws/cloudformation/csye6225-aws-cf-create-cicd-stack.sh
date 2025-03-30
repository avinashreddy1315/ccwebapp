#!/usr/bin/env bash


read -p "Please enter policy stack name : " PolicyStack

aws_domain_name=$(aws route53 list-hosted-zones --query 'HostedZones[0].Name' --output text)
bucketName="code-deploy.${aws_domain_name:0:-1}"
ImagebucketName="${aws_domain_name:0:-1}.csye6225.com"
echo ${ImagebucketName}
echo ${bucketName}
#export bucketName=$(aws s3api list-buckets --query "Buckets[].Name" --output text|grep code-deploy|awk '{print $1}')

UserId="circleci"


#aws cloudformation validate-template --template-body file://testpol.json


aws cloudformation create-stack --stack-name $PolicyStack \
--template-body file://csye6255-cf-cicd.json \
--capabilities CAPABILITY_NAMED_IAM \
--parameters \
ParameterKey=CircleCIUser,ParameterValue=$UserId \
ParameterKey=S3CodeBucket,ParameterValue=$bucketName \
ParameterKey=S3CodebucketImage,ParameterValue=$ImagebucketName


export STACK_STATUS=$(aws cloudformation describe-stacks --stack-name $PolicyStack --query "Stacks[][ [StackStatus ] ][]" --output text)

while [ $STACK_STATUS != "CREATE_COMPLETE" ]
do
	STACK_STATUS=`aws cloudformation describe-stacks --stack-name $PolicyStack --query "Stacks[][ [StackStatus ] ][]" --output text`
done
echo "Created Stack ${PolicyStack} successfully!"
