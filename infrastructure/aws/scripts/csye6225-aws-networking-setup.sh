#!/bin/bash
#******************************************************************************
#    AWS Networking Resources Creation Shell Script
#******************************************************************************
#
# SYNOPSIS
#    Automates the creation of a custom VPC, 3 subnets, gateway, Route Table.
#
# DESCRIPTION
#    This shell script leverages the AWS Command Line Interface (AWS CLI) to
#    automatically create a custom VPC and other realted resources.
#==============================================================================

# NOTES
#   VERSION:   0.1.0
#   LASTEDIT:  06/11/2019
#   AUTHOR:    Soumavo Guria
#   EMAIL:     guria.s@husky.neu.edu
#   REVISIONS:
#       0.1.0  06/10/2019 - first release
#       0.1.1  06/11/2019 - second release
#       0.1.2  06/13/2019 - final release
#
#==============================================================================
#   User provided Input
#==============================================================================
#
read -p "Enter your Region :" AWS_REGION
read -p "Enter a tag Name for VPC:" TAG_NAME
read -p "Enter your VPC_CIDR :" VPC_CIDR
read -p "Enter your Subnet1 Public CIDR :" SUBNET1_PUBLIC_CIDR
read -p "Enter your Subnet1 Availability Zone :" SUBNET1_PUBLIC_VR
# read -p "Enter your Subnet1 Public Name :" SUBNET1_PUBLIC_NAME
read -p "Enter your Subnet2 Public CIDR :" SUBNET2_PUBLIC_CIDR
read -p "Enter your Subnet2 Public Availability Zone :" SUBNET2_PUBLIC_VR
# read -p "Enter your Subnet2 Public Name :" SUBNET2_PUBLIC_NAME
read -p "Enter your Subnet3 Public CIDR :" SUBNET3_PUBLIC_CIDR
read -p "Enter your Subnet3 Public Availability Zone :" SUBNET3_PUBLIC_VR
# read -p "Enter your Subnet3 Public Name :" SUBNET3_PUBLIC_NAME
# read -p "Enter Internet Gateway Name :" IGW_NAME
# read -p "Enter Route Table Name :" ROUTE_TABLE_NAME

step="START"

echo " *********************************************** "
echo " ************* Script Started ****************** "
echo " *********************************************** "


step="Create VPC"

VPC_ID=$(aws ec2 create-vpc \
  --cidr-block $VPC_CIDR \
  --query 'Vpc.{VpcId:VpcId}' \
  --output text \
  --region $AWS_REGION)
echo "Creating VPC in preferred region"

flag=$?
#Create VPC
# Add Name tag to VPC
if [ $flag -eq 0 ]
then
echo "  VPC ID '$VPC_ID' CREATED in '$AWS_REGION' region."
  step="Create tag for VPC"
    aws ec2 create-tags \
    --resources $VPC_ID \
    --tags "Key=Name,Value=${TAG_NAME}" \
    --region $AWS_REGION
    echo "  VPC ID '$VPC_ID' NAMED as '$TAG_NAME'."
flag=$?

fi

# Create Public Subnet1
if [ $flag -eq 0 ]
then
step="Create Subnet1"
echo "Creating Public Subnet1..."
SUBNET1_PUBLIC_ID=$(aws ec2 create-subnet \
  --vpc-id $VPC_ID \
  --cidr-block $SUBNET1_PUBLIC_CIDR \
  --availability-zone $SUBNET1_PUBLIC_VR \
  --query 'Subnet.{SubnetId:SubnetId}' \
  --output text \
  --region $AWS_REGION)
  echo "  Subnet ID '$SUBNET1_PUBLIC_ID' CREATED in '$SUBNET1_PUBLIC_VR'" \
  "Availability Zone."
  flag=$?
fi


# Add Name tag to Public Subnet1
if [ $flag -eq 0 ]
then
step="Create tag for Subnet1"
aws ec2 create-tags \
  --resources $SUBNET1_PUBLIC_ID \
  --tags "Key=Name,Value=${TAG_NAME}-publicSubnet1" \
  --region $AWS_REGION
echo "  Subnet ID '$SUBNET1_PUBLIC_ID' NAMED as" \
"'${TAG_NAME}-publicSubnet1'."
flag=$?
fi



# Create Public Subnet2
if [ $flag -eq 0 ]
then
step="Create Subnet2"
echo "Creating Public Subnet2..."
SUBNET2_PUBLIC_ID=$(aws ec2 create-subnet \
  --vpc-id $VPC_ID \
  --cidr-block $SUBNET2_PUBLIC_CIDR \
  --availability-zone $SUBNET2_PUBLIC_VR \
  --query 'Subnet.{SubnetId:SubnetId}' \
  --output text \
  --region $AWS_REGION)
echo "  Subnet ID '$SUBNET2_PUBLIC_ID' CREATED in '$SUBNET2_PUBLIC_VR'" \
"Availability Zone."
flag=$?
fi

# Add Name tag to Public Subnet2
if [ $flag -eq 0 ]
then
step="Create tag for Subnet2"
aws ec2 create-tags \
  --resources $SUBNET2_PUBLIC_ID \
  --tags "Key=Name,Value=${TAG_NAME}-publicSubnet2" \
  --region $AWS_REGION
echo "  Subnet ID '$SUBNET2_PUBLIC_ID' NAMED as" \
"'${TAG_NAME}-publicSubnet2'."
flag=$?

fi

# Create Public Subnet3
if [ $flag -eq 0 ]
then
step="Create Subnet3"
echo "Creating Public Subnet3..."
SUBNET3_PUBLIC_ID=$(aws ec2 create-subnet \
  --vpc-id $VPC_ID \
  --cidr-block $SUBNET3_PUBLIC_CIDR \
  --availability-zone $SUBNET3_PUBLIC_VR \
  --query 'Subnet.{SubnetId:SubnetId}' \
  --output text \
  --region $AWS_REGION)
