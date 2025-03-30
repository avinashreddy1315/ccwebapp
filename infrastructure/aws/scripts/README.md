# csye6225-Summer 2019

*Soumavo Guria guria.s@husky.neu.edu 001495565
* Anurag Dhar dhar.a@husky.neu.edu 001443316
* Avi Tiwari tiwari.av@husky.neu.edu 001449863


### Instructions to create VPC, Subnets, Internet Gateways, Route Tables in the AWS using AWS CLI

#### Files

  * Shell scripts : csye6225-aws-networking-setup.sh, csye6225-aws-networking-teardown.sh

  Parameters taken as input are:-
  * Tag Name, cidrBlock, AvailabilityZone for VPC and subnets

### Instruction to run the shell scripts and create the Networking Resources

  * Go to the terminal in the specific directory ~/csye6225/dev/ccwebapp/infrastructure/aws/scripts
  * Run the shell script by typing the command $ sh csye6225-aws-networking-setup.sh
  * Provide the tag name and the cidrBlock and AvailabilityZone for VPC
  * Provide the cidrBlock and AvailabilityZone for subnets
  * Above command creates networking resources in the AWS

  ### Instruction to run the shell scripts and delete the Networking Resources

    * Go to the terminal in the specific directory ~/csye6225/dev/ccwebapp/infrastructure/aws/scripts
    * Run the shell script by typing the command $ sh csye6225-aws-networking-teardown.sh
    * Provide the VPC tag name to be deleted
    * Above command deletes netwroking resources in the AWS
