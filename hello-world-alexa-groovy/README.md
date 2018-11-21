# Micronaut Example Alexa Usage

### Summary

This example shows how to use Micronaut to make a custom skill for Alexa.
The example is a simple hello world but there are some tasks involved to be able to run a full integration test and deploy it.

You'll need the following:

1) Account created at http://developer.amazon.com - it's separate from a Amazon Web Services account, but it's totally free.
2) An Amazon Web Services account. This can all be done at the free tier. Sign up at https://aws.amazon.com.
3) (Optional)ASK CLI (Alexa Skills Kit) see https://developer.amazon.com/docs/smapi/ask-cli-intro.html for more information.  

The rest of the tasks can be handled with the included Gradle tasks.
These are the steps needed to run the Lambda Alexa skill: 

1) uploading and creating the AWS Lambda function via Gradle
2) creating the Skill definition on the Amazon Developer Portal getting the Skill ID with the ARN in step 1
3) updating the lambda on aws to link to the alexa skill ID


### Creating the Skill on developer.amazon.com 
There are 2 ways now to create a skill on the developer portal:
 
 1) You can use the UI and manually create the intent schema, sample utterances, and slot types.
 2) You can use a JSON manifest file like here https://developer.amazon.com/docs/smapi/skill-manifest.html#sample-manifests and use the ASK CLI to deploy it
  
If you choose option 2, it eliminates a little back and forth because you can just put the ARN for your Lambda function into the manifest, then upload the manifest to create the skill on the portal without any other manual interaction.

Let's start with option 2. If you want to try the first choice, see this SETT Article from Object Computing: https://objectcomputing.com/resources/publications/sett/july-2016-building-alexa-skills-with-grails   

### Install the ASK CLI

Full instructions are here: https://developer.amazon.com/docs/smapi/quick-start-alexa-skills-kit-command-line-interface.html

#### Requirements

- Node.js 4.5 and NPM
- IAM User created with a role to execute the Lambda functions


### Commands to run
To install run:

`
$ npm install -g ask-cli
`

to setup (it will ask you to login):

`
$ ask init
`

you should now have a ~/.ask/cli-config file with access tokens inside.

#### Create the skill automatically on developer portal

Once you install the ASK CLI successfully you can now create the skill from the provided skill.json file - so much easier!
When you run the command the output should look something like below:

`
$ ask api create-skill --file src/resources/skill.json 
Skill created successfully.
Skill ID: amzn1.ask.skill.69aafd3f-1c78-4613-92a3-8588a955c7c6
`

Note the skill ID your output gives you. You'll have to enter that to link the lambda to your skill in the AWS console.

```html
fyi there is a service called SAM for AWS that allow you to use CloudFormation templates to deploy your whole serverless app - see https://github.com/awslabs/serverless-application-model for more information 
```

Now let's deploy to AWS Lambda:

```
$ ./gradlew deploy
Starting a Gradle Daemon, 2 busy and 10 incompatible and 1 stopped Daemons could not be reused, use --status for details

> Task :deploy
Function not found: arn:aws:lambda:us-east-1:057654311259:function:hello-world-alexa (Service: AWSLambda; Status Code: 404; Error Code: ResourceNotFoundException; Request ID: cdf1817e-ed16-11e8-9e9e-0f1e0998d006)
Creating function... hello-world-alexa
```


