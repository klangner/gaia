# Gaia Frontend app

## Deplyment

1. Create webapp

```bashd --prod
ng buil --aot
```

Configure S3 bucket for website hosting

```bash
aws s3 website s3://gaia.klangner.com/ --index-document index.html --error-document error.html
```

Upload frontend app

```bash
aws s3 cp ./dist s3://gaia.klangner.com --recursive --acl public-read
```
