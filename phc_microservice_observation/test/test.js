const mongoose = require("mongoose");
const ObservationModel = require("../app/models/Observation");
const chai = require("chai");
const chaiHttp = require("chai-http");
const app = require("../app");
const { describe, it } = require("mocha");
const should = chai.should();
chai.use(chaiHttp);

describe("Observation", () => {
	let observationId = "";
	let encounterId = "";
	let encounterDate = "";
	let UHId = "";

	before((done) => {
		ObservationModel.remove({}, (err) => {
			done();
		});
	});

	describe("/POST Create Observation", () => {
		it("it should POST a new Observation", (done) => {
			chai
				.request(app)
				.post("/observations")
				.send({
					citizenId: "string",
					UHId: "string",
					encounterId: "string",
					effectiveDate: "2022-05-03T12:23:57.383Z",
					patientId: "string",
					doctor: "string",
					assessments: [
						{
							assessmentTitle: "Cardio Vascular Assessment",
							observations: [
								{
									observationName: "Cardiac Rhythm",
									observationValues: [
										"Normal sinus rhythm",
										"Sinus arrhythmia",
										"Sinus Arrest",
									],
								},
								{
									observationName: "Heart Rhythm",
									observationValues: ["Regular", "Regularly irregular"],
								},
							],
						},
					],
				})
				.end((err, res) => {
					observationId = res.body._id;
					encounterId = res.body.encounterId;
					encounterDate = res.body.effectiveDate;
					UHId = res.body.UHId;
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("UHId");
					res.body.should.have.property("encounterId");
					res.body.should.have.property("patientId");
					res.body.should.have.property("doctor");
					res.body.should.have.property("assessments");
					res.body.should.have.property("audit");
					done();
				});
		});
		it("it should not create a new Observation if UHID is not string", (done) => {
			chai
				.request(app)
				.post("/observations")
				.send({
					UHId: 12345,
					encounterId: "string",
					patientId: "string",
					doctor: "string",
					assessments: [
						{
							assessmentTitle: "Cardio Vascular Assessment",
							observations: [
								{
									observationName: "Cardiac Rhythm",
									observationValues: [
										"Normal sinus rhythm",
										"Sinus arrhythmia",
										"Sinus Arrest",
									],
								},
								{
									observationName: "Heart Rhythm",
									observationValues: ["Regular", "Regularly irregular"],
								},
							],
						},
					],
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
		it("it should not create a new Observation if length of assessments is less than 1", (done) => {
			chai
				.request(app)
				.post("/observations")
				.send({
					UHId: "string",
					encounterId: "string",
					patientId: "string",
					doctor: "string",
					assessments: [],
				})
				.end((err, res) => {
					res.should.have.status(400);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors.should.be.a("array");
					res.body.errors[0].should.have.property("field").eql("assessments");
					res.body.errors[0].should.have
						.property("message")
						.eql("Invalid value");
					done();
				});
		});
	});

	describe("/GET/:observationId fetch Observation", () => {
		it("it should not fetch the Observation", (done) => {
			chai
				.request(app)
				.get("/observations/6270ebd3768b9395e0111111")
				.end((err, res) => {
					res.should.have.status(404);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors.should.be.a("array");
					res.body.errors[0].should.have.property("field").eql("observationId");
					res.body.errors[0].should.have
						.property("message")
						.eql("Physical Examination not found for 6270ebd3768b9395e0111111");
					done();
				});
		});
		it("it should fetch the Observation", (done) => {
			chai
				.request(app)
				.get(`/observations/${observationId}`)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("_id").eql(observationId);
					res.body.should.have.property("citizenId");
					res.body.should.have.property("patientId");
					res.body.should.have.property("UHId");
					res.body.should.have.property("encounterId");
					res.body.should.have.property("doctor");
					res.body.should.have.property("assessments");
					res.body.assessments.should.be.a("array");
					res.body.assessments[0].should.have.property("assessmentTitle");
					res.body.assessments[0].should.have.property("observations");
					res.body.assessments[0].observations.should.be.a("array");
					res.body.assessments[0].observations[0].should.have.property(
						"observationName"
					);
					res.body.assessments[0].observations[0].should.have.property(
						"observationValues"
					);
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

	describe("/PATCH/:observationId update Observation", () => {
		it("it should update the Observation", (done) => {
			chai
				.request(app)
				.patch(`/observations/${observationId}`)
				.send({
					doctor: "patchedString",
					assessments: [
						{
							assessmentTitle: "Cardio Vascular patch",
							observations: [
								{
									observationName: "Cardiac rhythm patch",
									observationValues: [
										"Normal sinus rhythm",
										"Sinus arrhythmia",
										"checking patch",
									],
								},
							],
						},
					],
				})
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("_id").eql(observationId);
					res.body.should.have.property("citizenId");
					res.body.should.have.property("patientId");
					res.body.should.have.property("UHId");
					res.body.should.have.property("encounterId");
					res.body.should.have.property("doctor");
					res.body.should.have.property("assessments");
					res.body.assessments.should.be.a("array");
					res.body.assessments[0].should.have.property("assessmentTitle");
					res.body.assessments[0].should.have.property("observations");
					res.body.assessments[0].observations.should.be.a("array");
					res.body.assessments[0].observations[0].should.have.property(
						"observationName"
					);
					res.body.assessments[0].observations[0].should.have.property(
						"observationValues"
					);
					res.body.assessments[0].observations[0].observationValues.should.be.a(
						"array"
					);
					res.body.should.have.property("audit");
					res.body.audit.should.be.a("object");
					res.body.audit.should.have.property("createdBy");
					res.body.audit.should.have.property("dateCreated");
					res.body.audit.should.have.property("modifiedBy");
					res.body.audit.should.have.property("dateModified");
					done();
				});
		});
		it("it should not update the Observation if invalid values are passed", (done) => {
			chai
				.request(app)
				.patch(`/observations/${observationId}`)
				.send({
					UHId: 1234,
					doctor: 12234,
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
					res.body.errors[1].should.have.property("field").eql("doctor");
					res.body.errors[1].should.have
						.property("message")
						.eql("Invalid value");
					done();
				});
		});
	});

	describe("/GET filter observations", () => {
		it("it should filter the observations if page, size and encounterId is provided", (done) => {
			chai
				.request(app)
				.get(`/observations/filter?page=1&size=5&encounterId=${encounterId}`)
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
		it("it should filter the observations if page, size and encounterDate is provided", (done) => {
			chai
				.request(app)
				.get(
					`/observations/filter?page=1&size=5&encounterDate=${encounterDate}`
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
		it("it should filter the observations if page, size and UHId is provided", (done) => {
			chai
				.request(app)
				.get(`/observations/filter?page=1&size=5&UHId=${UHId}`)
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
		it("it should not filter the observations if page and size in not provided", (done) => {
			chai
				.request(app)
				.get("/observations/filter")
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
		ObservationModel.remove(() => {
			done();
		});
	});
});
