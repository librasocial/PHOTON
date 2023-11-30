const express = require("express"); // Importing express module
// const env = require('./env');
const bodyParser = require("body-parser");
const mongoose = require("mongoose");
const actuator = require("express-actuator");
const router = require("./routes/routes");
const apiErrorHandler = require("./app/middlewares/api-error-handler");
const appLogger = require("./config/logger.js");
const loadAWSJSONSecretsIntoENV = require('./config/aws-secrets');

(async () => {
  let DB_LINK_MONGO;
  console.log(`profile : ${process.env.profile}`);

    if (process.env.NODE_ENV === 'test') {
      console.log("running in test environment");
      const { MongoMemoryServer } = require('mongodb-memory-server');
      const mongoServer = await MongoMemoryServer.create()
      DB_LINK_MONGO = mongoServer.getUri()
       mongoose
          .connect(DB_LINK_MONGO, {
            useNewUrlParser: true,
            useUnifiedTopology: true,
          })
          .then(() => {
            console.log("Connected to test database!");
          })
          .catch(() => {
            console.log("Test Connection failed!");
          });
    } else {
    console.log("Entering into else block");
    console.log(`Running in ${process.env.profile} environment`);
    let secretName = process.env.secret;
    let region = process.env.region;
    let profile = process.env.profile;
    console.log(`profile : ${profile} , secretName :${secretName}, region : ${region}`);
    loadAWSJSONSecretsIntoENV(region, secretName, console.log)
    .then(() => {
        console.log(`DB_LINK_MONGO : ${process.env.DB_LINK_MONGO}`)
        DB_LINK_MONGO = process.env.DB_LINK_MONGO;
        DB_LINK_MONGO = DB_LINK_MONGO.concat('/',profile).concat('-','health-ids')
        console.log(`DB_LINK_MONGO : ${DB_LINK_MONGO}`)
        mongoose
         .connect(DB_LINK_MONGO, {
          useNewUrlParser: true,
          useUnifiedTopology: true,
        })
        .then(() => {
          console.log("Connected to database!");
        })
        .catch(() => {
          console.log("Connection failed!");
        });
    });
}

})().catch((e) => { console.log(e) })

const app = express(); // Creating an express object

const actuatorOptions = {
  basePath: "/healthIds", // It will set /management/info instead of /info
};

app.use(actuator(actuatorOptions));

app.use(bodyParser.json({ extended: true }));
app.use(bodyParser.urlencoded({ extended: true }));

app.use("/healthIds", router);

app.use(apiErrorHandler);

module.exports = app;
