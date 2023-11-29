process.env.profile = "test";

let chai = require("chai");
let chaiHttp = require("chai-http");
const { loadData } = require("./helper");
let app = require("../app");
let should = chai.should();
const HealthIdModel = require("../app/models/health-ids");

chai.use(chaiHttp);
const patchHealthIdFilePath = "./test/patch-health-id.json";
let patchHealthIdData = null;

describe("HealthIds", () => {
  let healthId;
  before((done) => {
    try {
      loadData(patchHealthIdFilePath).then(
        (fileData) => {
          patchHealthIdData = fileData;
          HealthIdModel.remove(
            () => {
              done();
            },
            (err) => {
              console.log(err);
              console.log("cannot load test files, terminating tests");
            }
          );
        },
        (err) => {
          console.log(err);
          console.log("cannot load test files, terminating tests");
        }
      );
    } catch (e) {
      console.log(e);
    }
  });
  /*
   * Test the /POST route
   */
  describe("/POST healthIds", () => {
    it("it should not POST a HealthId without citizenId feild", (done) => {
      let initialization = {
        staffId: "string",
      };
      chai
        .request(app)
        .post("/healthIds")
        .send(initialization)
        .end((err, res) => {
          res.should.have.status(206);
          res.body.should.be.a("object");
          res.body.should.have.property("errors");
          res.body.errors[0].should.have.property("field").eql("citizenId");
          done();
        });
    });

    it("it should not POST a HealthId without staffId feild", (done) => {
      let initialization = {
        citizenId: "string",
      };
      chai
        .request(app)
        .post("/healthIds")
        .send(initialization)
        .end((err, res) => {
          res.should.have.status(206);
          res.body.should.be.a("object");
          res.body.should.have.property("errors");
          res.body.errors[0].should.have.property("field").eql("staffId");
          done();
        });
    });

    it("give successful response on correct data ", (done) => {
      let initialization = {
        citizenId: "string",
        staffId: "string",
      };
      chai
        .request(app)
        .post("/healthIds")
        .send(initialization)
        .end((err, res) => {
          healthId = res.body.id;
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("msg").eql("successful");
          done();
        });
    });
  });

  // describe('/PATCH healthIds', () => {
  //     it('it should not PATCH if success object do not have generatedTimestamp in success for GENERATED', (done) => {
  //         let payload = patchHealthIdData[0].testObject
  //         chai.request(app)
  //             .patch(`/healthIds/${healthId}`)
  //             .send(payload)
  //             .end((err, res) => {
  //                 res.should.have.status(206);
  //                 res.body.should.be.a('object');
  //                 res.body.should.have.property('errors');
  //                 res.body.errors[0].should.have.property('field', 'success.generatedTimeStamp');
  //                 done();
  //             });
  //     });
  //     it('it should not PATCH if object do not have timestamp in body for GENERATED', (done) => {
  //         let payload = patchHealthIdData[1].testObject
  //         chai.request(app)
  //             .patch(`/healthIds/${healthId}`)
  //             .send(payload)
  //             .end((err, res) => {
  //                 res.should.have.status(206);
  //                 res.body.should.be.a('object');
  //                 res.body.should.have.property('errors');
  //                 res.body.errors[0].should.have.property('field', 'timestamp');
  //                 done();
  //             });
  //     });
  //     it('it should not PATCH if object do not have timestamp in body for HANDEDOVER', (done) => {
  //         let payload = patchHealthIdData[2].testObject
  //         chai.request(app)
  //             .patch(`/healthIds/${healthId}`)
  //             .send(payload)
  //             .end((err, res) => {
  //                 res.should.have.status(206);
  //                 res.body.should.be.a('object');
  //                 res.body.should.have.property('errors');
  //                 res.body.errors[0].should.have.property('field', 'timestamp');
  //                 done();
  //             });
  //     });
  //     it('it should not PATCH if object do not have timestamp in body for PRINTED', (done) => {
  //         let payload = patchHealthIdData[3].testObject
  //         chai.request(app)
  //             .patch(`/healthIds/${healthId}`)
  //             .send(payload)
  //             .end((err, res) => {
  //                 res.should.have.status(206);
  //                 res.body.should.be.a('object');
  //                 res.body.should.have.property('errors');
  //                 res.body.errors[0].should.have.property('field', 'timestamp');
  //                 done();
  //             });
  //     });

  // })

  describe("/POST healthIds/filter", () => {
    it("it should not GET with if invalid or no value for skip", (done) => {
      chai
        .request(app)
        .post("/healthIds/filter?size=4d&page=1")
        .send({
          staffId: [],
          gender: [],
        })
        .end((err, res) => {
          res.should.have.status(206);
          res.body.should.be.a("object");
          res.body.should.have.property("errors");
          res.body.errors[0].should.have.property("field").eql("size");
          done();
        });
    });

    it("it should not GET with if invalid or no value for page", (done) => {
      chai
        .request(app)
        .post("/healthIds/filter?size=4")
        .send({
          staffId: [],
          gender: [],
        })
        .end((err, res) => {
          res.should.have.status(206);
          res.body.should.be.a("object");
          res.body.should.have.property("errors");
          res.body.errors[0].should.have.property("field").eql("page");
          done();
        });
    });
  });

  describe("/GET healthIds/{healthId}", () => {
    it("it should not GET for invalid healthId", (done) => {
      chai
        .request(app)
        .get(`/healthIds/healthId`)
        .end((err, res) => {
          res.should.have.status(206);
          res.body.should.be.a("object");
          res.body.should.have.property("errors");
          res.body.errors[0].should.have.property("field").eql("_id");
          done();
        });
    });

    it("it should not GET if healthId do not exist", (done) => {
      chai
        .request(app)
        .get(`/healthIds/61d7eb9de8169666d428b046`)
        .end((err, res) => {
          res.should.have.status(404);
          res.body.should.be.a("object");
          res.body.should.have.property("errors");
          res.body.errors[0].should.have.property("field").eql("_id");
          done();
        });
    });

    it("it should GET for correct values", (done) => {
      chai
        .request(app)
        .get(`/healthIds/${healthId}`)
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("_id").eql(healthId);
          res.body.should.have.property("status");
          res.body.should.have.property("citizenId");
          res.body.should.have.property("staffId");
          done();
        });
    });
  });

  after((done) => {
    HealthIdModel.remove(() => {
      done();
    });
  });
});
