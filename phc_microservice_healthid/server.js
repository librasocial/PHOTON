const loadAWSJSONSecretsIntoENV = require("./config/aws-secrets");
var http = require("http");

if (process.env.profile !== "test") {
    let secretName = process.env.secret;
    let region = process.env.region;
    let profile = process.env.profile;
  loadAWSJSONSecretsIntoENV(region, secretName, console.log)
    .then(() => {
      const app = require("./app");
      const port = 81; // Setting an port for this application

      app.set("port", port);

      /**
       * Create HTTP server.
       */
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
    .catch((err) => {
      console.log("error occurred while fetching secrets", err);
    });
} else {
  const app = require("./app");
  const port = 81; // Setting an port for this application

  app.set("port", port);

  /**
   * Create HTTP server.
   */
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
