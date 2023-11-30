const express = require("express");
const bodyParser = require("body-parser");
const mongoose = require("mongoose");
const actuator = require("express-actuator");
const router = require("./routes/routes");
const apiErrorHandler = require("./app/middlewares/api-error-handler");
const appLogger = require("./config/logger.js");
const loadAWSJSONSecretsIntoENV = require("./config/aws-secrets.js");

(async () => {
  let DB_LINK;
  console.log(process.env.profile);

    if (process.env.NODE_ENV === 'test') {
      console.log("running in test environment");
      const { MongoMemoryServer } = require('mongodb-memory-server');
      const mongoServer = await MongoMemoryServer.create()
      DB_LINK = mongoServer.getUri()
       mongoose
          .connect(DB_LINK, {
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
    console.log(`Running in ${process.env.profile} environment`);
    let secretName = process.env.secret;
    let region = process.env.region;
    let profile = process.env.profile;
    console.log(`secretName: ${secretName} , region: ${region} , profile: ${profile}`);
    loadAWSJSONSecretsIntoENV(region, secretName, console.log)
    .then(() => {
        DB_LINK = process.env.DB_LINK_MONGO;
        DB_LINK = DB_LINK.concat('/',profile).concat('-','allergies')
        console.log(`DB_LINK : ${DB_LINK}`)
        mongoose
         .connect(DB_LINK, {
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
	basePath: "/allergies", // It will set /management/info instead of /info
};

app.use(actuator(actuatorOptions));

app.use(bodyParser.json({ extended: true }));
app.use(bodyParser.urlencoded({ extended: true }));

app.use("/allergies", router);

app.use(apiErrorHandler);

module.exports = app;
