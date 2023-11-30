const mongoose = require("mongoose");
const encounters = require("../app/models/encounter");
const chai = require("chai");
const chaiHttp = require("chai-http");
const app = require("../app");
const { describe, it } = require("mocha");
const should = chai.should();
chai.use(chaiHttp);

describe("Encounter", () => {
	let encounterId = "";

	before((done) => {
		encounters.deleteOne({}, (err) => {
			done();
		});
	});

	describe("/POST encounters", () => {
		it("it should not create new encounter without patientId", (done) => {
			let encounter = {
				citizenId: "String",
				// patientId: "String",
				purpose: "Doctor",
				assignedTo: {
					staffId: "String",
					staffName: "String",
					staffType: "String",
				},
				referredByAshaWorker: {
					staffId: "String",
					staffName: "String",
					staffType: "String",
				},
				referredByDoctor: {
					staffId: "String",
					staffName: "String",
					staffType: "String",
				},
				attendantName: "String",
				attendantPhone: "String",
			};
			chai
				.request(app)
				.post("/encounters?fromSaga=true")
				.send(encounter)
				.end((err, res) => {
					// console.log(res.body.errors);
					res.should.have.status(400);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors[0].should.have.property("field").eql("patientId");
					done();
				});
		});
		it("it should create a encounter if all the required data is provided", (done) => {
			let encounter = {
				citizenId: "String",
				patientId: "String",
				purpose: "Doctor",
				assignedTo: {
					staffId: "String",
					staffName: "String",
					staffType: "String",
				},
				referredByAshaWorker: {
					staffId: "String",
					staffName: "String",
					staffType: "String",
				},
				referredByDoctor: {
					staffId: "String",
					staffName: "String",
					staffType: "String",
				},
				attendantName: "String",
				attendantPhone: "String",
				audit: {
					createdBy: "string",
					dateCreated: "2022-03-07T10:10:22.727Z",
					modifiedBy: "string",
					dateModified: "2022-03-07T10:10:22.727Z",
				},
			};
			chai
				.request(app)
				.post("/encounters?fromSaga=true")
				.send(encounter)
				.end((err, res) => {
					encounterId = res.body._id;
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("audit");
					done();
				});
		});
	});

	describe("/POST/filter filterEncounters", () => {
	
		it("it should filter the encounters if purpose and encounterDate is provided", (done) => {
			let body = {
				purpose: "Doctor",
				encounterDate: "2022-03-17T07:28:58.262Z",
			};
			chai
				.request(app)
				.post("/encounters/filter")
				.send(body)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("Array");
					res.body[0].should.have.property("purpose").eql(body.purpose);
					done();
				});
		});
	});

	describe("/GET/:encounterId fetchEncounter", () => {
		it("it should not fetch encounter if given id is invalid", (done) => {
			chai
				.request(app)
				.get("/encounters/6239a7797ec728e9e9a79ba7")
				.end((err, res) => {
					res.should.have.status(404);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors[0].should.have.property("field").eql("_id");
					done();
				});
		});
		it("it should GET an encounter by the given id", (done) => {
			chai
				.request(app)
				.get(`/encounters/${encounterId}`)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("_id").eql(encounterId);
					res.body.should.have.property("citizenId");
					res.body.should.have.property("purpose");
					res.body.should.have.property("assignedTo");
					res.body.should.have.property("referredByAshaWorker");
					res.body.should.have.property("audit");
					done();
				});
		});
	});

	describe("/PATCH/:encounterId updateEncounter", () => {
		it("it should fetch an encounter by the given id", (done) => {
			let newData = {
				citizenId: "String",
				patientId: "String",
				purpose: "Doctor",
				assignedTo: {
					staffId: "String",
					staffName: "String",
					staffType: "String",
				},
				referredByAshaWorker: {
					staffId: "String",
					staffName: "String",
					staffType: "String",
				},
				referredByDoctor: {
					staffId: "String",
					staffName: "String",
					staffType: "String",
				},
				attendantName: "String",
				attendantPhone: "String",
				audit: {
					createdBy: "string",
					dateCreated: "2022-03-07T10:10:22.727Z",
					modifiedBy: "string",
					dateModified: "2022-03-07T10:10:22.727Z",
				},
			};
			chai
				.request(app)
				.patch(`/encounters/${encounterId}/?fromSaga=true`)
				.send(newData)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("patientId");
					res.body.should.have.property("audit");
					done();
				});
		});
	});

	after((done) => {
		encounters.deleteOne(() => {
			done();
		});
	});
});