echo "  Subnet ID '$SUBNET3_PUBLIC_ID' CREATED in '$SUBNET3_PUBLIC_VR'" \
"Availability Zone."
flag=$?

fi


# Add Name tag to Public Subnet3
if [ $flag -eq 0 ]
then
step="Create tag for Subnet3"
aws ec2 create-tags \
  --resources $SUBNET3_PUBLIC_ID \
  --tags "Key=Name,Value=${TAG_NAME}-publicSubnet3" \
  --region $AWS_REGION
echo "  Subnet ID '$SUBNET3_PUBLIC_ID' NAMED as" \
"'${TAG_NAME}-publicSubnet3'."
flag=$?

fi




# Create Internet gateway
if [ $flag -eq 0 ]
then
step="Create IG"
echo "Creating Internet Gateway..."
IGW_ID=$(aws ec2 create-internet-gateway \
  --query 'InternetGateway.{InternetGatewayId:InternetGatewayId}' \
  --output text \
  --region $AWS_REGION)
echo "  Internet Gateway ID '$IGW_ID' CREATED."
flag=$?
fi

#Add Name tag to Internet gateway
if [ $flag -eq 0 ]
then
step="Create IG Tag"
aws ec2 create-tags \
  --resources $IGW_ID \
  --tags "Key=Name,Value=${TAG_NAME}-IGW" \
  --region $AWS_REGION
echo " Internet Gateway ID '$IGW_ID' NAMED as" \
"'${TAG_NAME}-IGW'."
flag=$?
fi


# Create Route Table
if [ $flag -eq 0 ]
then
	step="Create RT"
echo "Creating Route Table..."
ROUTE_TABLE_ID=$(aws ec2 create-route-table \
  --vpc-id $VPC_ID \
  --query 'RouteTable.{RouteTableId:RouteTableId}' \
  --output text \
  --region $AWS_REGION)
echo "  Route Table ID '$ROUTE_TABLE_ID' CREATED."
flag=$?
fi

#Add Name tag to Route table
if [ $flag -eq 0 ]
then
	step="Tag RT"
aws ec2 create-tags \
  --resources $ROUTE_TABLE_ID \
  --tags "Key=Name,Value=${TAG_NAME}-myRT" \
  --region $AWS_REGION
echo " Internet Gateway ID '$ROUTE_TABLE_ID' NAMED as" \
"'${ROUTE_TABLE_NAME}-myRT'."
flag=$?
fi

# Attach Internet gateway to your VPC
if [ $flag -eq 0 ]
then
	step="Attach IG"
aws ec2 attach-internet-gateway \
  --vpc-id $VPC_ID \
  --internet-gateway-id $IGW_ID \
  --region $AWS_REGION
echo "  Internet Gateway ID '$IGW_ID' ATTACHED to VPC ID '$VPC_ID'."
flag=$?
fi

# Create route to Internet Gateway
if [ $flag -eq 0 ]
then
	step="Open RT"
RESULT=$(aws ec2 create-route \
  --route-table-id $ROUTE_TABLE_ID \
  --destination-cidr-block 0.0.0.0/0 \
  --gateway-id $IGW_ID \
  --region $AWS_REGION)
echo "  Route to '0.0.0.0/0' via Internet Gateway ID '$IGW_ID' ADDED to" \
"Route Table ID '$ROUTE_TABLE_ID'."
flag=$?
fi


# Associate Public Subnet1 with Route Table
if [ $flag -eq 0 ]
then
	step="Attach subnet-1 to RT"
RESULT=$(aws ec2 associate-route-table  \
  --subnet-id $SUBNET1_PUBLIC_ID \
  --route-table-id $ROUTE_TABLE_ID \
  --region $AWS_REGION)
echo "  Public Subnet ID '$SUBNET1_PUBLIC_ID' ASSOCIATED with Route Table ID" \
"'$ROUTE_TABLE_ID'."
	flag=$?
fi

# Associate Public Subnet2 with Route Table
if [ $flag -eq 0 ]
then
	step="Attach Subnet 2 to RT"
RESULT=$(aws ec2 associate-route-table  \
  --subnet-id $SUBNET2_PUBLIC_ID \
  --route-table-id $ROUTE_TABLE_ID \
  --region $AWS_REGION)
echo "  Public Subnet ID '$SUBNET2_PUBLIC_ID' ASSOCIATED with Route Table ID" \
"'$ROUTE_TABLE_ID'."
	flag=$?
fi
# Associate Public Subnet3 with Route Table
if [ $flag -eq 0 ]
then
	step="Attach Subnet 3 to RT"
RESULT=$(aws ec2 associate-route-table  \
  --subnet-id $SUBNET3_PUBLIC_ID \
  --route-table-id $ROUTE_TABLE_ID \
  --region $AWS_REGION)
echo "  Public Subnet ID '$SUBNET3_PUBLIC_ID' ASSOCIATED with Route Table ID" \
"'$ROUTE_TABLE_ID'."
	flag=$?
fi

if [ $flag -eq 0 ]
then

	echo " ********* All resources created ***********"

	flag=0

	echo -e "\n"

	echo " ***************************************************************************** "
	echo " ******* Script eneded successfully. Exit status: $flag ********************** "
	echo " ***************************************************************************** "

	echo -e "\n"

	exit 0

else
	flag=1

	echo -e "\n"

	echo " ******************************************************************************* "
	echo " ******** Script ended with failure - Step: $step - Exit status: $flag ********* "
	echo " ******************************************************************************* "

	echo -e "\n"

	exit 1
fi
