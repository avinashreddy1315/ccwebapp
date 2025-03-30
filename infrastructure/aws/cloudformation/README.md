# csye6225-Summer 2019

* Anurag Dhar dhar.a@husky.neu.edu 001443316
* Avi Tiwari tiwari.av@husky.neu.edu 001449863
* Soumavo Guria guria.s@husky.neu.edu 001495565

### Instructions to create VPC, Subnets, Internet Gateways, Route Tables in the AWS using cloud formation stacks

#### Files

  * Shell scripts : csye6225-aws-cf-create-stack.sh, csye6225-aws-cf-terminate-stack-sh
  * Parameter file : parameter.json
  * Template : csye6225-cf-networking.json

  Parameters taken as input are:-
  * Stack Name and the cidrBlock for VPC

  * Other Parameters such as the region and AvailabilityZone are taken from the config files
  * cidrBlock for the subnets are set based on the cidrBlock of VPC and distributing as cidrBits

### Instruction to run the shell scripts and create the Stack

  * Go to the terminal in the specific directory ~/csye6225/dev/ccwebapp/infrastructure/aws/cloudformation
  * Run the shell script by typing the command $ sh csye6225-aws-cf-create-stack.sh
  * Provide the stack name and the cidrBlock for VPC
  * Above command creates stack in the AWS

  ### Instruction to run the shell scripts and delete the Stack

    * Go to the terminal in the specific directory ~/csye6225/dev/ccwebapp/infrastructure/aws/cloudformation
    * Run the shell script by typing the command $ sh csye6225-aws-cf-terminate-stack.sh
    * Provide the stack name to be deleted
    * Above command deletes stack in the AWS
