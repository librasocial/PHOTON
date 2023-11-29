const AWS = require('aws-sdk');
const cognito = require('../../config/cognito');
const Env = require('../../Env');
const ApiError = require('../errors/handler');

module.exports = class Cognito {
    config = {
        region: 'ap-south-1',
    }
    clientId = Env.AWSClientId;

    cognitoIdentity;

    constructor() {
        // AWS.config.credentials = new AWS.CognitoIdentityCredentials({
        //     IdentityPoolId: "ap-south-1:eb7aefef-2561-43ff-b703-f9ad1ae01fa0",
        //     IdentityId: "ap-south-1:3b20c768-8433-4e93-af2a-f4f757d2e652",
        //     Logins: {
        //         'cognito-identity.amazonaws.com': 'eyJraWQiOiIyRHBKUjVDQ05BNllRXC9VU0oyWE9qV3RudXcxR1VsZnlsdUFxaTZrcm43ST0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiI4MWI3ZWFiYy1hYzg3LTQxZmUtYjY3NS0zYjQ5YjM0NjM2YmMiLCJjb2duaXRvOmdyb3VwcyI6WyJzc2YtY29nbml0b2FkbWludXNlcnMiXSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLmFwLXNvdXRoLTEuYW1hem9uYXdzLmNvbVwvYXAtc291dGgtMV9paWNaRlhwOU8iLCJjb2duaXRvOnVzZXJuYW1lIjoicmFrZXNoLWRldiIsIm9yaWdpbl9qdGkiOiIyOWQzYmZmZi1kNDM2LTRkNzQtYjk4OS02NjVjYThjYTgyNGQiLCJjb2duaXRvOnJvbGVzIjpbImFybjphd3M6aWFtOjoxMjUyNzcyMjg4NjI6cm9sZVwvc3NmLWRldi1jb2duaXRvYWRtaW51c2VyLXJvbGUiXSwiYXVkIjoiM3Y5c21iajF0ZHNnb3VrZmpmNGx0bmowN2MiLCJldmVudF9pZCI6IjQ2M2U2MjNlLWU5YTUtNGVhOC04ODdhLTg3Yzg5YzcwNTRlZSIsInRva2VuX3VzZSI6ImlkIiwiYXV0aF90aW1lIjoxNjc5ODU2MDExLCJleHAiOjE2Nzk5NDI0MTEsImlhdCI6MTY3OTg1NjAxMSwianRpIjoiNTMwMmQ3NGItNjJjMS00NDkxLTk0NTQtZmQzMTVhYmM0NzFjIn0.wtQ32buP2HrYD760B5kSmS5hTo8XRcE7JcVyKAkuw7AKJ7nYdcBLW_LlmKHhvQhyknO767A-ohlMZ4-fpKxhwkWEdntF7tf9WYBJ7eRKsojzjQNULMAGVs4HRkQZrO01DZb7rBi3N-aDcFoxbryQEfZUVZfTQ-Gpg83hLpWkQs0icL59lZpSJlfv0lrwrlNMDoPMy10XBHNCcJu8C11hccb1nE1J_gBp_cn92ClKBPYi0wuD2o3aVal7ZXGY8qwEgOsqo6QQsVZTr4Dd-N3iKPscjlk-mfv--ACDgdyCNBpN8H-n3ffeFyvpoN6quZHfzCyd1Df6mD-jC0HEzZZcaA'
        //     }
        // });
        AWS.config.update({
            credentials: new AWS.Credentials({
              accessKeyId: "AKIAR2KY2Z47MKN3N3FZ",
              secretAccessKey: "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",
              region: "ap-south-1"
            })
          });
        this.cognitoIdentity = new AWS.CognitoIdentityServiceProvider(this.config)
    }

    async adminCreateUser(params) {
        try {
            const data = await this.cognitoIdentity.adminCreateUser(params).promise()
            console.log(data);
            return data;
        } catch (e) {
            console.log(e);
            return false;
        }
    }

    async adminGetUser(username) {
        console.log(`adminGetUser -> ${username} `)
        try {
            const params = {
                "Username": username,
                "UserPoolId": 'ap-south-1_iicZFXp9O'
            }

            const data = await this.cognitoIdentity.adminGetUser(params).promise()
            console.log(data);
            return data;
        } catch (e) {
            console.log(e);
            return false;
        }
    }

    async adminSetUserPassword(params){
        try {
            const data = await this.cognitoIdentity.adminSetUserPassword(params).promise()
            console.log(`data - ${JSON.stringify(data)}`);
            return true;
        } catch (e) {
            console.log(e);
            return false;
        }
    }

    createUserParamGenerator(body) {
        const params = {}
        if (!body.Username) {
            throw ApiError.partialContent(`Require Username in body`, `Username`)
        }
        if (!body.TemporaryPassword) {
            throw ApiError.partialContent(`Require TemporaryPassword in body`, `TemporaryPassword`)
        }
        params.TemporaryPassword = body.TemporaryPassword
        const config = cognito.AdminCreateUser
        params.DesiredDeliveryMediums = config.DesiredDeliveryMediums
        params.MessageAction = config.MessageAction
        params.ForceAliasCreation = config.ForceAliasCreation
        params.Username = body.Username
        params.UserPoolId = config.UserPoolId
        params.UserAttributes = []
        if (!body.UserAttributes) body.UserAttributes = {}
        config.UserAttributes.forEach(element => {
            if (body.UserAttributes[element.name]) {
                params.UserAttributes.push({ Name: element.name, Value: body.UserAttributes[element.name] })
            }
            else {
                if (element.required)
                    throw ApiError.partialContent(`Require ${element.name} in body.UserAttributes`, `UserAttributes.${element.name}`)
            }
        });
        return params
    }

    
    adminSetUserParamGenerator(body) {
        const params = {}
        if (!body.Username) {
            throw ApiError.partialContent(`Require Username in body`, `Username`)
        }
        if (!body.Password) {
            throw ApiError.partialContent(`Require Password in body`, `Password`)
        }
        const config = cognito.AdminSetUserPassword

        params.Username = body.Username
        params.UserPoolId = 'ap-south-1_iicZFXp9O'
        params.Password = body.Password
        params.Permanent = config.Permanent
        return params
    }

    adminSetUserParamGenerator1(body) {
            const params = {}
            const config = cognito.AdminSetUserPassword
            params.Username = body.Username
            params.UserPoolId = 'ap-south-1_iicZFXp9O'
            params.Password = body.TemporaryPassword
            params.Permanent = config.Permanent
            return params
        }
}