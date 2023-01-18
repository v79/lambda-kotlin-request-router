
# lambda-kotlin-request-router

A fork of [moia-oss/lambda-kotlin-request-router](https://github.com/moia-oss/lambda-kotlin-request-router) to help me understand some of the errors I am getting with that project. The original seems to be the only Kotlin (or Java) library that provides a routing function for AWS Lambda functions through an API Gateway Proxy+.

In particular, issues I am facing:

- Can only get Cloudwatch logging when I used `println`; using a SL4J logger facade produces no logs at all
- I can't move functions into separate classes/objects, possibly because of [this change?](https://github.com/moia-oss/lambda-kotlin-request-router/pull/148)

Other changes I'd like to make:

- Use lower-case for the action names (`get()` rather than `GET()`) - purely a personal preference
- Use kotlinx.serialization rather than guava for serialization/deserialization
- Move to the `gradle.kts` kotlin format for build files

General observations:

- The API Gateway Proxy+ request requirements are quite complicated and setting up tests has been very hit-and-miss

