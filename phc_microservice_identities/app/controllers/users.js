const { patch } = require("../../routes/routes");
const ApiError = require("../errors/handler");
const Cognito = require("../helpers/cognito.service");

module.exports = {
    async store(req, res, next) {
        try {
            let cognitoService = new Cognito()
            let params = cognitoService.createUserParamGenerator(req.body)
            console.log(params);
            const cognitoResponse = await cognitoService.adminCreateUser(params)
            console.log(`cognitoResponse :${cognitoResponse}`)
            if(cognitoResponse){
            params = cognitoService.adminSetUserParamGenerator1(req.body)
            const passwordResetResponse = await cognitoService.adminSetUserPassword(params)
            console.log(`passwordResetResponse :${passwordResetResponse}`)
            return res.send(true)
            }
            return res.send(false)

        } catch (err) {
        console.log("Entering into catch block")
            console.log(err);
            next(err)
            return res.send(false)
        }
    },

    async fetch(req, res, next) {
        try {
            console.log(`req -> ${req}`)
            const IdentityId = req.params.IdentityId
            let cognitoService = new Cognito()
            const cognitoResponse = await cognitoService.adminGetUser(IdentityId)
            res.send(cognitoResponse)
        } catch (err) {
            console.log(err);
            next(err)
            return
        }
    },

    async patch(req, res, next) {
        try {
            let cognitoService = new Cognito()
            const params = cognitoService.adminSetUserParamGenerator(req.body)
            const cognitoResponse = await cognitoService.adminSetUserPassword(params)
            res.send(cognitoResponse)
        } catch (err) {
            console.log(err);
            next(err)
            return
        }
    },
}



