# Gaia

Integration test runner. 
Test are implemented as Docker images and Gaia runs them on K8s cluster using
user define configuration.


It is implemented using Serverless architecture on AWS.

## Deplyment

1. Create webapp

```bash
ng build --prod --aot
```

Configure S3 bucket for website hosting

```bash
aws s3 website s3://gaia.klangner.com/ --index-document index.html --error-document error.html
```

Upload frontend app

```bash
aws s3 cp ./dist s3://gaia.klangner.com --recursive --acl public-read
```

