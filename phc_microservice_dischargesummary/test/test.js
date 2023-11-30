/* eslint-disable */ 
const DischargeModel = require('../app/models/DischargeModel')
const chai = require('chai')
const chaiHttp = require('chai-http')
const app = require('../app')
const { describe, it } = require('mocha')
const should = chai.should()
chai.use(chaiHttp)

describe('DischargeSummary', () => {
  let historyId = ''
  let encounterId = ''
  let UHId = ''
  let encounterDate = ''

  before((done) => {
    DischargeModel.remove({}, (err) => {
      done()
    })
  })

  describe('/POST dischargesummary', () => {
    it('it should create a new dischargesummary', (done) => {
      const dischargesummary = {
        UHId: 'string',
        encounterId: 'string',
        encounterDate: '2022-05-17T06:01:06.779Z',
        deathDateTime: '2022-05-18T06:01:06.779Z',
        patientId: 'string',
        doctor: 'string',
        type: 'Sent Home',
        remarks: 'string',
        referTo: 'string',
        nextVisit: '2022-05-17T06:01:06.779Z'
      }

      chai
        .request(app)
        .post('/dischargesummary')
        .send(dischargesummary)
        .end((err, res) => {
          historyId = res.body._id
          UHId = res.body.UHId
          encounterId = res.body.encounterId
          encounterDate = res.body.encounterDate
          res.should.have.status(201)
          res.body.should.be.a('object')
          res.body.should.have.property('UHId')
          res.body.UHId.should.be.a('string')
          res.body.should.have.property('encounterId')
          res.body.encounterId.should.be.a('string')
          res.body.should.have.property('encounterDate')
          res.body.encounterDate.should.be.a('String')
          res.body.should.have.property('patientId')
          res.body.patientId.should.be.a('string')
          res.body.should.have.property('doctor')
          res.body.doctor.should.be.a('string')
          res.body.should.have.property('type')
          res.body.type.should.be.a('string')
          res.body.should.have.property('remarks')
          res.body.remarks.should.be.a('string')
          res.body.should.have.property('referTo')
          res.body.referTo.should.be.a('string')
          res.body.should.have.property('nextVisit')
          res.body.nextVisit.should.be.a('string')
          res.body.should.have.property('audit')
          res.body.audit.should.be.a('object')
          res.body.should.have.property('_id')
          res.body._id.should.be.a('string')
          done()
        })
    })
    it('it should not create a new dischargesummary if encounterDate is not of Date type', (done) => {
      chai
        .request(app)
        .post('/dischargesummary')
        .send({
          UHId: 'string',
          encounterId: 'string',
          encounterDate: 'checkkkkk',
          patientId: 'string',
          doctor: 'string',
          type: 'Sent Home',
          remarks: 'string',
          referTo: 'string',
          nextVisit: '2022-05-17T06:01:06.779Z'
        })
        .end((err, res) => {
          res.should.have.status(400)
          res.body.should.be.a('object')
          res.body.should.have.property('errors')
          res.body.errors.should.be.a('array')
          res.body.errors[0].should.have.property('field').eql('encounterDate')
          res.body.errors[0].should.have
            .property('message')
            .eql('Invalid value')
          done()
        })
    })
    it('it should not create a new dischargesummary if doctor is not of string type', (done) => {
      chai
        .request(app)
        .post('/dischargesummary')
        .send({
          UHId: 'string',
          encounterId: 'string',
          encounterDate: '2022-05-17T06:01:06.779Z',
          patientId: 'string',
          doctor: 1234,
          type: 'Sent Home',
          remarks: 'string',
          referTo: 'string',
          nextVisit: '2022-05-17T06:01:06.779Z'
        })
        .end((err, res) => {
          res.should.have.status(400)
          res.body.should.be.a('object')
          res.body.should.have.property('errors')
          res.body.errors.should.be.a('array')
          res.body.errors[0].should.have.property('field').eql('doctor')
          res.body.errors[0].should.have
            .property('message')
            .eql('Invalid value')
          done()
        })
    })
  })

  describe('/GET dischargesummary', () => {
    it('it should not fetch the dischargesummary', (done) => {
      chai
        .request(app)
        .get('/dischargesummary/6270ebd3768b9395e0111111')
        .end((err, res) => {
          res.should.have.status(404)
          res.body.should.be.a('object')
          res.body.should.have.property('errors')
          res.body.errors.should.be.a('array')
          res.body.errors[0].should.have.property('field').eql('historyId')
          res.body.errors[0].should.have
            .property('message')
            .eql('Discharge Summary not found for 6270ebd3768b9395e0111111')
          done()
        })
    })
    it('it should fetch the Discharge Summary', (done) => {
      chai
        .request(app)
        .get(`/dischargesummary/${historyId}`)
        .end((err, res) => {
          res.should.have.status(200)
          res.body.should.be.a('object')
          res.body.should.have.property('_id').eql(historyId)
          res.body.should.have.property('UHId')
          res.body.UHId.should.be.a('string')
          res.body.should.have.property('encounterId')
          res.body.encounterId.should.be.a('string')
          res.body.should.have.property('encounterDate')
          res.body.encounterDate.should.be.a('String')
          res.body.should.have.property('patientId')
          res.body.patientId.should.be.a('string')
          res.body.should.have.property('doctor')
          res.body.doctor.should.be.a('string')
          res.body.should.have.property('type')
          res.body.type.should.be.a('string')
          res.body.should.have.property('remarks')
          res.body.remarks.should.be.a('string')
          res.body.should.have.property('referTo')
          res.body.referTo.should.be.a('string')
          res.body.should.have.property('nextVisit')
          res.body.nextVisit.should.be.a('string')
          res.body.should.have.property('audit')
          res.body.audit.should.be.a('object')
          done()
        })
    })
  })

  describe('/PATCH dischargesummary', () => {
    it('it should not update the dischargesummary', (done) => {
      chai
        .request(app)
        .patch('/dischargesummary/6270ebd3768b9395e0111111')
        .send({
          type: 'Refer To',
          remarks: 'stringPATCHED',
          referTo: 'stringPATCHED'
        })
        .end((err, res) => {
          res.should.have.status(404)
          res.body.should.be.a('object')
          res.body.should.have.property('errors')
          res.body.errors.should.be.a('array')
          res.body.errors[0].should.have.property('field').eql('historyId')
          res.body.errors[0].should.have
            .property('message')
            .eql('Discharge Summary not found for 6270ebd3768b9395e0111111')
          done()
        })
    })
    it('it should update the dischargesummary', (done) => {
      chai
        .request(app)
        .patch(`/dischargesummary/${historyId}`)
        .send({
          type: 'Refer To',
          remarks: 'stringPATCHED',
          referTo: 'stringPATCHED'
        })
        .end((err, res) => {
          res.should.have.status(200)
          res.body.should.be.a('object')
          res.body.should.have.property('_id').eql(historyId)
          res.body.should.have.property('UHId')
          res.body.UHId.should.be.a('string')
          res.body.should.have.property('encounterId')
          res.body.encounterId.should.be.a('string')
          res.body.should.have.property('encounterDate')
          res.body.encounterDate.should.be.a('String')
          res.body.should.have.property('patientId')
          res.body.patientId.should.be.a('string')
          res.body.should.have.property('doctor')
          res.body.doctor.should.be.a('string')
          res.body.should.have.property('type')
          res.body.type.should.be.a('string')
          res.body.should.have.property('remarks')
          res.body.remarks.should.be.a('string')
          res.body.should.have.property('referTo')
          res.body.referTo.should.be.a('string')
          res.body.should.have.property('nextVisit')
          res.body.nextVisit.should.be.a('string')
          res.body.should.have.property('audit')
          res.body.audit.should.be.a('object')
          done()
        })
    })
  })

  describe('/GET filter dischargesummary', () => {
    it('it should filter the dischargesummary if page, size and encounterId is provided', (done) => {
      chai
        .request(app)
        .get(
					`/dischargesummary/filter?page=1&size=2&encounterId=${encounterId}`
        )
        .end((err, res) => {
          res.should.have.status(200)
          res.body.should.be.a('object')
          res.body.should.have.property('meta')
          res.body.meta.should.be.a('object')
          res.body.meta.should.have.property('totalPages')
          res.body.meta.totalPages.should.be.a('number')
          res.body.meta.should.have.property('totalElements')
          res.body.meta.totalElements.should.be.a('number')
          res.body.meta.should.have.property('size')
          res.body.meta.size.should.be.a('number')
          res.body.meta.should.have.property('number')
          res.body.meta.number.should.be.a('number')
          res.body.should.have.property('data')
          res.body.data.should.be.a('array')
          res.body.data[0].should.have.property('UHId')
          res.body.data[0].UHId.should.be.a('string')
          res.body.data[0].should.have.property('encounterId')
          res.body.data[0].encounterId.should.be.a('string')
          res.body.data[0].should.have.property('patientId')
          res.body.data[0].patientId.should.be.a('string')
          res.body.data[0].should.have.property('encounterDate')
          res.body.data[0].encounterDate.should.be.a('String')
          res.body.data[0].should.have.property('doctor')
          res.body.data[0].doctor.should.be.a('string')
          res.body.data[0].should.have.property('type')
          res.body.data[0].type.should.be.a('string')
          res.body.data[0].should.have.property('remarks')
          res.body.data[0].remarks.should.be.a('string')
          res.body.data[0].should.have.property('referTo')
          res.body.data[0].referTo.should.be.a('string')
          res.body.data[0].should.have.property('nextVisit')
          res.body.data[0].nextVisit.should.be.a('string')
          res.body.data[0].should.have.property('audit')
          res.body.data[0].audit.should.be.a('object')
          done()
        })
    })
    it('it should filter the dischargesummary if page, size and UHId is provided', (done) => {
      chai
        .request(app)
        .get(`/dischargesummary/filter?page=1&size=2&UHId=${UHId}`)
        .end((err, res) => {
          res.should.have.status(200)
          res.body.should.be.a('object')
          res.body.should.have.property('meta')
          res.body.meta.should.be.a('object')
          res.body.meta.should.have.property('totalPages')
          res.body.meta.totalPages.should.be.a('number')
          res.body.meta.should.have.property('totalElements')
          res.body.meta.totalElements.should.be.a('number')
          res.body.meta.should.have.property('size')
          res.body.meta.size.should.be.a('number')
          res.body.meta.should.have.property('number')
          res.body.meta.number.should.be.a('number')
          res.body.should.have.property('data')
          res.body.data.should.be.a('array')
          res.body.data[0].should.have.property('UHId')
          res.body.data[0].UHId.should.be.a('string')
          res.body.data[0].should.have.property('encounterId')
          res.body.data[0].encounterId.should.be.a('string')
          res.body.data[0].should.have.property('patientId')
          res.body.data[0].patientId.should.be.a('string')
          res.body.data[0].should.have.property('encounterDate')
          res.body.data[0].encounterDate.should.be.a('String')
          res.body.data[0].should.have.property('doctor')
          res.body.data[0].doctor.should.be.a('string')
          res.body.data[0].should.have.property('type')
          res.body.data[0].type.should.be.a('string')
          res.body.data[0].should.have.property('remarks')
          res.body.data[0].remarks.should.be.a('string')
          res.body.data[0].should.have.property('referTo')
          res.body.data[0].referTo.should.be.a('string')
          res.body.data[0].should.have.property('nextVisit')
          res.body.data[0].nextVisit.should.be.a('string')
          res.body.data[0].should.have.property('audit')
          res.body.data[0].audit.should.be.a('object')
          done()
        })
    })
    it('it should filter the dischargesummary if page, size and encounterDate is provided', (done) => {
      chai
        .request(app)
        .get(
					`/dischargesummary/filter?page=1&size=2&encounterDate=${encounterDate}`
        )
        .end((err, res) => {
          res.should.have.status(200)
          res.body.should.be.a('object')
          res.body.should.have.property('meta')
          res.body.meta.should.be.a('object')
          res.body.meta.should.have.property('totalPages')
          res.body.meta.totalPages.should.be.a('number')
          res.body.meta.should.have.property('totalElements')
          res.body.meta.totalElements.should.be.a('number')
          res.body.meta.should.have.property('size')
          res.body.meta.size.should.be.a('number')
          res.body.meta.should.have.property('number')
          res.body.meta.number.should.be.a('number')
          res.body.should.have.property('data')
          res.body.data.should.be.a('array')
          res.body.data[0].should.have.property('UHId')
          res.body.data[0].UHId.should.be.a('string')
          res.body.data[0].should.have.property('encounterId')
          res.body.data[0].encounterId.should.be.a('string')
          res.body.data[0].should.have.property('patientId')
          res.body.data[0].patientId.should.be.a('string')
          res.body.data[0].should.have.property('encounterDate')
          res.body.data[0].encounterDate.should.be.a('String')
          res.body.data[0].should.have.property('doctor')
          res.body.data[0].doctor.should.be.a('string')
          res.body.data[0].should.have.property('type')
          res.body.data[0].type.should.be.a('string')
          res.body.data[0].should.have.property('remarks')
          res.body.data[0].remarks.should.be.a('string')
          res.body.data[0].should.have.property('referTo')
          res.body.data[0].referTo.should.be.a('string')
          res.body.data[0].should.have.property('nextVisit')
          res.body.data[0].nextVisit.should.be.a('string')
          res.body.data[0].should.have.property('audit')
          res.body.data[0].audit.should.be.a('object')
          done()
        })
    })
  })

  after((done) => {
    DischargeModel.remove(() => {
      done()
    })
  })
})
