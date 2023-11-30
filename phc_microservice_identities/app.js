const express = require('express'); // Importing express module
// const env = require('./env');
const bodyParser = require("body-parser");
const actuator = require('express-actuator');
const router = require('./routes/routes');
const apiErrorHandler = require('./app/middlewares/api-error-handler');

(async () => {

    if (process.env.NODE_ENV === 'test') {
        console.log("running in test environment")
    }
})().catch((e) => { console.log(e) })

const app = express(); // Creating an express object

app.use(actuator());

app.use(bodyParser.json({ extended: true }));
app.use(bodyParser.urlencoded({ extended: true }));

app.use('/', router);

app.use(apiErrorHandler)

module.exports = app