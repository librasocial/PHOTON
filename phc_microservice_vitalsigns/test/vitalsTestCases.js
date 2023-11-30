const mongoose = require("mongoose");
const VitalSignsModel = require("../app/models/vitalSigns");
const chai = require("chai");
const chaiHttp = require("chai-http");
const app = require("../app");
const { describe, it } = require("mocha");
const should = chai.should();
chai.use(chaiHttp);

describe("VitalSigns", () => {
	let vitalSignId = "";

	before((done) => {
		VitalSignsModel.remove({}, (err) => {
			done();
		});
	});

	describe("/POST vitals", () => {
		it("it should create a new vital", (done) => {
			chai
				.request(app)
				.post("/vitalsigns")
				.send({
					citizenId: "string",
					patientId: "STRING",
					UHId: "string",
					encounterId: "string",
					encounterDate: "2022-04-18T09:10:05.197Z",
					medicalSigns: [
						{
							signName: "string",
							signValue: "value",
							_id: "625d2aeda8e7a7d6a45d720f",
						},
					],
					audit: {
						dateCreated: "2022-04-18T09:10:05.197Z",
						createdBy: "rakesh-dev",
						dateModified: "2022-04-18T09:10:05.197Z",
						modifiedBy: "rakesh-dev",
					},
				})
				.end((err, res) => {
					vitalSignId = res.body._id;
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.citizenId.should.be.a("string");
					res.body.patientId.should.be.a("string");
					res.body.UHId.should.be.a("string");
					res.body.encounterId.should.be.a("string");
					res.body.medicalSigns.should.be.a("array");
					res.body.should.have.property("audit");
					res.body.audit.should.have.property("dateCreated");
					res.body.audit.should.have.property("createdBy");
					res.body.audit.should.have.property("dateModified");
					res.body.audit.should.have.property("modifiedBy");
					done();
				});
		});
		it("it should not create new vital if UHId is not String", (done) => {
			chai
				.request(app)
				.post("/vitalsigns")
				.send({
					citizenId: "string",
					patientId: "STRING",
					UHId: 12345,
					encounterId: "string",
					medicalSigns: [
						{
							signName: "string",
							signValue: "value",
						},
					],
					audit: {
						dateCreated: "2022-04-18T09:10:05.197Z",
						createdBy: "rakesh-dev",
						dateModified: "2022-04-18T09:10:05.197Z",
						modifiedBy: "rakesh-dev",
					},
				})
				.end((err, res) => {
					res.should.have.status(400);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors[0].should.have.property("field").eql("UHId");
					done();
				});
		});
		it("it should not create new vital if patientId is not String", (done) => {
			chai
				.request(app)
				.post("/vitalsigns")
				.send({
					citizenId: "string",
					patientId: 12345,
					UHId: "string",
					encounterId: "string",
					medicalSigns: [
						{
							signName: "string",
							signValue: "value",
						},
					],
					audit: {
						dateCreated: "2022-04-18T09:10:05.197Z",
						createdBy: "rakesh-dev",
						dateModified: "2022-04-18T09:10:05.197Z",
						modifiedBy: "rakesh-dev",
					},
				})
				.end((err, res) => {
					res.should.have.status(400);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors[0].should.have.property("field").eql("patientId");
					done();
				});
		});
	});

	describe("/GET/vitalsigns/filter vitals", () => {
		it("it should get vitals if encounterId is provided", (done) => {
			chai
				.request(app)
				.get("/vitalsigns/filter?encounterId=string&page=1")
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("meta");
					res.body.meta.should.have.property("totalPages");
					res.body.meta.should.have.property("totalElements");
					res.body.meta.should.have.property("number");
					res.body.meta.should.have.property("size");
					res.body.should.have.property("data");
					res.body.data.should.be.a("array");
					res.body.data[0].should.have.property("citizenId");
					res.body.data[0].should.have.property("patientId");
					res.body.data[0].should.have.property("UHId");
					res.body.data[0].should.have.property("encounterId");
					res.body.data[0].should.have.property("encounterDate");
					res.body.data[0].should.have.property("medicalSigns");
					res.body.data[0].should.have.property("audit");
					res.body.data[0].audit.should.have.property("dateCreated");
					res.body.data[0].audit.should.have.property("createdBy");
					res.body.data[0].audit.should.have.property("dateModified");
					res.body.data[0].audit.should.have.property("modifiedBy");
					done();
				});
		});
		it("it should get vitals if encounterDate is provided", (done) => {
			chai
				.request(app)
				.get("/vitalsigns/filter?encounterDate=2022-04-18&page=1")
				.end((err, res) => {
					// console.log(res.body);
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("meta");
					res.body.meta.should.have.property("totalPages");
					res.body.meta.should.have.property("totalElements");
					res.body.meta.should.have.property("number");
					res.body.meta.should.have.property("size");
					res.body.should.have.property("data");
					res.body.data.should.be.a("array");
					res.body.data[0].should.have.property("citizenId");
					res.body.data[0].should.have.property("patientId");
					res.body.data[0].should.have.property("UHId");
					res.body.data[0].should.have.property("encounterId");
					res.body.data[0].should.have.property("encounterDate");
					res.body.data[0].should.have.property("medicalSigns");
					res.body.data[0].should.have.property("audit");
					res.body.data[0].audit.should.have.property("dateCreated");
					res.body.data[0].audit.should.have.property("createdBy");
					res.body.data[0].audit.should.have.property("dateModified");
					res.body.data[0].audit.should.have.property("modifiedBy");
					done();
				});
		});
		it("it should get vitals if UHId is provided", (done) => {
			chai
				.request(app)
				.get("/vitalsigns/filter?UHId=string&page=1")
				.end((err, res) => {
					// console.log(res.body);
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("meta");
					res.body.meta.should.have.property("totalPages");
					res.body.meta.should.have.property("totalElements");
					res.body.meta.should.have.property("number");
					res.body.meta.should.have.property("size");
					res.body.should.have.property("data");
					res.body.data.should.be.a("array");
					res.body.data[0].should.have.property("citizenId");
					res.body.data[0].should.have.property("patientId");
					res.body.data[0].should.have.property("UHId");
					res.body.data[0].should.have.property("encounterId");
					res.body.data[0].should.have.property("encounterDate");
					res.body.data[0].should.have.property("medicalSigns");
					res.body.data[0].should.have.property("audit");
					res.body.data[0].audit.should.have.property("dateCreated");
					res.body.data[0].audit.should.have.property("createdBy");
					res.body.data[0].audit.should.have.property("dateModified");
					res.body.data[0].audit.should.have.property("modifiedBy");
					done();
				});
		});
		it("it should not get vitals if none of the required details is provided", (done) => {
			chai
				.request(app)
				.get("/vitalsigns/filter?page=1")
				.end((err, res) => {
					res.should.have.status(206);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors[0].should.have
						.property("field")
						.eql("encounterDate or encounterId or UHId");
					res.body.errors[0].should.have
						.property("message")
						.eql("No or invalid value found");
					done();
				});
		});
	});

	describe("/GET/vitalsigns/:vitalSignId", () => {
		it("it should get a vitalsign if vitalSignId is provided and is valid", (done) => {
			chai
				.request(app)
				.get(`/vitalsigns/${vitalSignId}`)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("citizenId");
					res.body.should.have.property("patientId");
					res.body.should.have.property("UHId");
					res.body.should.have.property("encounterId");
					res.body.should.have.property("encounterDate");
					res.body.should.have.property("medicalSigns");
					res.body.medicalSigns.should.be.a("array");
					res.body.should.have.property("audit");
					res.body.audit.should.be.a("object");
					res.body.audit.should.have.property("dateCreated");
					res.body.audit.should.have.property("createdBy");
					res.body.audit.should.have.property("dateModified");
					res.body.audit.should.have.property("modifiedBy");
					done();
				});
		});
		it("it should not get a vitalsign if vitalSignId is invalid", (done) => {
			chai
				.request(app)
				.get("/vitalsigns/6257ab88b219eb5d575d1111")
				.end((err, res) => {
					res.should.have.status(404);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors[0].should.have.property("field").eql("vitalSignId");
					done();
				});
		});
	});

	describe("/PATCH/vitalsigns/:vitalSignId", () => {
		it("it should update a vitalsign if vitalSignId is provided and is valid", (done) => {
			chai
				.request(app)
				.patch(`/vitalsigns/${vitalSignId}`)
				.send({
					medicalSigns: [
						{
							signName: "Patchedstring",
							signValue: "Patchedvalue",
						},
						{
							signName: "string2",
							signValue: "value2",
						},
					],
				})
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("citizenId");
					res.body.should.have.property("patientId");
					res.body.should.have.property("UHId");
					res.body.should.have.property("encounterId");
					res.body.should.have.property("encounterDate");
					res.body.should.have.property("medicalSigns");
					res.body.medicalSigns.should.be.a("array");
					res.body.should.have.property("audit");
					res.body.audit.should.be.a("object");
					res.body.audit.should.have.property("dateCreated");
					res.body.audit.should.have.property("createdBy");
					res.body.audit.should.have.property("dateModified");
					res.body.audit.should.have.property("modifiedBy");
					done();
				});
		});
		it("it should not update a vitalsign if vitalSignId is invalid", (done) => {
			chai
				.request(app)
				.patch("/vitalsigns/6257ab88b219eb5d575d1111")
				.send({
					medicalSigns: [
						{
							signName: "Patchedstring",
							signValue: "Patchedvalue",
						},
						{
							signName: "string2",
							signValue: "value2",
						},
					],
				})
				.end((err, res) => {
					res.should.have.status(404);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors[0].should.have.property("field").eql("vitalSignId");
					done();
				});
		});
	});

	after((done) => {
		VitalSignsModel.remove({}, (err) => {
			done();
		});
	});
});
