#!/usr/bin/env bash

#Enter app name
read -p "Enter Stack Name: " applicationStackName


echo "deleting stack....."

aws cloudformation delete-stack \
--stack-name ${applicationStackName}

echo "Waiting on ${applicationStackName} for delete completion..."

aws cloudformation wait stack-delete-complete --stack-name ${applicationStackName}

echo "stack deleted successfully"
