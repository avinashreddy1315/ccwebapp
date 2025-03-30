#!/usr/bin/env bash
#******************************************************************************
#    AWS Stack Creation Shell Script
#******************************************************************************
#
# SYNOPSIS
#    Automates the creation of a custom VPC, 3 subnets, gateway, Route Table
#    using cloudformation Stack
#
# DESCRIPTION
#    This shell script leverages the AWS Command Line Interface (AWS CLI) to
#    automatically create a stack that will create custom VPC and other related
#    resources.
#==============================================================================

# NOTES
#   VERSION:   0.1.0
#   LASTEDIT:  06/14/2019
#   AUTHOR:    Anurag Dhar
#   EMAIL:     dhar.a@husky.neu.edu
#   REVISIONS:
#       0.1.0  06/10/2019 - first release
#       0.1.1  06/14/2019 - second release
#
#==============================================================================
#   User provided Input
#==============================================================================
#

read -p "Enter Stack Name: " stack_name
read -p "enter CidrBlock - format 10.0.0.0/16: " cidrBlock

# Creating stacks
echo "creating stack....."

aws cloudformation create-stack \
--stack-name ${stack_name} \
--template-body file://csye6225-cf-networking.json \
--parameters ParameterKey=stackName,ParameterValue=$stack_name ParameterKey=cidrBlock,ParameterValue=$cidrBlock
# Waiting for the stack to get created
echo "Waiting on ${stack_name} for create completion..."

aws cloudformation wait stack-create-complete --stack-name ${stack_name}
aws cloudformation describe-stacks --stack-name ${stack_name}

echo "stack created successfully"
# Stack creation ends
