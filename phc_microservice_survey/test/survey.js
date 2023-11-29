let mongoose = require("mongoose");
let chai = require('chai');
let chaiHttp = require('chai-http');
let app = require('../app');
const SurveyResponseModel = require("../app/models/survey-response");
let should = chai.should();
const { loadData } = require('./helper');
const SurveyMasterModel = require("../app/models/survey-master");

chai.use(chaiHttp);
describe('Surveys', () => {
  before((done) => {
    const filePath1 = './test/survey-master-entry-1.json';
    const filePath2 = './test/survey-master-entry-2.json';

    SurveyMasterModel.remove(() => {
      Promise.all([loadData(filePath1), loadData(filePath2)]).then(() => {
        SurveyResponseModel.remove(() => {
          done()
        }
        )
      }, (err) => {
        console.log(err)
      });
    });
  });
  /*
    * Test the /POST route
    */
  describe('/POST survey-response', () => {

  });

  describe('/GET survey-response', () => {
    it('it should not GET a survey if surveyId is not correct ', (done) => {

      chai.request(app)
        .get('/surveys/2/responses')
        .end((err, res) => {
          res.should.have.status(206);
          res.body.should.be.a('object');
          res.body.should.have.property('path').eql('surveyId');
          res.body.should.have.property('kind').eql("ObjectId");
          done();
        });
    });

  });

  describe('/GET surveys/filter', () => {

  })

  describe('/GET surveys/{surveyId}', () => {
    const surveyId = "61d40fd0380bcaefcc179fd6"
    it('it sould not get if wrong surveyId', (done) => {

      chai.request(app)
        .get(`/surveys/1234`)
        .end((err, res) => {
          res.should.have.status(404);
          done();
        });
    });

  })

  after((done) => {
    SurveyMasterModel.remove(()=>{
      SurveyResponseModel.remove(()=>{
        done()
      });
    });
  });

});
