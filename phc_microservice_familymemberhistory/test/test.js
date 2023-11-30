const mongoose = require("mongoose");
const FamilyMemberHistoryModel = require("../app/models/FamilyMemberHistory");
const chai = require("chai");
const chaiHttp = require("chai-http");
const app = require("../app");
const { describe, it } = require("mocha");
const should = chai.should();
chai.use(chaiHttp);

describe("FamilyMemberHistory", () => {
	let historyId = "";
	let encounterId = "";
	let UHId = "";
	let encounterDate = "";

	before((done) => {
		FamilyMemberHistoryModel.remove({}, (err) => {
			done();
		});
	});

	describe("/POST familyMemberHistory", () => {
		it("it should create a new familyMemberHistory", (done) => {
			const familyMemberHistory = {
				UHId: "string",
				encounterId: "string",
				encounterDate: "2022-05-11T06:52:38.713Z",
				patientId: "string",
				doctor: "staffid of medical officer",
				conditions: [
					{
						member: "family member name",
						problem: "the medical condition",
						onsetPeriod: "string iso8601 duration",
					},
				],
			};

			chai
				.request(app)
				.post("/familymemberhistory")
				.send(familyMemberHistory)
				.end((err, res) => {
					historyId = res.body._id;
					encounterId = res.body.encounterId;
					UHId = res.body.UHId;
					encounterDate = res.body.encounterDate;
					res.should.have.status(201);
					res.body.should.be.a("object");
					res.body.should.have.property("UHId");
					res.body.UHId.should.be.a("string");
					res.body.should.have.property("encounterId");
					res.body.encounterId.should.be.a("string");
					res.body.should.have.property("encounterDate");
					res.body.encounterDate.should.be.a("String");
					res.body.should.have.property("patientId");
					res.body.patientId.should.be.a("string");
					res.body.should.have.property("doctor");
					res.body.doctor.should.be.a("string");
					res.body.should.have.property("conditions");
					res.body.conditions.should.be.a("array");
					res.body.should.have.property("audit");
					res.body.audit.should.be.a("object");
					res.body.should.have.property("_id");
					res.body._id.should.be.a("string");
					done();
				});
		});
		it("it should not create a new familyMemberHistory if encounterDate is not of Date type", (done) => {
			chai
				.request(app)
				.post("/familymemberhistory")
				.send({
					UHId: "string",
					encounterId: "string",
					encounterDate: "checkkkkk",
					patientId: "string",
					doctor: "staffid of medical officer",
					conditions: [
						{
							member: "family member name",
							problem: "the medical condition",
							onsetPeriod: "string iso8601 duration",
						},
					],
				})
				.end((err, res) => {
					res.should.have.status(400);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors.should.be.a("array");
					res.body.errors[0].should.have.property("field").eql("encounterDate");
					res.body.errors[0].should.have
						.property("message")
						.eql("Invalid value");
					done();
				});
		});
		it("it should not create a new familymemberhistory if doctor is not of string type", (done) => {
			chai
				.request(app)
				.post("/familymemberhistory")
				.send({
					UHId: "string",
					encounterId: "string",
					encounterDate: "2022-05-11T06:52:38.713Z",
					patientId: "string",
					doctor: 1234,
					conditions: [
						{
							member: "family member name",
							problem: "the medical condition",
							onsetPeriod: "string iso8601 duration",
						},
					],
				})
				.end((err, res) => {
					res.should.have.status(400);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors.should.be.a("array");
					res.body.errors[0].should.have.property("field").eql("doctor");
					res.body.errors[0].should.have
						.property("message")
						.eql("Invalid value");
					done();
				});
		});
	});

	describe("/GET familyMemberHistory", () => {
		it("it should not fetch the familyMemberHistory", (done) => {
			chai
				.request(app)
				.get("/familymemberhistory/6270ebd3768b9395e0111111")
				.end((err, res) => {
					res.should.have.status(404);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors.should.be.a("array");
					res.body.errors[0].should.have.property("field").eql("historyId");
					res.body.errors[0].should.have
						.property("message")
						.eql("FamilyMemberHistory not found for 6270ebd3768b9395e0111111");
					done();
				});
		});
		it("it should fetch the MedicalConditions", (done) => {
			chai
				.request(app)
				.get(`/familymemberhistory/${historyId}`)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("_id").eql(historyId);
					res.body.should.have.property("UHId");
					res.body.UHId.should.be.a("string");
					res.body.should.have.property("encounterId");
					res.body.encounterId.should.be.a("string");
					res.body.should.have.property("encounterDate");
					res.body.encounterDate.should.be.a("String");
					res.body.should.have.property("patientId");
					res.body.patientId.should.be.a("string");
					res.body.should.have.property("doctor");
					res.body.doctor.should.be.a("string");
					res.body.should.have.property("conditions");
					res.body.conditions.should.be.a("array");
					res.body.should.have.property("audit");
					res.body.audit.should.be.a("object");
					done();
				});
		});
	});

	describe("/PATCH familyMemberHistory", () => {
		it("it should not update the familyMemberHistory", (done) => {
			chai
				.request(app)
				.patch(`/familymemberhistory/6270ebd3768b9395e0111111`)
				.send({
					doctor: "staffid",
					conditions: [
						{
							member: "family member name patched",
							problem: "the medical condition patched",
						},
					],
				})
				.end((err, res) => {
					res.should.have.status(404);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors.should.be.a("array");
					res.body.errors[0].should.have.property("field").eql("historyId");
					res.body.errors[0].should.have
						.property("message")
						.eql("FamilyMemberHistory not found for 6270ebd3768b9395e0111111");
					done();
				});
		});
		it("it should update the familyMemberHistory", (done) => {
			chai
				.request(app)
				.patch(`/familymemberhistory/${historyId}`)
				.send({
					doctor: "staffid",
					conditions: [
						{
							member: "family member name patched",
							problem: "the medical condition patched",
						},
					],
				})
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("_id").eql(historyId);
					res.body.should.have.property("UHId");
					res.body.UHId.should.be.a("string");
					res.body.should.have.property("encounterId");
					res.body.encounterId.should.be.a("string");
					res.body.should.have.property("encounterDate");
					res.body.encounterDate.should.be.a("String");
					res.body.should.have.property("patientId");
					res.body.patientId.should.be.a("string");
					res.body.should.have.property("doctor");
					res.body.doctor.should.be.a("string");
					res.body.should.have.property("conditions");
					res.body.conditions.should.be.a("array");
					res.body.should.have.property("audit");
					res.body.audit.should.be.a("object");
					done();
				});
		});
	});

	describe("/GET filter familyMemberHistory", () => {
		it("it should filter the familyMemberHistory if page, size and encounterId is provided", (done) => {
			chai
				.request(app)
				.get(
					`/familymemberhistory/filter?page=1&size=2&encounterId=${encounterId}`
				)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("meta");
					res.body.meta.should.be.a("object");
					res.body.meta.should.have.property("totalPages");
					res.body.meta.totalPages.should.be.a("number");
					res.body.meta.should.have.property("totalElements");
					res.body.meta.totalElements.should.be.a("number");
					res.body.meta.should.have.property("size");
					res.body.meta.size.should.be.a("number");
					res.body.meta.should.have.property("number");
					res.body.meta.number.should.be.a("number");
					res.body.should.have.property("data");
					res.body.data.should.be.a("array");
					res.body.data[0].should.have.property("UHId");
					res.body.data[0].UHId.should.be.a("string");
					res.body.data[0].should.have.property("encounterId");
					res.body.data[0].encounterId.should.be.a("string");
					res.body.data[0].should.have.property("patientId");
					res.body.data[0].patientId.should.be.a("string");
					res.body.data[0].should.have.property("encounterDate");
					res.body.data[0].encounterDate.should.be.a("String");
					res.body.data[0].should.have.property("doctor");
					res.body.data[0].doctor.should.be.a("string");
					res.body.data[0].should.have.property("conditions");
					res.body.data[0].conditions.should.be.a("array");
					res.body.data[0].should.have.property("audit");
					res.body.data[0].audit.should.be.a("object");
					done();
				});
		});
		it("it should filter the familyMemberHistory if page, size and UHId is provided", (done) => {
			chai
				.request(app)
				.get(`/familymemberhistory/filter?page=1&size=2&UHId=${UHId}`)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("meta");
					res.body.meta.should.be.a("object");
					res.body.meta.should.have.property("totalPages");
					res.body.meta.totalPages.should.be.a("number");
					res.body.meta.should.have.property("totalElements");
					res.body.meta.totalElements.should.be.a("number");
					res.body.meta.should.have.property("size");
					res.body.meta.size.should.be.a("number");
					res.body.meta.should.have.property("number");
					res.body.meta.number.should.be.a("number");
					res.body.should.have.property("data");
					res.body.data.should.be.a("array");
					res.body.data[0].should.have.property("UHId");
					res.body.data[0].UHId.should.be.a("string");
					res.body.data[0].should.have.property("encounterId");
					res.body.data[0].encounterId.should.be.a("string");
					res.body.data[0].should.have.property("patientId");
					res.body.data[0].patientId.should.be.a("string");
					res.body.data[0].should.have.property("encounterDate");
					res.body.data[0].encounterDate.should.be.a("String");
					res.body.data[0].should.have.property("doctor");
					res.body.data[0].doctor.should.be.a("string");
					res.body.data[0].should.have.property("conditions");
					res.body.data[0].conditions.should.be.a("array");
					res.body.data[0].should.have.property("audit");
					res.body.data[0].audit.should.be.a("object");
					done();
				});
		});
		it("it should filter the familyMemberHistory if page, size and encounterDate is provided", (done) => {
			chai
				.request(app)
				.get(
					`/familymemberhistory/filter?page=1&size=2&encounterDate=${encounterDate}`
				)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("meta");
					res.body.meta.should.be.a("object");
					res.body.meta.should.have.property("totalPages");
					res.body.meta.totalPages.should.be.a("number");
					res.body.meta.should.have.property("totalElements");
					res.body.meta.totalElements.should.be.a("number");
					res.body.meta.should.have.property("size");
					res.body.meta.size.should.be.a("number");
					res.body.meta.should.have.property("number");
					res.body.meta.number.should.be.a("number");
					res.body.should.have.property("data");
					res.body.data.should.be.a("array");
					res.body.data[0].should.have.property("UHId");
					res.body.data[0].UHId.should.be.a("string");
					res.body.data[0].should.have.property("encounterId");
					res.body.data[0].encounterId.should.be.a("string");
					res.body.data[0].should.have.property("patientId");
					res.body.data[0].patientId.should.be.a("string");
					res.body.data[0].should.have.property("encounterDate");
					res.body.data[0].encounterDate.should.be.a("String");
					res.body.data[0].should.have.property("doctor");
					res.body.data[0].doctor.should.be.a("string");
					res.body.data[0].should.have.property("conditions");
					res.body.data[0].conditions.should.be.a("array");
					res.body.data[0].should.have.property("audit");
					res.body.data[0].audit.should.be.a("object");
					done();
				});
		});
	});

	after((done) => {
		FamilyMemberHistoryModel.remove(() => {
			done();
		});
	});
});
