const loadAWSJSONSecretsIntoENV = require("./config/aws-secrets.js");
const region = "ap-south-1";
const secretName = "ssf-dev-secret";

var http = require("http");

if (process.env.NODE_ENV == "prod") {
  loadAWSJSONSecretsIntoENV(region, secretName, console.log)
    .then(() => {
      const app = require("./app");
      const port = 81; // Setting an port for this application

      app.set("port", port);

      // Create HTTP server.

      var server = http.createServer(app);

      // Starting server using listen function
      server.listen(port, function (err) {
        if (err) {
          console.log("Error while starting server");
        } else {
          console.log("Server has been started at " + port);
        }
        app.get("/", function (req, res) {
          res.send("we are at the root route of our server");
        });
      });
    })
    .catch((error) => {
      console.log("error occurred while fetching the secrets", error);
    });
} else {
  const port = 81; // Setting an port for this application
  const app = require("./app");
  app.set("port", port);

  // Create HTTP server.

  var server = http.createServer(app);

  // Starting server using listen function
  server.listen(port, function (err) {
    if (err) {
      console.log("Error while starting server");
    } else {
      console.log("Server has been started at " + port);
    }
    app.get("/", function (req, res) {
      res.send("we are at the root route of our server");
    });
  });
}
