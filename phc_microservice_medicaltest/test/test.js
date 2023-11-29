const mongoose = require("mongoose");
const MedicalTestModel = require("../app/models/MedicalTest");
const chai = require("chai");
const chaiHttp = require("chai-http");
const app = require("../app");
const { describe, it } = require("mocha");
const should = chai.should();
chai.use(chaiHttp);

describe("MedicalTest", () => {
	let testId = "";
	let encounterId = "";
	let encounterDate = "";
	let UHId = "";
	let doctor = "";

	before((done) => {
		MedicalTestModel.remove({}, (err) => {
			done();
		});
	});

	describe("/POST Create MedicalTest", () => {
		it("it should POST a new MedicalTest", (done) => {
			chai
				.request(app)
				.post("/medicaltests")
				.send({
					patientId: "string",
					UHId: "string",
					encounterId: "string",
					effectiveDate: "2022-05-03T12:23:57.383Z",
					doctor: "string",
					tests: ["test1", "test2", "test3"],
				})
				.end((err, res) => {
					testId = res.body._id;
					encounterId = res.body.encounterId;
					encounterDate = res.body.effectiveDate;
					UHId = res.body.UHId;
					doctor = res.body.doctor;
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("UHId");
					res.body.should.have.property("encounterId");
					res.body.should.have.property("effectiveDate");
					res.body.should.have.property("patientId");
					res.body.should.have.property("doctor");
					res.body.should.have.property("tests");
					res.body.should.have.property("audit");
					done();
				});
		});
		it("it should not create a new medicaltest if UHID is not string", (done) => {
			chai
				.request(app)
				.post("/medicaltests")
				.send({
					UHId: 12345,
					encounterId: "string",
					effectiveDate: "2022-05-03T12:23:57.383Z",
					patientId: "string",
					doctor: "string",
					tests: ["test1", "test2", "test3"],
				})
				.end((err, res) => {
					res.should.have.status(400);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors.should.be.a("array");
					res.body.errors[0].should.have.property("field").eql("UHId");
					res.body.errors[0].should.have
						.property("message")
						.eql("Invalid value");
					done();
				});
		});
		it("it should not create a new medicaltest if length of tests is less than 1", (done) => {
			chai
				.request(app)
				.post("/medicaltests")
				.send({
					UHId: "string",
					encounterId: "string",
					patientId: "string",
					doctor: "string",
					tests: [],
				})
				.end((err, res) => {
					res.should.have.status(400);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors.should.be.a("array");
					res.body.errors[0].should.have.property("field").eql("tests");
					res.body.errors[0].should.have
						.property("message")
						.eql("Invalid value");
					done();
				});
		});
	});

	describe("/GET/:testId fetch MedicalTest", () => {
		it("it should not fetch the MedicalTest", (done) => {
			chai
				.request(app)
				.get("/medicaltests/6270ebd3768b9395e0111111")
				.end((err, res) => {
					res.should.have.status(404);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors.should.be.a("array");
					res.body.errors[0].should.have.property("field").eql("testId");
					res.body.errors[0].should.have
						.property("message")
						.eql("Investigation not found for 6270ebd3768b9395e0111111");
					done();
				});
		});
		it("it should fetch the MedicalTest", (done) => {
			chai
				.request(app)
				.get(`/medicaltests/${testId}`)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("_id").eql(testId);
					res.body.should.have.property("patientId");
					res.body.should.have.property("UHId");
					res.body.should.have.property("encounterId");
					res.body.should.have.property("doctor");
					res.body.should.have.property("tests");
					res.body.tests.should.be.a("array");
					res.body.should.have.property("audit");
					res.body.audit.should.be.a("object");
					res.body.audit.should.have.property("createdBy");
					res.body.audit.should.have.property("dateCreated");
					res.body.audit.should.have.property("modifiedBy");
					res.body.audit.should.have.property("dateModified");
					done();
				});
		});
	});

	describe("/PATCH Update medicaltests", () => {
		it("it should update the medicaltests", (done) => {
			chai
				.request(app)
				.patch(`/medicaltests/${testId}`)
				.send({
					patientId: "string",
					UHId: "string",
					tests: ["test1", "test2", "test3"],
				})
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("_id").eql(testId);
					res.body.should.have.property("patientId");
					res.body.should.have.property("UHId");
					res.body.patientId.should.eql("string");
					res.body.UHId.should.eql("string");
					done();
				});
		});
		it("it should not update the medicaltests if testId is invalid", (done) => {
			chai
				.request(app)
				.patch("/medicaltests/6270ebd3768b9395e0111111")
				.send({
					patientId: "string",
					UHId: "string",
					tests: ["test1", "test2", "test3"],
				})
				.end((err, res) => {
					res.should.have.status(404);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors.should.be.a("array");
					res.body.errors[0].should.have.property("field").eql("_id");
					res.body.errors[0].should.have
						.property("message")
						.eql("MedicalTest not found for _id 6270ebd3768b9395e0111111");
					done();
				});
		});
	});

	describe("/GET filter medicaltests", () => {
		it("it should filter the medicaltests if page, size and encounterId is provided", (done) => {
			chai
				.request(app)
				.get(`/medicaltests/filter?page=1&size=5&encounterId=${encounterId}`)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("meta");
					res.body.meta.should.be.a("object");
					res.body.meta.should.have.property("totalPages");
					res.body.meta.should.have.property("totalElements");
					res.body.meta.should.have.property("number");
					res.body.meta.should.have.property("size");
					res.body.should.have.property("data");
					res.body.data.should.be.a("array");
					res.body.data[0].should.have.property("_id");
					res.body.data[0].should.have.property("encounterId").eql(encounterId);
					done();
				});
		});
		it("it should filter the medicaltests if page, size and encounterDate is provided", (done) => {
			chai
				.request(app)
				.get(
					`/medicaltests/filter?page=1&size=5&encounterDate=${encounterDate}`
				)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("meta");
					res.body.meta.should.be.a("object");
					res.body.meta.should.have.property("totalPages");
					res.body.meta.should.have.property("totalElements");
					res.body.meta.should.have.property("number");
					res.body.meta.should.have.property("size");
					res.body.should.have.property("data");
					res.body.data.should.be.a("array");
					res.body.data[0].should.have.property("_id");
					res.body.data[0].should.have
						.property("effectiveDate")
						.eql(encounterDate);
					done();
				});
		});
		it("it should filter the medicaltests if page, size and UHId is provided", (done) => {
			chai
				.request(app)
				.get(`/medicaltests/filter?page=1&size=5&UHId=${UHId}`)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("meta");
					res.body.meta.should.be.a("object");
					res.body.meta.should.have.property("totalPages");
					res.body.meta.should.have.property("totalElements");
					res.body.meta.should.have.property("number");
					res.body.meta.should.have.property("size");
					res.body.should.have.property("data");
					res.body.data.should.be.a("array");
					res.body.data[0].should.have.property("_id");
					res.body.data[0].should.have.property("UHId").eql(UHId);
					done();
				});
		});
		it("it should filter the medicaltests if page, size and doctor is provided", (done) => {
			chai
				.request(app)
				.get(`/medicaltests/filter?page=1&size=5&doctor=${doctor}`)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("meta");
					res.body.meta.should.be.a("object");
					res.body.meta.should.have.property("totalPages");
					res.body.meta.should.have.property("totalElements");
					res.body.meta.should.have.property("number");
					res.body.meta.should.have.property("size");
					res.body.should.have.property("data");
					res.body.data.should.be.a("array");
					res.body.data[0].should.have.property("_id");
					res.body.data[0].should.have.property("doctor").eql(doctor);
					done();
				});
		});
		it("it should not filter the medicaltests if page and size in not provided", (done) => {
			chai
				.request(app)
				.get("/medicaltests/filter")
				.end((err, res) => {
					res.should.have.status(206);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors.should.be.a("array");
					res.body.errors[0].should.have.property("field").eql("size");
					res.body.errors[0].should.have
						.property("message")
						.eql("No or invalid value found");
					done();
				});
		});
	});

	after((done) => {
		MedicalTestModel.remove(() => {
			done();
		});
	});
});
