#!/bin/bash

echo "Enter the Stack name: "
read stack_name

   aws_response=$(aws cloudformation create-stack --stack-name $stack_name --template-body file://csye6225-cf-waf.yml)

   echo "Waiting for stack to be created ..."
   aws cloudformation wait stack-create-complete --stack-name $stack_name 

   echo "Stack Id = $aws_response created successfully!"