# Micronaut & AWS Metadata/Parameter Store Demo

The sample project contains three files of interest:

1. `src/main/groovy/com/example/bootstrap/Bootstrap.groovy` - access and log out AWS metadata from the `ServiceStartedEvent`
2. `src/main/resources/bootstrap.yml` - configures distributed configuration from Parameter Store
3. `src/main/groovy/com/example/controllers/AmazonConfigurationController.groovy` - binds to a Parameter Store value and renders it to the controller response

Note that `Bootstrap` and `AmazonConfigurationController` require an EC2 environment to be enabled.