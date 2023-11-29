const Env = require("../Env")

const Config = {
    keys: {

    },
    AdminCreateUser: {
        /**
         *  Configure the below according to requirements
         *  refer - https://docs.aws.amazon.com/cognito-user-identity-pools/latest/APIReference/API_AdminCreateUser.html
         *  refer to above mentioned url before makin any changes
         */
        UserPoolId: 'ap-south-1_iicZFXp9O',
        DesiredDeliveryMediums: ['EMAIL'],
        MessageAction: "SUPPRESS",
        ForceAliasCreation: false,
        UserAttributes: [
            { name: 'custom:membershipId', required: true },
            { name: 'email', required: false },
            { name: 'phone_number' }
        ]
    },
    AdminSetUserPassword: {
        UserPoolId: 'ap-south-1_iicZFXp9O',
        Permanent: true
    }
}
module.exports = Config
