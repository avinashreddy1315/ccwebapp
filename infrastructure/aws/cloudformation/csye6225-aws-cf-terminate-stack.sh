#!/usr/bin/env bash
#******************************************************************************
#    AWS Stack Deletion Shell Script
#******************************************************************************
#
# SYNOPSIS
#    Automates the deletion of a Stack
#
# DESCRIPTION
#    This shell script leverages the AWS Command Line Interface (AWS CLI) to
#    automatically delete a stack.
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


echo "deleting stack....."

aws cloudformation delete-stack \
--stack-name ${stack_name}

echo "Waiting on ${stack_name} for delete completion..."

aws cloudformation wait stack-delete-complete --stack-name ${stack_name}

echo "stack deleted successfully"
