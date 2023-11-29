const mongoose = require("mongoose");
const MedicalConditionModel = require("../app/models/MedicalCondition");
const chai = require("chai");
const chaiHttp = require("chai-http");
const app = require("../app");
const { describe, it } = require("mocha");
const should = chai.should();
chai.use(chaiHttp);

describe("MedicalCondition", () => {
	let conditionId = "";
	let encounterId = "";
	let UHId = "";
	let encounterDate = "";

	before((done) => {
		MedicalConditionModel.remove({}, (err) => {
			done();
		});
	});

	describe("/POST Create MedicalConditions", () => {
		it("it should create a new MedicalCondition", (done) => {
			chai
				.request(app)
				.post("/MedicalConditions")
				.send({
					encounterId: "String",
					patientId: "String",
					UHId: "String",
					recordedBy: "String",
					doctor: "String",
					effectiveDate: "2022-05-03T06:57:06.237Z",
					effectivePeriod: "String",
					complaintText: "String",
				})
				.end((err, res) => {
					conditionId = res.body._id;
					encounterId = res.body.encounterId;
					UHId = res.body.UHId;
					encounterDate = res.body.effectiveDate;
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("audit");
					res.body.audit.should.be.a("object");
					done();
				});
		});
		it("it should not create a new MedicalCondition if effectiveDate is not of Date type", (done) => {
			chai
				.request(app)
				.post("/MedicalConditions")
				.send({
					encounterId: "String",
					patientId: "String",
					UHId: "String",
					recordedBy: "String",
					doctor: "String",
					effectiveDate: "checkkk",
					effectivePeriod: "String",
					complaintText: "String",
				})
				.end((err, res) => {
					res.should.have.status(400);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors.should.be.a("array");
					res.body.errors[0].should.have.property("field").eql("effectiveDate");
					res.body.errors[0].should.have
						.property("message")
						.eql("Invalid value");
					done();
				});
		});
		it("it should not create a new MedicalCondition if effectivePeriod is not of string type", (done) => {
			chai
				.request(app)
				.post("/MedicalConditions")
				.send({
					encounterId: "String",
					patientId: "String",
					UHId: "String",
					recordedBy: "String",
					doctor: "String",
					effectiveDate: "2022-05-03T06:57:06.237Z",
					effectivePeriod: 1234,
					complaintText: "String",
				})
				.end((err, res) => {
					res.should.have.status(400);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors.should.be.a("array");
					res.body.errors[0].should.have
						.property("field")
						.eql("effectivePeriod");
					res.body.errors[0].should.have
						.property("message")
						.eql("Invalid value");
					done();
				});
		});
	});

	describe("/GET fetch MedicalConditions", () => {
		it("it should not fetch the MedicalConditions", (done) => {
			chai
				.request(app)
				.get("/MedicalConditions/6270ebd3768b9395e0111111")
				.end((err, res) => {
					res.should.have.status(404);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors.should.be.a("array");
					res.body.errors[0].should.have.property("field").eql("conditionId");
					res.body.errors[0].should.have
						.property("message")
						.eql("Chief Complaint not found for 6270ebd3768b9395e0111111");
					done();
				});
		});
		it("it should fetch the MedicalConditions", (done) => {
			chai
				.request(app)
				.get(`/MedicalConditions/${conditionId}`)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("_id").eql(conditionId);
					res.body.should.have.property("encounterId");
					res.body.should.have.property("patientId");
					res.body.should.have.property("UHId");
					res.body.should.have.property("recordedBy");
					res.body.should.have.property("doctor");
					res.body.should.have.property("effectiveDate");
					res.body.should.have.property("effectivePeriod");
					res.body.should.have.property("complaintText");
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

	describe("/PATCH Update MedicalConditions", () => {
		it("it should update the MedicalConditions", (done) => {
			chai
				.request(app)
				.patch(`/MedicalConditions/${conditionId}`)
				.send({
					doctor: "Patched staff Id of the medical officer",
					complaintText: "Patched complaint text",
				})
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("_id").eql(conditionId);
					res.body.should.have.property("doctor");
					res.body.should.have.property("complaintText");
					res.body.doctor.should.eql("Patched staff Id of the medical officer");
					res.body.complaintText.should.eql("Patched complaint text");
					done();
				});
		});
		it("it should not update the MedicalConditions if conditionId is invalid", (done) => {
			chai
				.request(app)
				.patch("/MedicalConditions/6270ebd3768b9395e0111111")
				.send({
					doctor: "Patched staff Id of the medical officer",
					complaintText: "Patched complaint text",
				})
				.end((err, res) => {
					res.should.have.status(404);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors.should.be.a("array");
					res.body.errors[0].should.have.property("field").eql("_id");
					res.body.errors[0].should.have
						.property("message")
						.eql(
							"Medical condition not found for _id 6270ebd3768b9395e0111111"
						);
					done();
				});
		});
	});

	describe("/GET filter MedicalConditions", () => {
		it("it should filter the MedicalConditions if page, size and encounterId is provided", (done) => {
			chai
				.request(app)
				.get(
					`/MedicalConditions/filter?page=1&size=5&encounterId=${encounterId}`
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
					res.body.data[0].should.have.property("encounterId").eql(encounterId);
					done();
				});
		});
		it("it should filter the MedicalConditions if page, size and encounterDate is provided", (done) => {
			chai
				.request(app)
				.get(
					`/MedicalConditions/filter?page=1&size=5&encounterDate=${encounterDate}`
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
		it("it should filter the MedicalConditions if page, size and UHId is provided", (done) => {
			chai
				.request(app)
				.get(`/MedicalConditions/filter?page=1&size=5&UHId=${UHId}`)
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
		it("it should not filter the MedicalConditions if page and size in not provided", (done) => {
			chai
				.request(app)
				.get("/MedicalConditions/filter")
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
		MedicalConditionModel.remove(() => {
			done();
		});
	});
});
